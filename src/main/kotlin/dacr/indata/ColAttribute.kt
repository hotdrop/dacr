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
         * zero paddingは単品（valueがカンマ区切りの複数要素でない）かautoIncrementでのみ有効
         * たまにchar型に日付を設定しているが、そのvariable指定対応はしない。
         **/
        val format : String,

        /**
         * 自動発番するか。
         * 次の型で有効「char varchar number」
         **/
        val autoIncrement : Boolean,
        /**
         * サイズの限界まで値を入れるか。
         * 次の型で有効「char varchar」
         * CharsetがUTF8の場合、ものによっては１文字３バイトになる可能性があるのでこの
         * 指定をする場合は注意すること。なお、これをfalseにするとsizeの1/3の長さで文字を生成する
         **/
        val fillMaxSize : Boolean,

        /**
         * 固定/可変
         **/
        val valueType : String,

        /**
         * 値を指定する。
         * 全ての型で有効。
         *
         * fixingの場合:値を1つ指定するとその値固定で出力する。空の場合は空を入れる。
         *             値をカンマ区切りで複数指定すると、その値を順番に出力する。
         * variableの場合:値を指定しても無視してランダム値を出力する。
         *               値をカンマ区切りで複数指定すると、それらの値をランダムに出力する。
         *               Number型かつautoIncrement指定した場合、指定した値から順にインクリメントする。
         **/
        val value : String,

        /**
         * マルチバイト文字でデータ作成するか。
         * 次の型で有効「char varchar」
         * かつvalueTypeが可変のみ有効。
         *
         **/
        val hasMultiByte : Boolean
) {
    companion object {
        const val DATA_TYPE_CHAR = "char"
        const val DATA_TYPE_VARCHAR = "varchar"
        const val DATA_TYPE_NUMBER = "number"
        const val DATA_TYPE_DATE = "date"
        const val DATA_TYPE_DATETIME = "datetime"
        const val DATA_TYPE_TIMESTAMP = "timestamp"

        const val VALUE_TYPE_FIXING = "fixing"
        const val VALUE_TYPE_VARIABLE = "variable"

        const val VALUE_NOW = "now"

        const val FORMAT_ZERO_PADDING = "zeroPadding"
    }
}