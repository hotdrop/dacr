package dacr.model

import dacr.indata.ColAttribute
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Grain Class of Date type column
 */
class GrainDate(attr: ColAttribute): IGrain {

    private val name = attr.name
    private val isPrimaryKey = attr.primaryKey
    private val isFixingValue = (attr.valueType.toUpperCase() == ColAttribute.VALUE_TYPE_FIXING)

    private val value = attr.value
    private val isCurrentDate = (value.toUpperCase() == ColAttribute.VALUE_NOW)

    private val values: List<String>?
    private val dtf: DateTimeFormatter

    private var valueIdx = 0

    init {

        values = if(value.contains(",")) value.split(",") else null

        try {
            val format = when {
                attr.format.contains("y") -> attr.format.replace("y", "u")
                attr.format.contains("Y") -> attr.format.replace("Y", "u")
                else -> attr.format
            }
            // formatが誤っていた場合、早めに検知したいのでこのタイミングでofPatternに入れてチェックする
            // 本当はチェック関数を別に作って最初に全部チェックしたほうがいい。
            dtf = DateTimeFormatter.ofPattern(format)
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("incorrect date format. " +
                    " columnName=" + name + " format=" + attr.format, e)
        }
    }

    override fun isPrimaryKey() = isPrimaryKey

    override fun isAutoIncrement() = false

    override fun isFixingValue() = isFixingValue

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