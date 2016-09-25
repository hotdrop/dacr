package dacr.model

import dacr.indata.ColAttribute
import java.text.SimpleDateFormat
import java.util.*

/**
 * date型カラムのGrainクラス
 */
class GrainDate(attr: ColAttribute) : IGrain {

    val name : String
    val primaryKey : Boolean
    val dataType : String

    private val value : String
    private val multiValues : List<String>?

    private val isFixingValue: Boolean

    private val dateFormat : SimpleDateFormat
    private val isCurrentDate : Boolean

    init {
        name = attr.name
        primaryKey = attr.primaryKey
        dataType = attr.dataType

        isFixingValue = if(attr.valueType == ColAttribute.VALUE_TYPE_FIXING) true else false

        try {
            dateFormat = SimpleDateFormat(attr.format)
        } catch (e : IllegalArgumentException) {
            throw IllegalArgumentException("日付フォーマットが誤っています。format=" + attr.format, e)
        }

        value = attr.value
        isCurrentDate = if(value == ColAttribute.VALUE_NOW) true else false
        multiValues = if(value.contains(",")) value.split(",") else null
    }

    override fun create() : String {

        if(isFixingValue && !isCurrentDate) {
            return value
        }

        if(isCurrentDate) {
            return dateFormat.format(Date())
        }

        if(multiValues != null) {
            val rand = Random()
            return multiValues[rand.nextInt(multiValues.size)]
        }

        // フォーマットに従ってランダムに値を生成する
        return makeValue()
    }

    private fun makeValue() : String {
        // TODO valueに"2001/1/1 to 2016/12/31"など指定できるようにしたい。今は2000/1/1
        val cal = Calendar.getInstance()
        val endDateEpoch = cal.timeInMillis

        cal.set(2000,1,1,0,0,0)
        val startDateEpoch = cal.timeInMillis

        val dateRange = ((endDateEpoch - startDateEpoch)/(60 * 60 * 1000 * 24)).toInt()

        val randObj = Random()
        cal.add(Calendar.DAY_OF_MONTH, randObj.nextInt(dateRange))

        // DateTime型の場合は時分秒もランダム値にする
        if(dataType == ColAttribute.DATA_TYPE_DATETIME) {
            cal.add(Calendar.HOUR_OF_DAY, randObj.nextInt(24))
            cal.add(Calendar.MINUTE, randObj.nextInt(60))
            cal.add(Calendar.SECOND, randObj.nextInt(60))
        }

        return dateFormat.format(cal.time)
    }

}