package dacr

import dacr.indata.ColAttribute
import dacr.model.Sphere
import org.junit.Assert
import org.junit.Test
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.system.measureTimeMillis

class performanceTest {

    @Test
    fun charTest() {
        var fixingList = mutableListOf<ColAttribute>()
        for(i in 1..10) {
            fixingList.add(ColAttribute(dataType = "char", size = 20, valueType = "fixing", value = "12345678901234567890"))
        }

        var varList = mutableListOf<ColAttribute>()
        for(i in 1..10) {
            varList.add(ColAttribute(dataType = "char", size = 200, valueType = "variable", value = ""))
        }

        var varFillList = mutableListOf<ColAttribute>()
        for(i in 1..10) {
            varFillList.add(ColAttribute(dataType = "char", size = 200, fillMaxSize = true, valueType = "variable", value = ""))
        }

        var varEncloseMarkList = mutableListOf<ColAttribute>()
        for(i in 1..10) {
            varEncloseMarkList.add(ColAttribute(dataType = "char", size = 200, valueType = "variable", value = "", encloseChar = ColAttribute.ENCLOSE_CHAR_DOUBLE_QUOTATION))
        }

        // These Test TimeValues on Mac Book Pro Retina
        //  OS: OS X El Capitan
        //  CPU: Intel Core i5 2.7GHx
        //  Memory: 8GB 1867 MHz DDR3
        val fixingTimeAvg = benchMark(fixingList)
        println("fixing time avg:" + fixingTimeAvg)
        Assert.assertTrue(fixingTimeAvg < 100)

        val variableTimeAvg = benchMark(varList)
        println("variable time avg:" + variableTimeAvg)
        Assert.assertTrue(fixingTimeAvg < 1500)

        val charEncloseTimeAvg = benchMark(varEncloseMarkList)
        println("char enclose time avg:" + charEncloseTimeAvg)
        Assert.assertTrue(fixingTimeAvg < 1500)

        val fillMaxTimeAvg = benchMark(varFillList)
        println("fill max time avg:" + fillMaxTimeAvg)
        Assert.assertTrue(fixingTimeAvg < 4000)
    }

    @Test
    fun dateTimeTest() {
        var fixingList = mutableListOf<ColAttribute>()
        for(i in 1..100) {
            fixingList.add(ColAttribute(dataType = "dateTime", valueType = "fixing", value = "2016/09/30 12:50:25"))
        }

        var nowList = mutableListOf<ColAttribute>()
        for(i in 1..100) {
            nowList.add(ColAttribute(dataType = "dateTime", format="uuuu-MM-dd HH:mm:ss", valueType = "variable", value = "now"))
        }

        var varList = mutableListOf<ColAttribute>()
        for(i in 1..100) {
            varList.add(ColAttribute(dataType = "dateTime", format="uuuu-MM-dd HH:mm:ss", valueType = "variable", value = ""))
        }

        val fixingTimeAvg = benchMark(fixingList)
        println("fixing time avg:" + fixingTimeAvg)

        val nowTimeAvg = benchMark(nowList)
        println("now time avg:" + nowTimeAvg)

        val variableTimeAvg = benchMark(varList)
        println("variable time avg:" + variableTimeAvg)

        Assert.assertTrue(true)
    }

    @Test
    fun dateFormatPatternTest() {
        var yyyySlashList = mutableListOf<ColAttribute>()
        for(i in 1..100) {
            yyyySlashList.add(ColAttribute(dataType = "dateTime", format="YYYY/MM/dd HH:mm:ss", valueType = "variable", value = "now"))
        }
        var yyyyISOLocalList = mutableListOf<ColAttribute>()
        for(i in 1..100) {
            yyyyISOLocalList.add(ColAttribute(dataType = "dateTime", format="YYYY-MM-dd HH:mm:ss", valueType = "variable", value = "now"))
        }
        var correctList = mutableListOf<ColAttribute>()
        for(i in 1..100) {
            correctList.add(ColAttribute(dataType = "dateTime", format="uuuu-MM-dd HH:mm:ss", valueType = "variable", value = "now"))
        }

        println("fixing time avg:" + benchMark(yyyySlashList))
        println("now time avg:" + benchMark(yyyyISOLocalList))
        println("variable time avg:" + benchMark(correctList))
    }

