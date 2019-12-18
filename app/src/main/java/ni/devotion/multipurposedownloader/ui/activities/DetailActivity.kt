package ni.devotion.multipurposedownloader.ui.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_detail.*
import ni.devotion.multidataparser.`interface`.OnDownloadResult
import ni.devotion.multidataparser.file.FileLoader
import ni.devotion.multidataparser.model.Files.FileTypes
import ni.devotion.multidataparser.selector.MultiDataParser
import ni.devotion.multipurposedownloader.R
import ni.devotion.multipurposedownloader.base.BaseApplication.Companion.context

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        btnDownload.setOnClickListener {
            if(!hasRequiredPermissions()) {
                askPermissions()
                return@setOnClickListener
            }
            MultiDataParser().obtainFile(context)
                .setFileName("HOLA-${System.currentTimeMillis()}")
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
                .download("https://images.unsplash.com/photo-1464550883968-cec281c19761?ixlib=rb-0.3.5\\u0026q=80\\u0026fm=jpg\\u0026crop=entropy\\u0026w=1080\\u0026fit=max\\u0026s=1881cd689e10e5dca28839e68678f432", FileTypes.TYPE_IMAGE_JPG)
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
