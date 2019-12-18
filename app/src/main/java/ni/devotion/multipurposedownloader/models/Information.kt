package ni.devotion.multipurposedownloader.models

data class Information(
    val categories: List<Category> = listOf(),
    val color: String = "", // #DCE0E4
    val created_at: String = "", // 2016-05-29T06:52:45-04:00
    val current_user_collections: List<Any> = listOf(),
    val height: Int = 0, // 4000
    val id: String = "", // sTBdWFQKDHE
    val liked_by_user: Boolean = false, // false
    val likes: Int = 0, // 3
    val links: LinksX = Links(),
    val urls: Urls = Urls(),
    val user: User = User(),
    val width: Int = 0 // 6000
)