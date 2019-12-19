package ni.devotion.multipurposedownloader.models

import java.io.Serializable

data class Category(
    val id: Int = 0, // 6
    val links: Links = Links(),
    val photo_count: Int = 0, // 15513
    val title: String = "" // People
): Serializable