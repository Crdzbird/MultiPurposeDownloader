package ni.devotion.multidataparser.image

import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.util.Log
import android.util.LruCache
import android.widget.ImageView
import ni.devotion.multidataparser.R
import ni.devotion.multidataparser.model.Images.Images
import java.util.*
import java.util.Collections.synchronizedMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ImageLoader internal constructor(private val context: Context)  {
    private val tag = "ttt ImageLoader"
    private val imageViewMap = synchronizedMap(WeakHashMap<ImageView, String>())
    private val handler: Handler
    private var placeholderResId = R.drawable.ic_image_holder
    private var isCacheEnabled = false
    private val executorService: ExecutorService
    private val memoryCache: LruCache<String, Bitmap>
    private var maxCacheSize: Int = (Runtime.getRuntime().maxMemory() / 1024).toInt()/8

    init {
        memoryCache = object : LruCache<String, Bitmap>(maxCacheSize) {
            override fun sizeOf(key: String, bitmap: Bitmap) = bitmap.byteCount / 1024
        }
        executorService = Executors.newFixedThreadPool(5, ImageManager.ImageThreadFactory())
        handler = Handler()
        val metrics = context.resources.displayMetrics
        screenWidth = metrics.widthPixels
        screenHeight = metrics.heightPixels
    }

    companion object {
        private lateinit var INSTANCE: ImageLoader
        internal var screenWidth = 0
        internal var screenHeight = 0
    }

    @Synchronized
    internal fun getInstance(): ImageLoader{
        INSTANCE = ImageLoader(context)
        return INSTANCE
    }

    fun load(imageView: ImageView, imageUrl: String) {
        require(imageUrl.isNotEmpty()) { "ImageLoader:load - Image Url should not be empty" }
        imageView.setImageResource(placeholderResId)
        imageViewMap[imageView] = imageUrl
        val bitmap = checkImageInCache(imageUrl)
        bitmap ?: executorService.submit(ImageLoadingThread(Images(imageUrl, imageView)))
        bitmap?.let {
            Log.d(tag, "cached")
            loadImageIntoImageView(imageView, it, imageUrl)
        }
    }

    fun setCacheSize(cacheSizeInMB: Int): ImageLoader {
        val cacheSize = cacheSizeInMB * 1024 * 1024
        if(cacheSize in 1 until maxCacheSize){ maxCacheSize = cacheSize }
        return INSTANCE
    }

    fun setCacheEnabled(isCacheEnabled: Boolean): ImageLoader {
        this.isCacheEnabled = isCacheEnabled
        return INSTANCE
    }

    fun placeholder(placeholderResId: Int): ImageLoader {
        require(placeholderResId != 0) { "ImageLoader:placeholder - placeholder should not be null." }
        this.placeholderResId = placeholderResId
        return INSTANCE
    }

    @Synchronized
    private fun loadImageIntoImageView(imageView: ImageView, bitmap: Bitmap?, imageUrl: String) {
        bitmap ?: throw Exception("ImageLoader:loadImageIntoImageView - Bitmap should not be null")
        bitmap.let { btMap ->
            val scaledBitmap = ImageManager.scaleBitmapForLoad(btMap, imageView.width, imageView.height)
            scaledBitmap?.let {scaledMap ->
                if(!isImageViewReused(Images(imageUrl, imageView))) imageView.setImageBitmap(scaledMap)
            }
        }
    }

    private fun isImageViewReused(Images: Images): Boolean {
        val tag = imageViewMap[Images.imageView]
        return tag == null || tag != Images.imgUrl
    }

    @Synchronized
    private fun checkImageInCache(imageUrl: String): Bitmap? = memoryCache.get(imageUrl)

    internal inner class DisplayBitmap(private var imageRequest: Images) : Runnable {
        override fun run() {
            if(!isImageViewReused(imageRequest)) loadImageIntoImageView(imageRequest.imageView, checkImageInCache(imageRequest.imgUrl), imageRequest.imgUrl)
        }
    }

    internal inner class ImageLoadingThread(private var Images: Images) : Runnable {
        override fun run() {
            if(isImageViewReused(Images)) return
            val bitmap = ImageManager.downloadBitmapFromURL(Images.imgUrl)
            memoryCache.put(Images.imgUrl, bitmap)
            if(isImageViewReused(Images)) return
            val displayBitmap = DisplayBitmap(Images)
            handler.post(displayBitmap)
        }
    }
}