package dacr.model

import dacr.indata.ColAttribute
import java.util.*

/**
 * number型カラムのGrainクラス
 */
class GrainNumber(attr: ColAttribute) : IGrain {

    val name : String
    val primaryKey : Boolean

    private val value : String
    private val multiValues : List<String>?

    private var sequence : Int = 1
    private val size : Int
    private val maxvalue : Int

    private val isFixingValue: Boolean
    private val autoIncrement : Boolean

    init {
        name = attr.name
        primaryKey = attr.primaryKey
        size = attr.size
        var tmp = 1
        for(i in 1..size) {
            tmp *= 10
        }
        maxvalue = tmp - 1

        isFixingValue = if(attr.valueType == ColAttribute.VALUE_TYPE_FIXING) true else false
        autoIncrement = attr.autoIncrement

        value = attr.value

        try {
            // 空の場合は初期値0を設定するため除外している。
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
            } else if(value != ""){
                value.toInt()
                multiValues = null
            } else {
                multiValues = null
            }
        } catch(e : NumberFormatException) {
            throw NumberFormatException("数値でない値が含まれています。空白もダメで数値とカンマだけにしてください。 value=" + value)
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
            retVal = rand.nextInt(maxvalue).toString()
        }

        return retVal
    }
}