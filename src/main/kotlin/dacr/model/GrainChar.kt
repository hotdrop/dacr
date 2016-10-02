package dacr.model

import dacr.indata.ColAttribute
import java.util.*

/**
 * char型とvarchar型カラムのGrainクラス
 * 最初はcharとvarchar分けていたが、データ生成は同じロジックで良さそう
 * なので一旦まとめる。後で不都合が出てきたら分割する
 */
class GrainChar(attr: ColAttribute): IGrain {

    override val name: String
    override val primaryKey: Boolean
    override val autoIncrement: Boolean
    override val isFixingValue: Boolean

    private val value: String
    private val values: List<String>?
    private var valueIdx = 0
    private var sequence = 1
    private val size: Int
    private val isZeroPadding: Boolean
    private val fillMaxSize: Boolean
    private val hasMultiByte: Boolean

    init {
        name = attr.name
        primaryKey = attr.primaryKey
        size = attr.size

        isFixingValue = if(attr.valueType.toUpperCase() == ColAttribute.VALUE_TYPE_FIXING) true else false
        autoIncrement = attr.autoIncrement

        value = attr.value
        // valueが空の場合を除外している理由はsequenceを初期値のままにするため。
        if(autoIncrement && value != "") {
            sequence = try { value.toInt() } catch (e : NumberFormatException) { 1 }
        }

        values = if(value.contains(",")) value.split(",") else null

        fillMaxSize = attr.fillMaxSize
        isZeroPadding = if(attr.format.toUpperCase() == ColAttribute.FORMAT_ZERO_PADDING && !fillMaxSize) true else false
        hasMultiByte = attr.hasMultiByte
    }

    /**
     * charの値を生成する
     */
    override fun create(): String {

        if(isFixingValue) {
            return makeFixingValue()
        }

        if(autoIncrement) {
            return makeAutoIncrement()
        }

        return makeVariableValue()
    }

    private fun makeFixingValue() : String {

        if(values == null) {
            return value
        }

        val retVal = values[valueIdx]
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

        var retVal = if(hasMultiByte) makeMultiByteString() else makeSingleByteString()
        return if(isZeroPadding) retVal.padStart(size, '0') else retVal
    }

    private companion object {
        // もっと増やしてもいいかもしれないが、ランダム生成のコストが大きくなることを恐れて一旦この数にした
        private val MULTI_BYTE_WORDS = arrayOf("あ","い","う","え","お")
        private val WORDS = arrayOf("A","B","C","D","E","F","G","H","I","J","K","L","M","N"
                ,"O","P","Q","R","S","T","U","V","W","X","Y","Z"
                ,"1","2","3","4","5","6","7","8","9")
    }
}