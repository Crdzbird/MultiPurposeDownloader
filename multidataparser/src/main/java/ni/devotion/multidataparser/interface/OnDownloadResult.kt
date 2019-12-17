package ni.devotion.multidataparser.`interface`

interface OnDownloadResult {
    fun onSuccess(filePath: String)
    fun onError(error:String = "unknown error", errorCode:Int = 0)
}