package dacr.indata

/**
 * レコード情報のうち、1つのカラムを保持するデータクラス
 * レコード情報は何らかの形式から読み込む。今の所Jsonだけ。
 */
data class ColumnAttributeData (

        /** カラム名 */
        val name : String,
        /** PKか否か */
        val primaryKey : Boolean,
        /** カラムのデータ型 */
        val dataType : String,
        /** カラムサイズ */
        val size : Int,
        /** 固定/可変 */
        val valueType : String,
        /**
         * 値指定
         * 取りうる値が固定の場合はその値を指定する
         * 複数の場合はリストで列挙する（jsonに従う）
         * AUTOINCREMENTS の場合はこの単語自体を指定する
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