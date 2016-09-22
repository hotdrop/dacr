package dacr.indata

/**
 * レコード情報のうち、1つのカラム属性情報を保持するデータクラス
 */
data class ColAttribute(

        /** カラム名 */
        val name : String,

        /**
         * PKか否か
         * PKの場合はvalueTypeがvariableでなければならない
         * 値を生成するときは一意になるよう制御する。
         * 従って特に指定がない場合は連番で生成する。
         **/
        val primaryKey : Boolean,

        /** カラムのデータ型 */
        val dataType : String,

        /**
         * カラムサイズ
         * 次の型で有効「char varchar number」
         * date型とtimestamp型も必要かもしれないが一旦はなしでいく
         **/
        val size : Int,
        /**
         * フォーマット
         * 次の型で有効「char varchar date timestamp」
         * ただしcharとvarcharはzero paddingするか否かのみの指定となりそれ以外は無視する。
         * たまにchar型に日付を設定しているが、そのvariable指定対応はしない。
         **/
        val format : String,

        /**
         * 自動発番するか。
         * 次の型で有効「char varchar number」
         **/
        val autoIncrements : Boolean,
        /**
         * サイズの限界まで値を入れるか。
         * 次の型で有効「char varchar」
         **/
        val fillMaxSize : Boolean,

        /**
         * 固定/可変
         **/
        val valueType : String,
        /**
         * 値の指定。
         * valueTypeが固定のみ有効
         * 全ての型で有効だが、PK指定している場合は無効にする
         **/
        val value : String,
        /**
         * マルチバイト文字でデータ作成するか。
         * 次の型で有効「char varchar」
         * かつvalueTypeが可変のみ有効。
         **/
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