package dacr.model

import dacr.indata.ColAttribute
import java.util.*

/**
 * Grain Class of Varchar type column
 */
class GrainVarchar(attr: ColAttribute): IGrain {

    private val name = attr.name
    private val isPrimaryKey = attr.primaryKey
    private val isAutoIncrement = attr.autoIncrement
    private val isFixingValue = (attr.valueType.toUpperCase() == ColAttribute.VALUE_TYPE_FIXING)

    private val value = attr.value

    private val size = attr.size
    private val fillMaxSize = attr.fillMaxSize
    private val hasMultiByte = attr.hasMultiByte

    private val values: List<String>?
    private val isZeroPadding: Boolean
    private val encloseMark: String

    private var valueIdx = 0
    private var sequence = 1

    init {

        values = if(value.contains(",")) value.split(",") else null

        if(isAutoIncrement) {
            sequence = try { value.toInt() } catch (e : NumberFormatException) { 1 }
        }

        isZeroPadding = if(attr.format != "") {
            when(attr.format.toUpperCase() ) {
                ColAttribute.FORMAT_ZERO_PADDING -> true
                else -> throw IllegalStateException("incorrect format by char. " +
                        " columnName=" + name + " format=" + attr.format)
            }
        } else {
            false
        }

        encloseMark = if(attr.encloseChar != "") {
            when(attr.encloseChar.toUpperCase()) {
                ColAttribute.ENCLOSE_CHAR_SINGLE_QUOTATION -> "'"
                ColAttribute.ENCLOSE_CHAR_DOUBLE_QUOTATION -> """""""
                else -> throw IllegalStateException("incorrect encloseChar by char. " +
                        " columnName=" + name + " encloseChar=" + attr.encloseChar)
            }
        } else {
            ""
        }
    }

    override fun isPrimaryKey() = isPrimaryKey

    override fun isAutoIncrement() = isAutoIncrement

    override fun isFixingValue() = isFixingValue

    override fun create(): String {

        fun encloseStr(ret: String): String {
            return if(encloseMark == "") ret else encloseMark + ret + encloseMark
        }

        val retVal = when {
            isFixingValue -> makeFixingValue()
            isAutoIncrement -> makeAutoIncrement()
            else -> makeVariableValue()
        }

        return encloseStr(retVal)
    }

    private fun makeFixingValue(): String {

        if(values == null) {
            return value
        }

        val retVal = values[valueIdx]
        valueIdx = if(valueIdx >= values.size - 1) 0 else ++valueIdx

        return retVal
    }

    private fun makeAutoIncrement(): String {
        val retVal = sequence.toString()
        sequence++
        return if(isZeroPadding) retVal.padStart(size, '0') else retVal
    }

    private fun makeVariableValue(): String {

        fun makeMultiByteString(): String {

            val rand = Random()
            // 日本語文字はUTF8だと1文字3バイトになるため、例えばデータ長をバイトで計算するOracleとかでも
            // 入れられるよう1/3とした。fillMaxSizeを指定してしまった場合は仕方ないのでinsertエラーになってもらう。
            val makeSize = if(fillMaxSize) size else if (size >= 6) size/3 else 1
            return buildString {
                for(idx in 1..makeSize) {
                    append(GrainChar.MULTI_BYTE_WORDS[rand.nextInt(GrainChar.MULTI_BYTE_WORDS.size)])
                }
            }
        }

        fun makeSingleByteString(): String {

            val rand = Random()
            val makeSize = if(fillMaxSize) size else if (size >= 6) size/3 else 1
            return buildString {
                for(idx in 1..makeSize) {
                    append(GrainChar.WORDS[rand.nextInt(GrainChar.WORDS.size)])
                }
            }
        }

        if(values != null) {
            return values[Random().nextInt(values.size)]
        }

        val retVal = if(hasMultiByte) makeMultiByteString() else makeSingleByteString()
        return if(isZeroPadding) retVal.padStart(size, '0') else retVal
    }
}