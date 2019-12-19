package ni.devotion.multidataparser.file

import android.app.Notification.Builder
import android.app.NotificationManager
import android.content.Context
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Process
import android.util.Log
import ni.devotion.multidataparser.R
import ni.devotion.multidataparser.`interface`.OnDownloadResult
import ni.devotion.multidataparser.model.Files.FileTypes
import ni.devotion.multidataparser.model.Files.Files
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ThreadFactory

@Suppress("DEPRECATION")
class FileManager(private val context: Context) {
    private var isNoteEnabled = false
    private var channelId = ""
    lateinit var onDownloadResultListener: OnDownloadResult
    lateinit var notificationManager: NotificationManager

    fun downloadFileFromURL(fileModel: Files, channelId: String, onDownloadResultListener: OnDownloadResult): String? {
        this.channelId = channelId
        isNoteEnabled = true
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return downloadFileFromURL(fileModel, onDownloadResultListener)
    }

    fun downloadFileFromURL(fileModel: Files, onDownloadResultListener: OnDownloadResult): String? {
        this.onDownloadResultListener = onDownloadResultListener
        val url = URL(fileModel.fileUrl)
        val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
        val responseCode = urlConnection.responseCode
        if(responseCode < 200 || responseCode >= 300){
            onDownloadResultListener.onError(context.getString(R.string.network_error), responseCode)
            return ""
        }
        val inputStream = BufferedInputStream(urlConnection.inputStream)
        return saveFile(inputStream, fileModel)
    }

    fun saveFile(file: File, fileModel: Files, onDownloadResultListener: OnDownloadResult): String{
        val filetosave = File(context.getExternalFilesDir(null)?.path, fileModel.fileName)
        filetosave.parentFile?.createNewFile()
        filetosave.createNewFile()
        val inStream = file.inputStream()
        inStream.use {
            FileOutputStream(filetosave).use { output ->
                val buffer = ByteArray(4 * 1024)
                var read: Int
                while (true) {
                    read = it.read(buffer)
                    if(read == -1) break
                    output.write(buffer, 0, read)
                }
                output.flush()
                onDownloadResultListener.onSuccess(filetosave.path)
            }
        }
        return file.absolutePath
    }

    fun saveFile(inputStream: BufferedInputStream, fileModel: Files): String{
        val ext: String = when (fileModel.fileType) {
            FileTypes.TYPE_PDF -> "pdf"
            FileTypes.TYPE_DOC -> "docx"
            FileTypes.TYPE_IMAGE_PNG -> "png"
            FileTypes.TYPE_IMAGE_JPG -> "jpg"
            FileTypes.TYPE_VIDEO -> "mp4"
            FileTypes.TYPE_AUDIO -> "mp3"
            FileTypes.TYPE_ZIP -> "zip"
            FileTypes.TYPE_RAR -> "rar"
        }
        val fileLocation = File(context.getExternalFilesDir(null)?.path, "${fileModel.fileName}.$ext")
        fileLocation.parentFile?.createNewFile()
        fileLocation.createNewFile()
        if(isNoteEnabled) notificationManager.notify(0, buildNote().build())
        inputStream.use { bufferInputStream ->
            FileOutputStream(fileLocation).use { outputStreamFile ->
                val buffer = ByteArray(4 * 1024)
                var read: Int
                while (true) {
                    read = bufferInputStream.read(buffer)
                    if(read == -1) break
                    outputStreamFile.write(buffer, 0, read)
                }
                outputStreamFile.flush()
                onDownloadResultListener.onSuccess(fileLocation.path)
                if(isNoteEnabled) notificationManager.cancel(0)
            }
        }
        return fileLocation.absolutePath
    }


    fun buildNote(): Builder{
        val builder = if(VERSION.SDK_INT >= VERSION_CODES.O) Builder(context, channelId) else Builder(context)
        builder.setContentTitle(context.getString(R.string.downloading))
        builder.setAutoCancel(false)
        builder.setSmallIcon(android.R.drawable.stat_sys_download)
        builder.setOngoing(true)
        return builder
    }

    internal class FileThreadFactory : ThreadFactory {
        override fun newThread(runnable: Runnable): Thread {
            return Thread(runnable).apply {
                name = "FileManager Thread"
                priority = Process.THREAD_PRIORITY_BACKGROUND
            }
        }
    }
}