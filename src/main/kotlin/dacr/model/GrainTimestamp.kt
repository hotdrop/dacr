package dacr.model

import dacr.indata.ColAttribute
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Grain Class of Timestamp type column
 */
class GrainTimestamp(attr: ColAttribute): IGrain {

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
            val replaceYearFormat = attr.format.replace("y", "u").replace("Y", "u")
            val replaceYearAndNanoFormat = attr.format.replace("S", "n")
            dtf = if(isCurrentDate) DateTimeFormatter.ofPattern(replaceYearFormat)
                    else DateTimeFormatter.ofPattern(replaceYearAndNanoFormat)
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("incorrect date format. " +
                    " columnName=" + name + " format=" + attr.format, e)
        }
    }

    override fun isPrimaryKey() = isPrimaryKey

    override fun isAutoIncrement() = false

    override fun isFixingValue() = isFixingValue

    override fun create() : String {

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
        // Now 3 digits 999 for Nanos, so even for nnnnnn the first 3 bytes will always be 000.
    }
}