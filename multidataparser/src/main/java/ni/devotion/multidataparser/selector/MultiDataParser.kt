package ni.devotion.multidataparser.selector

import android.content.Context
import ni.devotion.multidataparser.file.FileLoader
import ni.devotion.multidataparser.image.ImageLoader

class MultiDataParser {
    fun obtainFile(context: Context) = FileLoader(context).getInstance()
    fun obtainImage(context: Context) = ImageLoader(context).getInstance()
}