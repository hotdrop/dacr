package dacr.model

import dacr.indata.ColAttribute
import java.util.*

/**
 * Integer型カラムのGrainクラス
 */
class GrainInteger(attr: ColAttribute): IGrain {

    private val name: String
    private val primaryKey: Boolean
    private val autoIncrement: Boolean
    private val fixingValue: Boolean
    private val value: String
    private val values: List<String>?
    private val size: Int
    private val rangeMin: Int
    private val rangeMax: Int

    private var valueIdx = 0
    private var sequence = 1

    init {
        name = attr.name
        primaryKey = attr.primaryKey
        size = attr.size

        fixingValue = if(attr.valueType.toUpperCase() == ColAttribute.VALUE_TYPE_FIXING) true else false
        autoIncrement = attr.autoIncrement

        try {
            if (attr.value.contains(",")) {
                value = attr.value
                values = value.split(",").map(String::trim)
                // 単なる数値型チェック
                values.forEach { it.toInt() }
                // valuesが指定されている場合、rangeは使用しないので0
                rangeMin = 0
                rangeMax = 0
            } else if(attr.value.toUpperCase().contains("TO")) {
                val betweenList = attr.value.toUpperCase().split("TO")
                value = betweenList[0].trim()
                values = null
                rangeMin = betweenList[0].trim().toInt()
                rangeMax = if(betweenList[1].trim().toLong() < Int.MAX_VALUE) betweenList[1].trim().toInt() else Int.MAX_VALUE
                sequence = rangeMin
            } else if(attr.value != ""){
                value = attr.value.trim()
                values = null
                rangeMin = value.toInt()
                rangeMax = rangeMax()
                sequence = rangeMin
            } else {
                value = attr.value
                values = null
                rangeMin = 1
                rangeMax = rangeMax()
            }
        } catch(e: NumberFormatException) {
            throw NumberFormatException("incorrect value by Integer. " +
            " columnName=" + name + " format=" + attr.value)
        }
    }

    private fun rangeMax(): Int {
        if(size == 0 || size > 10) {
            return Int.MAX_VALUE
        }
        var tmp: Long = 1
        for(i in 1..size) { tmp *= 10 }
        val maxvalue: Long = tmp - 1
        return if(maxvalue >= Int.MAX_VALUE) Int.MAX_VALUE else maxvalue.toInt()
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

        if(fixingValue) {
            return makeFixingValue()
        }

        if(autoIncrement) {
            return makeAutoIncrement()
        }

        return makeVariableValue()
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
        var retVal = sequence.toString()
        sequence = if(sequence >= rangeMax) rangeMin else ++sequence
        return retVal
    }

    private fun makeVariableValue(): String {

        if (values != null) {
            return values[Random().nextInt(values.size)]
        }

        // 範囲が負数の場合だけ少し特殊な計算をする
        if(rangeMax < 0) {
            val argNextInt = (rangeMin * -1) - (rangeMax * -1)
            val randInt = Random().nextInt(argNextInt) + (rangeMax * -1)
            return (randInt * -1).toString()
        }

        if(rangeMin <= 0) {
            return (Random().nextInt(rangeMax) + rangeMin).toString()
        }

        return (Random().nextInt(rangeMax - rangeMin) + rangeMin).toString()
    }
}