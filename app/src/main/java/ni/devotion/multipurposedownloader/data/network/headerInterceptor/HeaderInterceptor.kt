package ni.devotion.multipurposedownloader.data.network.headerInterceptor

import ni.devotion.multipurposedownloader.preferences.AppPreferences
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    companion object ApiConstants{
        const val INFORMATION_URL = "/raw/wgkJgazE"
    }

    private fun setupCookies(){
        val cookies = AppPreferences().get(AppPreferences.Key.cookies, HashSet<String>()) as HashSet<*>
        var content = ""
        cookies.forEach { content += it as String }
        requestBuilder.addHeader("Cookie", content)
    }

    private lateinit var requestBuilder: Request.Builder

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        requestBuilder = request.newBuilder()
        when (request.url().url().path) {
            INFORMATION_URL -> {
                requestBuilder.addHeader("Content-Type", "application/json")
                setupCookies()
            }
            else -> {
                requestBuilder.addHeader("Content-Type", "application/json")
            }
        }
        request = requestBuilder.build()
        return chain.proceed(request)
    }
}