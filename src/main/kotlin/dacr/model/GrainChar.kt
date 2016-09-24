package dacr.model

import dacr.indata.ColAttribute
import java.util.*

/**
 * char型とvarchar型カラムのGrainクラス
 * 最初はcharとvarchar分けていたが、データ生成は同じロジックで良さそう
 * なので一旦まとめる。後で不都合が出てきたら分割する
 */
class GrainChar(attr: ColAttribute) : IGrain {

    val name : String
    val primaryKey : Boolean

    private val value : String
    private val multiValues : List<String>?

    private var sequence : Int = 1

    private val size : Int

    private val isFixingValue: Boolean
    private val isZeroPadding : Boolean
    private val fillMaxSize : Boolean
    private val autoIncrement : Boolean
    private val hasMultiByte : Boolean

    init {
        name = attr.name
        primaryKey = attr.primaryKey
        size = attr.size

        isFixingValue = if(attr.valueType == ColAttribute.VALUE_TYPE_FIXING) true else false
        fillMaxSize = attr.fillMaxSize
        isZeroPadding = if(attr.format == ColAttribute.FORMAT_ZERO_PADDING && !fillMaxSize) true else false
        autoIncrement = attr.autoIncrement
        hasMultiByte = attr.hasMultiByte

        value = attr.value
        // 空の場合は初期値0を設定するため除外している。
        if(autoIncrement && value != "") {
            sequence = try { value.toInt() } catch (e : NumberFormatException) { 1 }
        }

        multiValues = if(value.contains(",")) value.split(",") else null
    }

    /**
     * charの値を生成する
     * 値が固定の場合：無条件にvalueを返す
     * 値が可変＋autoIncrement：1からの連番
     */
    override fun create() : String {

        if(isFixingValue) {
            return value
        }

        if(multiValues != null) {
            val rand =Random()
            return multiValues[rand.nextInt(multiValues.size)]
        }

        var retVal : String

        if(autoIncrement) {
            retVal = sequence.toString()
            sequence++
        } else {
            retVal = if(hasMultiByte) makeMultiByteString() else makeSingleByteString()
        }

        if(isZeroPadding) {
            retVal = retVal.padStart(size, '0')
        }

        return retVal
    }

    private fun makeSingleByteString() : String {

        val rand =Random()
        val makeSize = if(fillMaxSize) size else if (size >= 6) size/3 else 1
        var buff = ""

        for(idx in 1..makeSize) {
            buff += WORDS[rand.nextInt(WORDS.size)]
        }
        return buff
    }

    private fun makeMultiByteString() :String {

        val rand =Random()
        val makeSize = if(fillMaxSize) size else if (size >= 6) size/3 else 1
        var buff = ""

        for(idx in 1..makeSize) {
            buff += MULTI_BYTE_WORDS[rand.nextInt(MULTI_BYTE_WORDS.size)]
        }
        return buff
    }

    private companion object {
        private val MULTI_BYTE_WORDS = arrayOf("あ","い","う","え","お")
        private val WORDS = arrayOf("A","B","C","D","E","1","2","3","4","5")
    }
}