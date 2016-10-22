package dacr.model

import dacr.indata.ColAttribute
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * timestamp型カラムのGrainクラス
 */
class GrainTimestamp(attr: ColAttribute): IGrain {

    override val name: String
    override val primaryKey: Boolean
    override val autoIncrement: Boolean = false
    override val isFixingValue: Boolean

    private val value: String
    private val values: List<String>?
    private var valueIdx = 0
    private val dtf: DateTimeFormatter
    private val isCurrentDate: Boolean

    init {
        name = attr.name
        primaryKey = attr.primaryKey

        isFixingValue = if(attr.valueType.toUpperCase() == ColAttribute.VALUE_TYPE_FIXING) true else false
        value = attr.value
        values = if(value.contains(",")) value.split(",") else null
        isCurrentDate = if(value.toUpperCase() == ColAttribute.VALUE_NOW) true else false

        try {
            // LocalDateTime.nowを使用する場合、ナノ秒nはエラーになるので以下の通り分ける
            val replaceYearFormat = attr.format.replace("y", "u").replace("Y", "u")
            val replaceYearAndNanoFormat = attr.format.replace("S", "n")
            dtf = if(isCurrentDate) DateTimeFormatter.ofPattern(replaceYearFormat)
                    else DateTimeFormatter.ofPattern(replaceYearAndNanoFormat)
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("incorrect date format. " +
                    " columnName=" + name + " format=" + attr.format, e)
        }
    }

    override fun create() : String {

        // valueに「now」指定がされていた場合はそれを最優先とする
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

    private fun makeVariableValue() : String {

        if(values != null) {
            return values[Random().nextInt(values.size)]
        }

        val randObj = Random()
        return dtf.format(LocalDateTime.of(1900,1,1,0,0,0,0)
                .plusYears(randObj.nextInt(116).toLong())
                .plusMonths(randObj.nextInt(12).toLong())
                .plusDays(randObj.nextInt(31).toLong())
                .plusHours(randObj.nextInt(23).toLong())
                .plusMinutes(randObj.nextInt(59).toLong())
                .plusSeconds(randObj.nextInt(59).toLong())
                .plusNanos(randObj.nextInt(999).toLong()))
        // TODO 今はNanosを3桁999にしているため、nnnnnnとしても先頭３バイトは必ず000になる。
        // そのため、本当はフォーマットのナノ桁数をcountしてnextIntの値を変えるべき
    }
}