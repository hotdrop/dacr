package dacr.model

import dacr.indata.ColAttribute
import java.util.*

/**
 * number型カラムのGrainクラス
 */
class GrainNumber(attr: ColAttribute) : IGrain {

    val name : String
    val primaryKey : Boolean

    val value : String
    private val multiValues : List<String>?

    private var sequence : Int = 1
    val size : Int

    val isFixingValue: Boolean
    val autoIncrement : Boolean

    init {
        name = attr.name
        primaryKey = attr.primaryKey
        size = attr.size

        isFixingValue = if(attr.valueType == ColAttribute.VALUE_TYPE_FIXING) true else false
        autoIncrement = attr.autoIncrement

        value = attr.value

        try {
            if (autoIncrement && value != "") {
                // Number型でtoIntに失敗した場合は例外
                sequence = value.toInt()
            }

            if (value.contains(",")) {
                multiValues = value.split(",")
                for (v: String in multiValues) {
                    // 数値でない値が含まれていた場合は例外
                    v.toInt()
                }
            } else {
                multiValues = null
            }
        } catch(e : NumberFormatException) {
            throw NumberFormatException("数値でない値が含まれています。 value=" + value)
        }
    }

    override fun create() : String {

        if(isFixingValue) {
            return value
        }

        if(multiValues != null) {
            val rand = Random()
            return multiValues[rand.nextInt(multiValues.size)]
        }

        val retVal : String
        if(autoIncrement) {
            retVal = sequence.toString()
            sequence++
        } else {
            val rand = Random()
            retVal = rand.nextInt(size).toString()
        }

        return retVal
    }
}