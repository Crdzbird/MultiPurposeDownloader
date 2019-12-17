package ni.devotion.multidataparser.utils

import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager
import kotlin.math.pow

fun Context.isOnline(): Boolean {
    val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}


val Int.dp
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.px
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()


val Double.byte
    get()=this* 1024.0.pow(2.0)
