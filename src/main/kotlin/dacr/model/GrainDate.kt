package dacr.model

import dacr.indata.ColAttribute
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * date型カラムのGrainクラス
 */
class GrainDate(attr: ColAttribute): IGrain {

    override val name: String
    override val primaryKey: Boolean
    override val autoIncrement: Boolean = false
    override val isFixingValue: Boolean

    private val dataType: String
    private val value: String
    private val values: List<String>?
    private var valueIdx = 0
    private val dtf: DateTimeFormatter
    private val isCurrentDate: Boolean

    init {
        name = attr.name
        primaryKey = attr.primaryKey
        dataType = attr.dataType.toUpperCase()

        isFixingValue = if(attr.valueType.toUpperCase() == ColAttribute.VALUE_TYPE_FIXING) true else false

        try {
            // DateTimeFormatter
            var format = if(attr.format.contains("y")) attr.format.replace("y", "u")
                            else if(attr.format.contains("Y")) attr.format.replace("Y", "u")
                            else attr.format
            // formatが誤っていた場合、早めに検知したいのでこのタイミングでofPatternに入れてチェックする
            // 本当はチェック関数を別に作って最初に全部チェックしたほうがいい。
            dtf = DateTimeFormatter.ofPattern(format)
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("日付フォーマットが誤っています。format=" + attr.format, e)
        }

        value = attr.value
        isCurrentDate = if(value.toUpperCase() == ColAttribute.VALUE_NOW) true else false

        values = if(value.contains(",")) value.split(",") else null
    }

    /**
     * dateの値を生成する
     */
    override fun create(): String {

        // Dateの場合、valueに「now」指定がされていた場合はそれを最優先とする
        if(isCurrentDate) {
            return dtf.format(LocalDateTime.now())
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

    private fun makeVariableValue(): String {

        if(values != null) {
            return values[Random().nextInt(values.size)]
        }

        val randObj = Random()
        return dtf.format(LocalDateTime.of(1900,1,1,0,0,0)
                .plusYears(randObj.nextInt(116).toLong())
                .plusMonths(randObj.nextInt(12).toLong())
                .plusDays(randObj.nextInt(31).toLong())
                .plusHours(randObj.nextInt(23).toLong())
                .plusMinutes(randObj.nextInt(59).toLong())
                .plusSeconds(randObj.nextInt(59).toLong()))
    }
}