    @Test
    fun timestampTest() {
        var fixingList = mutableListOf<ColAttribute>()
        for(i in 1..100) {
            fixingList.add(ColAttribute(dataType = "Timestamp", valueType = "fixing", value = "2016/09/30 12:50:25.382124"))
        }

        var nowList = mutableListOf<ColAttribute>()
        for(i in 1..100) {
            nowList.add(ColAttribute(dataType = "Timestamp", format="yyyy/MM/dd hh:mm:ss.SSSSSS", valueType = "variable", value = "now"))
        }

        var varList = mutableListOf<ColAttribute>()
        for(i in 1..100) {
            varList.add(ColAttribute(dataType = "Timestamp", format="yyyy-MM-dd HH:mm:ss.SSSSSS", valueType = "variable", value = ""))
        }

        val fixingTimeAvg = benchMark(fixingList)
        println("fixing timestamp avg:" + fixingTimeAvg)

        val nowTimeAvg = benchMark(nowList)
        println("now timestamp avg:" + nowTimeAvg)

        val variableTimeAvg = benchMark(varList)
        println("variable timestamp avg:" + variableTimeAvg)

        Assert.assertTrue(true)
    }

    @Test
    fun sdfVsDtfTest() {

        // SimpleDateFormat
        val sdf = SimpleDateFormat("YYYY/MM/dd hh:mm:ss")
        val sdfTimeAvg = measureTimeMillis { for(i in 1..1000000) {sdf.format(Date())} }
        println("simple normal:" + sdfTimeAvg)

        // DateTimeFormatter
        val dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd hh:mm:ss")
        val dtfTimeAvg = measureTimeMillis { for(i in 1..1000000) {dtf.format(LocalDateTime.now())} }
        println("formatter normal:" + dtfTimeAvg)

        Assert.assertTrue(dtfTimeAvg < sdfTimeAvg)

        // calculate SimpleDateFormat
        val sdfCalcTimeAvg = measureTimeMillis {
            for(i in 1..1000000) {
                val cal = Calendar.getInstance()
                val endDateEpoch = cal.timeInMillis
                cal.set(1900,1,1,0,0,0)
                val startDateEpoch = cal.timeInMillis

                val dateRange = ((endDateEpoch - startDateEpoch)/(60 * 60 * 1000 * 24)).toInt()
                val randObj = Random()
                cal.add(Calendar.DAY_OF_MONTH, randObj.nextInt(dateRange))
                cal.add(Calendar.HOUR_OF_DAY, randObj.nextInt(24))
                cal.add(Calendar.MINUTE, randObj.nextInt(60))
                cal.add(Calendar.SECOND, randObj.nextInt(60))
                cal.add(Calendar.MILLISECOND, randObj.nextInt(999))
                sdf.format(cal.time)
            }
        }
        println("simple calc:" + sdfCalcTimeAvg)

        // calculate DateTimeFormatter
        val dtfCalcTimeAvg = measureTimeMillis {
            for(i in 1..1000000) {
                val randObj = Random()
                // LocalDateTime.parse is slower than sdf.
                //dtf.format(LocalDateTime.parse("1900-01-01T00:00:00")
                dtf.format(LocalDateTime.of(1900,1,1,0,0,0)
                        .plusYears(randObj.nextInt(116).toLong())
                        .plusMonths(randObj.nextInt(12).toLong())
                        .plusDays(randObj.nextInt(31).toLong())
                        .plusHours(randObj.nextInt(23).toLong())
                        .plusMinutes(randObj.nextInt(59).toLong())
                        .plusSeconds(randObj.nextInt(59).toLong()))
            }
        }
        println("formatter calc:" + dtfCalcTimeAvg)

        Assert.assertTrue(dtfTimeAvg < sdfTimeAvg)
    }

    private fun benchMark(list: List<ColAttribute>): Long {
        val dataSphere = Sphere(list)
        return measureTimeMillis {
            for(i in 1..100000) {
                dataSphere.create()
            }
        }
    }
}