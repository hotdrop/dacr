package dacr.indata

/**
 * Column Attribute Data Class
 * Store one column attribute data
 */
data class ColAttribute(

        val name: String = "",
        val primaryKey: Boolean = false,
        val dataType: String = "",
        val size: Int = 0,
        val format: String = "",
        val valueType: String = "",
        val value: String = "",
        val autoIncrement: Boolean = false,
        val fillMaxSize: Boolean = false,
        val hasMultiByte: Boolean = false,
        val encloseChar: String = ""
) {
    companion object {
        const val DATA_TYPE_CHAR = "CHAR"
        const val DATA_TYPE_VARCHAR = "VARCHAR"
        const val DATA_TYPE_VARCHAR2 = "VARCHAR2"
        const val DATA_TYPE_INT = "INT"
        const val DATA_TYPE_INTEGER = "INTEGER"
        const val DATA_TYPE_DATE = "DATE"
        const val DATA_TYPE_DATETIME = "DATETIME"
        const val DATA_TYPE_TIMESTAMP = "TIMESTAMP"

        const val VALUE_TYPE_FIXING = "FIXING"
        const val VALUE_TYPE_VARIABLE = "VARIABLE"

        const val VALUE_NOW = "NOW"
        const val FORMAT_ZERO_PADDING = "ZEROPADDING"
        const val ENCLOSE_CHAR_SINGLE_QUOTATION = "SINGLEQUOTATION"
        const val ENCLOSE_CHAR_DOUBLE_QUOTATION = "DOUBLEQUOTATION"
    }
}