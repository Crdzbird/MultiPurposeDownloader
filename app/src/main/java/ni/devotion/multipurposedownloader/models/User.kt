package ni.devotion.multipurposedownloader.models

import java.io.Serializable

data class User(
    val id: String = "", // IR6cROTdd08
    val links: LinksXX = LinksXX(),
    val name: String = "", // Johan Mouchet
    val profile_image: ProfileImage = ProfileImage(),
    val username: String = "" // johanmouchet
): Serializable