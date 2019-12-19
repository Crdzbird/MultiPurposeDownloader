package ni.devotion.multipurposedownloader.models

import java.io.Serializable

data class LinksXX(
    val html: String = "", // http://unsplash.com/@johanmouchet
    val likes: String = "", // https://api.unsplash.com/users/johanmouchet/likes
    val photos: String = "", // https://api.unsplash.com/users/johanmouchet/photos
    val self: String = "" // https://api.unsplash.com/users/johanmouchet
): Serializable