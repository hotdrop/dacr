package dacr.model

import dacr.indata.ColAttribute
import java.util.*

/**
 * char型カラムのGrainクラス
 */
class GrainChar(attr: ColAttribute) : IGrain {

    val name : String
    val primaryKey : Boolean

    val value : String
    val multiValues : List<String>?

    var sequence : Int = 0

    val size : Int

    val isFixingValue: Boolean
    val isZeroPadding : Boolean
    val fillMaxSize : Boolean
    val autoIncrement : Boolean
    val hasMultiByte : Boolean

    init {
        name = attr.name
        primaryKey = attr.primaryKey
        size = attr.size

        isFixingValue = if(attr.valueType == ColAttribute.VALUE_TYPE_FIXING) true else false
        isZeroPadding = if(attr.format == ColAttribute.FORMAT_ZERO_PADDING) true else false
        fillMaxSize = attr.fillMaxSize
        autoIncrement = attr.autoIncrement
        hasMultiByte = attr.hasMultiByte

        value = if(isFixingValue) attr.value else ""
        multiValues = if(attr.value.contains(",")) attr.value.split(",") else null

        if(autoIncrement && value != "") {
            sequence = try { value.toInt() } catch (e : NumberFormatException) { 0 }
        }
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

        var retVal = value

        if(autoIncrement) {
            sequence++
            retVal = sequence.toString()
        } else if(retVal == "") {
            retVal = if(hasMultiByte) makeMultiByteString() else makeSingleByteString()
        } else if(multiValues != null) {
            val rand =Random()
            retVal = multiValues[rand.nextInt(multiValues.size)]
        }

        if(isZeroPadding) {
            retVal = retVal.padStart(size, '0')
        }

        return retVal
    }

    fun makeSingleByteString() : String {

        val rand =Random()
        val makeSize = if(fillMaxSize) size else if (size >= 6) size/3 else 1
        var buff = ""

        for(idx in 1..makeSize) {
            buff += WORDS[rand.nextInt(WORDS.size)]
        }
        return buff
    }

    fun makeMultiByteString() :String {

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