package ni.devotion.multidataparser.file

import android.content.Context
import android.util.Log
import android.util.LruCache
import ni.devotion.multidataparser.`interface`.OnDownloadResult
import ni.devotion.multidataparser.model.Files.FileTypes
import ni.devotion.multidataparser.model.Files.Files
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FileLoader internal constructor(private val context: Context)  {
    companion object {
        private lateinit var INSTANCE: FileLoader
    }

    private val tag = "ttt FileLoader"
    private lateinit var fileType: FileTypes
    private var fileName = ""
    private var isCacheEnabled = false
    private var isNotificationEnabled = false
    var notificationChannelId = ""
    lateinit var onDownloadResultListener: OnDownloadResult
    private val executorService: ExecutorService
    private var maxCacheSize: Int = (Runtime.getRuntime().maxMemory() / 1024).toInt()/8
    private val memoryCache: LruCache<String, File>
    private var fileLoaderUtility: FileManager = FileManager(context)

    init {
        memoryCache = object : LruCache<String, File>(maxCacheSize) {
            override fun sizeOf(key: String, file: File): Int {
                return (file.length() / 1024).toInt()
            }
        }
        executorService = Executors.newFixedThreadPool(5, FileManager.FileThreadFactory())
    }

    @Synchronized
    internal fun getInstance(): FileLoader{
        INSTANCE = FileLoader(context)
        return INSTANCE
    }

    fun setCacheSize(cacheSizeInMB: Int): FileLoader {
        when (val cacheSize = cacheSizeInMB * 1024 * 1024) {
            in 1 until maxCacheSize -> { maxCacheSize = cacheSize }
        }
        return INSTANCE
    }

    fun setFileName(fileName: String): FileLoader {
        this.fileName = fileName
        return INSTANCE
    }

    fun setCacheEnabled(isCacheEnabled: Boolean): FileLoader {
        this.isCacheEnabled = isCacheEnabled
        return INSTANCE
    }

    fun setNotificationEnabled(isNotificationEnabled: Boolean, channelId: String): FileLoader {
        require(channelId.isNotEmpty()){ throw Exception("FileLoader:setNotificationEnabled - channelId should not be empty") }
        this.isNotificationEnabled = isNotificationEnabled
        this.notificationChannelId = channelId
        return INSTANCE
    }

    fun download(fileUrl: String, fileType: FileTypes) {
        this.fileType = fileType
        Log.d(tag, "download")
        require(fileUrl.isNotEmpty()) { throw Exception("FileLoader:load - Url should not be empty") }
        if(fileName.isEmpty()) fileName = System.currentTimeMillis().toString()
        val file = checkFileInCache(fileUrl)
        file?.let {
            fileLoaderUtility.saveFile(it,
                Files(
                    fileUrl,
                    fileType,
                    fileName
                ), onDownloadResultListener)
        } ?: run {
            executorService.submit(FileLoadingThread(
                Files(
                    fileUrl,
                    fileType,
                    fileName
                )
            ))
        }
    }

    @Synchronized
    private fun checkFileInCache(fileUrl: String): File? = memoryCache.get(fileUrl)
        internal inner class FileLoadingThread(private var fileModel: Files) : Runnable {
            override fun run() {
                Log.d(tag, "FileLoadingThread")
                val filepath = if(isNotificationEnabled) fileLoaderUtility.downloadFileFromURL(fileModel, notificationChannelId, onDownloadResultListener) else fileLoaderUtility.downloadFileFromURL(fileModel, onDownloadResultListener)
                println(filepath)
                if(!isCacheEnabled) return
                memoryCache.put(fileModel.fileUrl, File(filepath!!))
            }
    }

    fun setOnDownloadResultListener(onDownloadResultListener: OnDownloadResult): FileLoader{
        this.onDownloadResultListener = onDownloadResultListener
        return INSTANCE
    }
}