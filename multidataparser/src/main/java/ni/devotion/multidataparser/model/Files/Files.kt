package ni.devotion.multidataparser.model.Files

data class Files(var fileUrl: String, var fileType: FileTypes, var fileName: String)
enum class FileTypes { TYPE_IMAGE, TYPE_DOC, TYPE_PDF, TYPE_AUDIO, TYPE_VIDEO }