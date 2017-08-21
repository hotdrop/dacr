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

    private val benchMarkLoopCnt = 10
    private val testListSize = 10

    @Test
    fun charTest() {
        val fixingList = mutableListOf<ColAttribute>()
        for(i in 1..testListSize) {
            fixingList.add(ColAttribute(dataType = "char", size = 20, valueType = "fixing", value = "12345678901234567890"))
        }

        val varList = mutableListOf<ColAttribute>()
        for(i in 1..testListSize) {
            varList.add(ColAttribute(dataType = "char", size = 200, valueType = "variable", value = ""))
        }

        val varFillList = mutableListOf<ColAttribute>()
        for(i in 1..testListSize) {
            varFillList.add(ColAttribute(dataType = "char", size = 200, fillMaxSize = true, valueType = "variable", value = ""))
        }

        val varEncloseMarkList = mutableListOf<ColAttribute>()
        for(i in 1..testListSize) {
            varEncloseMarkList.add(ColAttribute(dataType = "char", size = 200, valueType = "variable", value = "", encloseChar = ColAttribute.ENCLOSE_CHAR_DOUBLE_QUOTATION))
        }

        // These Test TimeValues on Mac Book Pro Retina
        //  OS: OS X El Capitan
        //  CPU: Intel Core i5 2.7GHx
        //  Memory: 8GB 1867 MHz DDR3
        val fixingTimeAvg = benchMark(fixingList)
        println("CharTest fixing   time:" + fixingTimeAvg)
        Assert.assertTrue(fixingTimeAvg < 100)

        val variableTimeAvg = benchMark(varList)
        println("CharTest variable time:" + variableTimeAvg)
        Assert.assertTrue(fixingTimeAvg < 1500)

        val charEncloseTimeAvg = benchMark(varEncloseMarkList)
        println("CharTest enclose  time:" + charEncloseTimeAvg)
        Assert.assertTrue(fixingTimeAvg < 1500)

        val fillMaxTimeAvg = benchMark(varFillList)
        println("CharTest fillMax  time:" + fillMaxTimeAvg)
        Assert.assertTrue(fixingTimeAvg < 4000)
    }

    @Test
    fun dateTimeTest() {
        val fixingList = mutableListOf<ColAttribute>()
        for(i in 1..testListSize) {
            fixingList.add(ColAttribute(dataType = "dateTime", valueType = "fixing", value = "2016/09/30 12:50:25"))
        }

        val nowList = mutableListOf<ColAttribute>()
        for(i in 1..testListSize) {
            nowList.add(ColAttribute(dataType = "dateTime", format="uuuu-MM-dd HH:mm:ss", valueType = "variable", value = "now"))
        }

        val varList = mutableListOf<ColAttribute>()
        for(i in 1..testListSize) {
            varList.add(ColAttribute(dataType = "dateTime", format="uuuu-MM-dd HH:mm:ss", valueType = "variable", value = ""))
        }

        val fixingTimeAvg = benchMark(fixingList)
        println("DateTest fixing   time:" + fixingTimeAvg)

        val nowTimeAvg = benchMark(nowList)
        println("DateTest now      time:" + nowTimeAvg)

        val variableTimeAvg = benchMark(varList)
        println("DateTest variable time:" + variableTimeAvg)
    }

    @Test
    fun dateFormatPatternTest() {
        val yyyySlashList = mutableListOf<ColAttribute>()
        for(i in 1..testListSize) {
            yyyySlashList.add(ColAttribute(dataType = "dateTime", format="YYYY/MM/dd HH:mm:ss", valueType = "variable", value = "now"))
        }
        val yyyyISOLocalList = mutableListOf<ColAttribute>()
        for(i in 1..testListSize) {
            yyyyISOLocalList.add(ColAttribute(dataType = "dateTime", format="YYYY-MM-dd HH:mm:ss", valueType = "variable", value = "now"))
        }
        val correctList = mutableListOf<ColAttribute>()
        for(i in 1..testListSize) {
            correctList.add(ColAttribute(dataType = "dateTime", format="uuuu-MM-dd HH:mm:ss", valueType = "variable", value = "now"))
        }

        val yyyyTimeAvg = benchMark(yyyySlashList)
        println("DateFormatTest yyyy/MM/dd time:" + yyyyTimeAvg)
        val isoLocalTimeAvg = benchMark(yyyyISOLocalList)
        println("DateFormatTest YYYY-MM-dd time:" + isoLocalTimeAvg)
        val correctTImeAvg = benchMark(correctList)
        println("DateFormatTest uuuu-MM-dd time:" + correctTImeAvg)

        val indication = correctTImeAvg + 500 //ms
        Assert.assertTrue(yyyyTimeAvg < indication)
        Assert.assertTrue(isoLocalTimeAvg < indication)
    }

    @Test
    fun timestampTest() {
        val fixingList = mutableListOf<ColAttribute>()
        for(i in 1..testListSize) {
            fixingList.add(ColAttribute(dataType = "Timestamp", valueType = "fixing", value = "2016/09/30 12:50:25.382124"))
        }

        val nowList = mutableListOf<ColAttribute>()
        for(i in 1..testListSize) {
            nowList.add(ColAttribute(dataType = "Timestamp", format="yyyy/MM/dd hh:mm:ss.SSSSSS", valueType = "variable", value = "now"))
        }

        val varList = mutableListOf<ColAttribute>()
        for(i in 1..testListSize) {
            varList.add(ColAttribute(dataType = "Timestamp", format="yyyy-MM-dd HH:mm:ss.SSSSSS", valueType = "variable", value = ""))
        }

        val fixingTimeAvg = benchMark(fixingList)
        println("TimestampTest fixing   time:" + fixingTimeAvg)

        val nowTimeAvg = benchMark(nowList)
        println("TimestampTest now      time:" + nowTimeAvg)

        val variableTimeAvg = benchMark(varList)
        println("TimestampTest variable time:" + variableTimeAvg)

        Assert.assertTrue(true)
    }

    private fun benchMark(list: List<ColAttribute>): Long {
        val dataSphere = Sphere(list)
        return measureTimeMillis {
            for(i in 1..benchMarkLoopCnt) {
                dataSphere.create()
            }
        }
    }

    // This performance Test is very load.
    //@Test
    fun sdfVsDtfTest() {

        println("SimpleDateFormat vs DateTimeFormatter round 1!")
        // SimpleDateFormat
        val sdf = SimpleDateFormat("YYYY/MM/dd hh:mm:ss")
        val sdfTimeAvg = measureTimeMillis { for(i in 1..10000000) {sdf.format(Date())} }
        println("      SimpleDateFormat  nowDate time:" + sdfTimeAvg)

        // DateTimeFormatter
        val dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd hh:mm:ss")
        val dtfTimeAvg = measureTimeMillis { for(i in 1..10000000) {dtf.format(LocalDateTime.now())} }
        println("      DateTimeFormatter nowDate time:" + dtfTimeAvg)

        Assert.assertTrue(dtfTimeAvg < sdfTimeAvg)

        println("SimpleDateFormat vs DateTimeFormatter round 2!")
        // calculate SimpleDateFormat
        val sdfCalcTimeAvg = measureTimeMillis {
            for(i in 1..10000000) {
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
        println("      SimpleDateFormat  date calculate time:" + sdfCalcTimeAvg)

        // calculate DateTimeFormatter
        val dtfCalcTimeAvg = measureTimeMillis {
            for(i in 1..10000000) {
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
        println("      DateTimeFormatter date calculate time:" + dtfCalcTimeAvg)

        Assert.assertTrue(dtfTimeAvg < sdfTimeAvg)
    }
}