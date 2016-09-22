package dacr.indata

/**
 * レコード情報のうち、1つのカラム属性情報を保持するデータクラス
 */
data class ColAttribute(

        /** カラム名 */
        val name : String,
        /** PKか */
        val primaryKey : Boolean,

        /** カラムのデータ型 */
        val dataType : String,
        /** カラムサイズ。次の型で有効「char varchar number」 */
        val size : Int,
        /** フォーマット。次の型で有効「char varchar date timestamp」 */
        val format : String,

        /** 自動発番するか。次の型で有効「char varchar number」 */
        val autoIncrements : Boolean,
        /** サイズの限界まで値を入れるか。次の型で有効「char varchar」 */
        val fillMaxSize : Boolean,

        /** 固定/可変 */
        val valueType : String,
        /** 値の指定。valueTypeが固定のみ有効 */
        val value : String,
        /** マルチバイト文字でデータ作成するか。valueTypeが可変のみ有効 */
        val hasMultiByte : Boolean,

        /**
         * 他のコード体系か。valueTypeが可変のみ有効
         * TODO 実現方式未検討
         **/
        val otherCodeSystem : String

) {
    companion object {
        const val DATA_TYPE_CHAR = "char"
        const val DATA_TYPE_VARCHAR = "varchar"
        const val DATA_TYPE_NUMBER = "number"
        const val DATA_TYPE_DATE = "date"
        const val DATA_TYPE_TIMESTAMP = "timestamp"

        const val VALUE_TYPE_FIXING = "fixing"
        const val VALUE_TYPE_VARIABLE = "variable"

        const val FORMAT_ZERO_PADDING = "zeroPadding"
    }
}