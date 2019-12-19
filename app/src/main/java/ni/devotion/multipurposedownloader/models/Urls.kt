package ni.devotion.multipurposedownloader.models

import java.io.Serializable

data class Urls(
    val full: String = "", // https://images.unsplash.com/photo-1464519046765-f6d70b82a0df?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&s=8cfd7aa940eb30ae2d754704c1ba89b5
    val raw: String = "", // https://images.unsplash.com/photo-1464519046765-f6d70b82a0df
    val regular: String = "", // https://images.unsplash.com/photo-1464519046765-f6d70b82a0df?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&w=1080&fit=max&s=34a7fbfc57457c2423413a9534b7ecfd
    val small: String = "", // https://images.unsplash.com/photo-1464519046765-f6d70b82a0df?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&w=400&fit=max&s=b28e48601f9d332f5ba92c8d49cdbf83
    val thumb: String = "" // https://images.unsplash.com/photo-1464519046765-f6d70b82a0df?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&w=200&fit=max&s=5bae312df2a102d92a526b553f88de1e
): Serializable