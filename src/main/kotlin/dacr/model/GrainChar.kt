package dacr.model

import dacr.indata.ColAttribute
import java.util.*

/**
 * Grain Class of Char type column
 */
class GrainChar(attr: ColAttribute): IGrain {

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

    override fun isPrimaryKey() =  isPrimaryKey

    override fun isAutoIncrement() = isAutoIncrement

    override fun isFixingValue() = isFixingValue

    override fun create(): String {

        fun encloseStr(ret: String) =
                if(encloseMark == "") ret else encloseMark + ret + encloseMark

        val retVal = when {
            isFixingValue -> makeFixingValue()
            isAutoIncrement -> makeAutoIncrement()
            else -> makeVariableValue()
        }

        return encloseStr(retVal.padEnd(size, ' '))
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
            // When Japanese characters are UTF 8, they become variable bytes.
            // In this case, it was set to 1/3 so that Oracle can also insert data.
            // If "fillMaxSize" is specified, insert error is assumed
            val makeSize = if(fillMaxSize) size else if (size >= 6) size/3 else 1
            return buildString {
                for(idx in 1..makeSize) {
                    append(MULTI_BYTE_WORDS[rand.nextInt(MULTI_BYTE_WORDS.size)])
                }
            }
        }

        fun makeSingleByteString(): String {

            val rand =Random()
            // It is not necessary to set it to 1/3, because alphanumeric characters are 1 byte.
            // I thought it better to compare with makeMultiByteString.
            val makeSize = if(fillMaxSize) size else if (size >= 6) size/3 else 1
            return buildString {
                for(idx in 1..makeSize) {
                    append(WORDS[rand.nextInt(WORDS.size)])
                }
            }
        }
        
        if(values != null) {
            return values[Random().nextInt(values.size)]
        }

        val retVal = if(hasMultiByte) makeMultiByteString() else makeSingleByteString()
        return if(isZeroPadding) retVal.padStart(size, '0') else retVal
    }

    companion object {
        // it even more
        val MULTI_BYTE_WORDS = arrayOf("あ","い","う","え","お")
        val WORDS = arrayOf("A","B","C","D","E","F","G","H","I","J","K","L","M","N"
                ,"O","P","Q","R","S","T","U","V","W","X","Y","Z"
                ,"1","2","3","4","5","6","7","8","9")
    }
}