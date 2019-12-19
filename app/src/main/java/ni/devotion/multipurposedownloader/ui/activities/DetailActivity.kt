package ni.devotion.multipurposedownloader.ui.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.navArgs
import kotlinx.android.synthetic.main.activity_detail.*
import ni.devotion.multidataparser.`interface`.OnDownloadResult
import ni.devotion.multidataparser.file.FileLoader
import ni.devotion.multidataparser.model.Files.FileTypes
import ni.devotion.multidataparser.selector.MultiDataParser
import ni.devotion.multipurposedownloader.R
import ni.devotion.multipurposedownloader.base.BaseApplication.Companion.context
import ni.devotion.multipurposedownloader.models.Information

class DetailActivity : AppCompatActivity() {

    private lateinit var information: Information
    private val detailArgs: DetailActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        information = detailArgs.information

        informationDetailImage?.let { MultiDataParser().obtainImage(context).load(it, information.urls.regular) }
        informationDetailCreationDate.text = information.created_at
        informationDetailUsername.text = information.user.name
        informationDetailLikes.text = context.getString(R.string.likes, information.likes)
        btnDownload.setOnClickListener {
            if(!hasRequiredPermissions()) {
                askPermissions()
                return@setOnClickListener
            }
            MultiDataParser().obtainFile(context)
                .setFileName("${information.id}-${System.currentTimeMillis()}")
                .setNotificationEnabled(true, getString(R.string.app_name))
                .setOnDownloadResultListener(object : OnDownloadResult {
                    override fun onSuccess(filePath: String) {
                        runOnUiThread{
                            Toast.makeText(context, String.format("File downloaded at: %s", filePath), Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onError(error: String, errorCode:Int) {
                        runOnUiThread{
                            Toast.makeText(context, String.format("File downloading error. ERROR_CODE_%s", errorCode), Toast.LENGTH_SHORT).show()
                        }
                    }
                })
                .download(information.urls.regular, FileTypes.TYPE_IMAGE_JPG)
        }
    }

    fun askPermissions(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }
    }

    fun hasRequiredPermissions(): Boolean{
        return ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }
}
