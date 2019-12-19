package ni.devotion.multipurposedownloader.models

import java.io.Serializable

data class Links(
    val photos: String = "", // https://api.unsplash.com/categories/6/photos
    val self: String = "" // https://api.unsplash.com/categories/6
): Serializable