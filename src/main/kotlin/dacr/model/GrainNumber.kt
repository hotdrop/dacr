package dacr.model

import dacr.indata.ColAttribute
import java.util.*

/**
 * number型カラムのGrainクラス
 */
class GrainNumber(attr: ColAttribute) : IGrain {

    override val name : String
    override val primaryKey : Boolean
    override val autoIncrement : Boolean
    override val isFixingValue: Boolean

    private val value : String
    private val values: List<String>?
    private var valueIdx = 0

    private var sequence : Int = 1
    private val size : Int
    private val maxvalue : Int

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
            // valueが空の場合を除外している理由はsequenceを初期値のままにするため。
            if (autoIncrement && value != "") {
                // Number型でtoIntに失敗した場合は例外
                sequence = value.toInt()
            }

            if (value.contains(",")) {
                values = value.split(",")
                for (v: String in values) {
                    // 数値でない値が含まれていた場合は例外
                    v.toInt()
                }
            } else if(value != ""){
                value.toInt()
                values = null
            } else {
                values = null
            }
        } catch(e : NumberFormatException) {
            throw NumberFormatException("数値でない値が含まれています。空白もダメで数値とカンマだけにしてください。 value=" + value)
        }
    }

    /**
     * numberの値を生成する
     */
    override fun create() : String {

        if(isFixingValue) {
            return makeFixingValue()
        }

        if(autoIncrement) {
            return makeAutoIncrement()
        }

        return makeVariableValue()
    }

    /**
     * 固定値作成
     * 表明:isFixingValue=True
     */
    private fun makeFixingValue() : String {
        if(values == null) {
            return value
        }

        val retVal = values[valueIdx]
        valueIdx++

        if(valueIdx >= values.size) {
            valueIdx = 0
        }
        return retVal
    }

    /**
     * 自動連番値作成
     * 表明:isFixingValue=false
     */
    private fun makeAutoIncrement() : String {
        var retVal = sequence.toString()
        sequence++
        return retVal
    }

    /**
     * 可変値作成
     * 表明:isFixingValue=false
     *     makeAutoIncrement=false
     */
    private fun makeVariableValue() : String {
        if (values != null) {
            return values[Random().nextInt(values.size)]
        }
        return Random().nextInt(maxvalue).toString()
    }
}