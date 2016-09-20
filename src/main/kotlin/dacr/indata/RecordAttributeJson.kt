package dacr.indata

import com.google.gson.Gson
import java.io.File

/**
 * Json形式のファイルからレコード定義情報を取得する
 */
class RecordAttributeJson(filePath : String) : IRecord {

    val filePath = filePath

    override fun parse() : RecordAttributeData {
        val source = File(filePath).readText(Charsets.UTF_8)
        return Gson().fromJson(source, RecordAttributeData::class.java)
    }
}
