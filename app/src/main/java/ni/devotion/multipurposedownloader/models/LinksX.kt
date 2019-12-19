package ni.devotion.multipurposedownloader.models

import java.io.Serializable

data class LinksX(
    val download: String = "", // http://unsplash.com/photos/sTBdWFQKDHE/download
    val html: String = "", // http://unsplash.com/photos/sTBdWFQKDHE
    val self: String = "" // https://api.unsplash.com/photos/sTBdWFQKDHE
): Serializable