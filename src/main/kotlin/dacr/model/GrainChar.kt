package dacr.model

import dacr.indata.ColAttribute
import java.util.*

/**
 * char型カラムのGrainクラス
 */
class GrainChar(attr: ColAttribute): IGrain {

    private val name: String
    private val primaryKey: Boolean
    private val autoIncrement: Boolean
    private val fixingValue: Boolean
    private val value: String
    private val values: List<String>?
    private val size: Int
    private val fillMaxSize: Boolean
    private val hasMultiByte: Boolean
    private val isZeroPadding: Boolean
    private val encloseMark: String

    private var valueIdx = 0
    private var sequence = 1

    init {
        name = attr.name
        primaryKey = attr.primaryKey
        size = attr.size

        value = attr.value
        values = if(value.contains(",")) value.split(",") else null

        autoIncrement = attr.autoIncrement
        fillMaxSize = attr.fillMaxSize
        hasMultiByte = attr.hasMultiByte
        fixingValue = if(attr.valueType.toUpperCase() == ColAttribute.VALUE_TYPE_FIXING) true else false

        if(autoIncrement) {
            sequence = try { value.toInt() } catch (e : NumberFormatException) { 1 }
        }

        if(attr.format != "") {
            when(attr.format.toUpperCase() ) {
                ColAttribute.FORMAT_ZERO_PADDING -> isZeroPadding = true
                else -> throw IllegalStateException("incorrect format by char. " +
                        " columnName=" + name + " format=" + attr.format)
            }
        } else {
            isZeroPadding = false
        }

        if(attr.encloseChar != "") {
            when(attr.encloseChar.toUpperCase()) {
                ColAttribute.ENCLOSE_CHAR_SINGLE_QUOTATION -> encloseMark = "'"
                ColAttribute.ENCLOSE_CHAR_DOUBLE_QUOTATION -> encloseMark = """""""
                else -> throw IllegalStateException("incorrect encloseChar by char. " +
                        " columnName=" + name + " encloseChar=" + attr.encloseChar)
            }
        } else {
            encloseMark = ""
        }
    }

    override fun isPrimaryKey(): Boolean {
        return primaryKey
    }

    override fun isAutoIncrement(): Boolean {
        return autoIncrement
    }

    override fun isFixingValue(): Boolean {
        return fixingValue
    }

    override fun create(): String {

        fun encloseStr(ret: String): String {
            return if(encloseMark == "") ret else encloseMark + ret + encloseMark
        }

        var retVal: String

        if(fixingValue) {
            retVal = makeFixingValue()
        } else if(autoIncrement) {
            retVal = makeAutoIncrement()
        } else {
            retVal = makeVariableValue()
        }

        return encloseStr(retVal.padEnd(size, ' '))
    }

    private fun makeFixingValue(): String {

        if(values == null) {
            return value
        }

        var retVal = values[valueIdx]
        valueIdx = if(valueIdx >= values.size - 1) 0 else ++valueIdx

        return retVal
    }

    private fun makeAutoIncrement(): String {
        var retVal = sequence.toString()
        sequence++
        return if(isZeroPadding) retVal.padStart(size, '0') else retVal
    }

    private fun makeVariableValue(): String {

        fun makeMultiByteString(): String {

            val rand =Random()
            // 日本語文字はUTF8だと1文字3バイトになるため、例えばデータ長をバイトで計算するOracleとかでも
            // 入れられるよう1/3とした。fillMaxSizeを指定してしまった場合は仕方ないのでinsertエラーになってもらう。
            val makeSize = if(fillMaxSize) size else if (size >= 6) size/3 else 1
            val sb = buildString {
                for(idx in 1..makeSize) {
                    append(MULTI_BYTE_WORDS[rand.nextInt(MULTI_BYTE_WORDS.size)])
                }
            }
            return sb
        }

        fun makeSingleByteString(): String {

            val rand =Random()
            // 英数字は１バイトなので、1/3にする明確な理由はない。単に1/3くらいなら程よい性能だろうと言うことと
            // makeMultiByteStringと合わせた方が分かりやすいかと思って同じ仕様にした。
            val makeSize = if(fillMaxSize) size else if (size >= 6) size/3 else 1
            val sb = buildString {
                for(idx in 1..makeSize) {
                    append(WORDS[rand.nextInt(WORDS.size)])
                }
            }
            return sb
        }
        
        if(values != null) {
            return values[Random().nextInt(values.size)]
        }

        val retVal = if(hasMultiByte) makeMultiByteString() else makeSingleByteString()
        return if(isZeroPadding) retVal.padStart(size, '0') else retVal
    }
    companion object {
        // もっと増やしてもいいかもしれないが、ランダム生成のコストが大きくなることを恐れて一旦この数にした
        val MULTI_BYTE_WORDS = arrayOf("あ","い","う","え","お")
        val WORDS = arrayOf("A","B","C","D","E","F","G","H","I","J","K","L","M","N"
                ,"O","P","Q","R","S","T","U","V","W","X","Y","Z"
                ,"1","2","3","4","5","6","7","8","9")
    }
}