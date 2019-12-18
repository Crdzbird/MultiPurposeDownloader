package ni.devotion.multipurposedownloader.base

import android.app.Application
import ni.devotion.multipurposedownloader.BuildConfig
import ni.devotion.multipurposedownloader.di.allAppModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class BaseApplication : Application() {
    companion object{
        lateinit var context: BaseApplication
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        startKoin {
            androidLogger()
            androidContext(this@BaseApplication)
            modules(allAppModules)
        }
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}