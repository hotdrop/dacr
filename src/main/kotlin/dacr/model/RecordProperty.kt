package dacr.model

import com.google.gson.Gson
import java.io.File

/**
 * レコード定義ファイルを保持するクラス
 */
data class RecordPropertyData (
        /** PKか否か */
        val primary_key : Boolean,
        /** カラムのデータ型 */
        val data_type : String,
        /** カラムサイズ */
        val size : Int,
        /** 固定/可変 */
        val value_type : String,
        /**
         * 値指定
         * 取りうる値が固定の場合はその値を指定する
         * 複数の場合はリストで列挙する（jsonに従う）
         * AUTOINCREMENTSの場合はこの単語自体を指定する
         **/
        val value : String,
        /**
         * フォーマット指定
         * 日付の場合はそのままフォーマット、文字の場合は0埋めするか等
         **/
        val format : String,
        /**
         * 他のコード体系か
         * 可変の場合のみ。別の何かを読み込んでその体系を設定する
         * TODO ここどう実現するか未検討
         **/
        val otherCodeSystem : String,
        /**
         * マルチバイト指定
         * 型が文字ならマルチバイト入れるか指定
         **/
        val hasMultiByte : Boolean,
        /**
         * 桁MAX
         * サイズの限界まで値を入れるか
         **/
        val fillMaxSize : Boolean
) {
    companion object {
        const val VALUE_TYPE_FIXING = "fixing"
        const val VALUE_TYPE_VARIABLE = "variable"
        const val FORMAT_ZERO_PADDING = "zeroPadding"
    }
}

class RecordProperty(filePath : String) {

    var filePath = filePath

    init {
        parseJson()
    }

    private fun parseJson() : RecordPropertyData {
        val source = File(filePath).readText(Charsets.UTF_8)
        return Gson().fromJson(source, RecordPropertyData::class.java)
    }
}
