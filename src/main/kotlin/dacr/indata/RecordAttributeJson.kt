package dacr.indata

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

/**
 * Json形式のファイルからレコード定義情報を取得する。
 * レコード情報はカラム単位で分割する
 *
 * @using google-gson Copyright 2008 Google Inc.
 * @license http://www.apache.org/licenses/LICENSE-2.0 Apache-2.0
 */
class RecordAttributeJson(filePath : String) : IRecord {

    private val filePath = filePath

    inline fun <reified T> genericType() = object: TypeToken<T>() {}.type!!

    override fun parse() : List<ColAttribute> {
        val source = File(filePath).readText(Charsets.UTF_8)
        return Gson().fromJson<List<ColAttribute>>(source, genericType<List<ColAttribute>>())
    }
}
