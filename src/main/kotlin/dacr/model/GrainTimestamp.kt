package dacr.model

import dacr.indata.ColAttribute
import java.text.SimpleDateFormat
import java.util.*

/**
 * timestamp型カラムのGrainクラス
 *
 * 現状、ほぼGrainDateと同じ
 */
class GrainTimestamp(attr: ColAttribute): IGrain {

    override val name: String
    override val primaryKey: Boolean
    override val autoIncrement: Boolean = false
    override val isFixingValue: Boolean

    private val value: String
    private val values: List<String>?
    private var valueIdx = 0
    private val dateFormat: SimpleDateFormat
    private val isCurrentDate: Boolean

    init {
        name = attr.name
        primaryKey = attr.primaryKey

        isFixingValue = if(attr.valueType.toUpperCase() == ColAttribute.VALUE_TYPE_FIXING) true else false

        try {
            dateFormat = SimpleDateFormat(attr.format)
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("日付フォーマットが誤っています。format=" + attr.format, e)
        }

        value = attr.value
        isCurrentDate = if(value.toUpperCase() == ColAttribute.VALUE_NOW) true else false

        values = if(value.contains(",")) value.split(",") else null
    }

    /**
     * timestampの値を生成する
     */
    override fun create() : String {

        // valueに「now」指定がされていた場合はそれを最優先とする
        if(isCurrentDate) {
            return dateFormat.format(Date())
        }

        if(isFixingValue) {
            return makeFixingValue()
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

    private fun makeVariableValue() : String {

        if(values != null) {
            return values[Random().nextInt(values.size)]
        }

        // TODO valueに"2001/1/1 to 2016/12/31"など指定できるようにしたい。今は2000/1/1
        val cal = Calendar.getInstance()
        val endDateEpoch = cal.timeInMillis

        cal.set(2000,1,1,0,0,0)
        val startDateEpoch = cal.timeInMillis

        val dateRange = ((endDateEpoch - startDateEpoch)/(60 * 60 * 1000 * 24)).toInt()

        val randObj = Random()
        cal.add(Calendar.DAY_OF_MONTH, randObj.nextInt(dateRange))
        cal.add(Calendar.HOUR_OF_DAY, randObj.nextInt(24))
        cal.add(Calendar.MINUTE, randObj.nextInt(60))
        cal.add(Calendar.SECOND, randObj.nextInt(60))
        cal.add(Calendar.MILLISECOND, randObj.nextInt(999))

        return dateFormat.format(cal.time)
    }
}