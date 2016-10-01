package dacr.indata

/**
 * レコード情報のうち、1つのカラム属性情報を保持するデータクラス
 */
data class ColAttribute(

        val name : String,
        val primaryKey : Boolean,
        val dataType : String,
        /** 次の型で有効:char varchar number */
        val size : Int,
        /** 次の型で有効:char varchar date timestamp */
        val format : String,
        val valueType : String,
        /**
         * fixingの場合:値を1つ指定するとその値固定で出力する。空の場合は空を入れる。
         *             値をカンマ区切りで複数指定すると、その値を順番に出力する。
         * variableの場合:値を指定しても無視してランダム値を出力する。
         *               値をカンマ区切りで複数指定すると、それらの値をランダムに出力する。
         *               Number型かつautoIncrement指定した場合、指定した値から順にインクリメントする。
         * DateまたはTimestampの場合:nowと指定すると現在日時を出力する。
         **/
        val value : String,
        /** 次の型で有効:char varchar number */
        val autoIncrement : Boolean,
        /** 次の型で有効:char varchar */
        val fillMaxSize : Boolean,
        /** 次の型で有効:char varchar */
        val hasMultiByte : Boolean
) {
    companion object {
        const val DATA_TYPE_CHAR = "CHAR"
        const val DATA_TYPE_VARCHAR = "VARCHAR"
        const val DATA_TYPE_NUMBER = "NUMBER"
        const val DATA_TYPE_DATE = "DATE"
        const val DATA_TYPE_DATETIME = "DATETIME"
        const val DATA_TYPE_TIMESTAMP = "TIMESTAMP"
        const val VALUE_TYPE_FIXING = "FIXING"
        const val VALUE_TYPE_VARIABLE = "VARIABLE"
        const val VALUE_NOW = "NOW"
        const val FORMAT_ZERO_PADDING = "ZEROPADDING"
    }
}