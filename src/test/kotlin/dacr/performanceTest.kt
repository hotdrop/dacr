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

    // parameter for performance test
    private val createRecordCount = 1000
    private val createColumnNum = 100
    private val loopCntForAverage = 10

    @Test
    fun charTest() {
        // make test data
        val fixingColumns = mutableListOf<ColAttribute>()
        for(i in 1..createColumnNum) {
            fixingColumns.add(ColAttribute(dataType = "char", size = 20, valueType = "fixing", value = "12345678901234567890"))
        }

        val varColumns = mutableListOf<ColAttribute>()
        for(i in 1..createColumnNum) {
            varColumns.add(ColAttribute(dataType = "char", size = 200, valueType = "variable", value = ""))
        }

        val varFillColumns = mutableListOf<ColAttribute>()
        for(i in 1..createColumnNum) {
            varFillColumns.add(ColAttribute(dataType = "char", size = 200, fillMaxSize = true, valueType = "variable", value = ""))
        }

        val varEncloseMarkColumns = mutableListOf<ColAttribute>()
        for(i in 1..createColumnNum) {
            varEncloseMarkColumns.add(ColAttribute(dataType = "char", size = 200, valueType = "variable", value = "", encloseChar = ColAttribute.ENCLOSE_CHAR_DOUBLE_QUOTATION))
        }

        // execute test

        // These Test TimeValues on Mac Book Pro Retina
        //  OS: OS X
        //  CPU: Intel Core i5 2.7GHx
        //  Memory: 8GB 1867 MHz DDR3
        val purposeFixingTime = 100
        val purposeVariableTime = 2000
        val purposeEncloseTime = 2000
        val purposeFillMaxTime = 4000

        println("Start Char performance test.")
        println("  Benchmark data: char columns = $createColumnNum. create records = $createRecordCount.")

        printResultAndAssert((1..loopCntForAverage).map { benchMark(fixingColumns) }.average(), purposeFixingTime)
        printResultAndAssert((1..loopCntForAverage).map { benchMark(varColumns) }.average(), purposeVariableTime)
        printResultAndAssert((1..loopCntForAverage).map { benchMark(varEncloseMarkColumns) }.average(), purposeEncloseTime)
        printResultAndAssert((1..loopCntForAverage).map { benchMark(varFillColumns) }.average(), purposeFillMaxTime)
    }

    @Test
    fun dateTimeTest() {
        // make test data
        val fixingColumns = mutableListOf<ColAttribute>()
        for(i in 1..createColumnNum) {
            fixingColumns.add(ColAttribute(dataType = "dateTime", valueType = "fixing", value = "2016/09/30 12:50:25"))
        }

        val nowColumns = mutableListOf<ColAttribute>()
        for(i in 1..createColumnNum) {
            nowColumns.add(ColAttribute(dataType = "dateTime", format="uuuu-MM-dd HH:mm:ss", valueType = "variable", value = "now"))
        }

        val varColumns = mutableListOf<ColAttribute>()
        for(i in 1..createColumnNum) {
            varColumns.add(ColAttribute(dataType = "dateTime", format="uuuu-MM-dd HH:mm:ss", valueType = "variable", value = ""))
        }

        // execute test
        val purposeFixingTime = 100
        val purposeNowTime = 200
        val purposeVariableTime = 200

        println("Start Date performance test.")
        println("  Benchmark data: date columns = $createColumnNum. create records = $createRecordCount.")

        printResultAndAssert((1..loopCntForAverage).map { benchMark(fixingColumns) }.average(), purposeFixingTime)
        printResultAndAssert((1..loopCntForAverage).map { benchMark(nowColumns) }.average(), purposeNowTime)
        printResultAndAssert((1..loopCntForAverage).map { benchMark(varColumns) }.average(), purposeVariableTime)
    }

    @Test
    fun dateFormatPatternTest() {
        // make test data
        val yyyySlashColumns = mutableListOf<ColAttribute>()
        for(i in 1..createColumnNum) {
            yyyySlashColumns.add(ColAttribute(dataType = "dateTime", format="YYYY/MM/dd HH:mm:ss", valueType = "variable", value = "now"))
        }
        val yyyyISOLocalColumns = mutableListOf<ColAttribute>()
        for(i in 1..createColumnNum) {
            yyyyISOLocalColumns.add(ColAttribute(dataType = "dateTime", format="YYYY-MM-dd HH:mm:ss", valueType = "variable", value = "now"))
        }
        val correctColumns = mutableListOf<ColAttribute>()
        for(i in 1..createColumnNum) {
            correctColumns.add(ColAttribute(dataType = "dateTime", format="uuuu-MM-dd HH:mm:ss", valueType = "variable", value = "now"))
        }

        // execute test
        val purposeSlashTime = 100
        val purposeIsoLocalTime = 100
        val purposeCorrectTime = 100

        println("Start DateFormat performance test.")
        println("  Benchmark data: DateFormat columns = $createColumnNum. create records = $createRecordCount.")

        printResultAndAssert((1..loopCntForAverage).map { benchMark(yyyySlashColumns) }.average(), purposeSlashTime)
        printResultAndAssert((1..loopCntForAverage).map { benchMark(yyyyISOLocalColumns) }.average(), purposeIsoLocalTime)
        printResultAndAssert((1..loopCntForAverage).map { benchMark(correctColumns) }.average(), purposeCorrectTime)
    }

    @Test
    fun timestampTest() {
        // make test data
        val fixingColumns = mutableListOf<ColAttribute>()
        for(i in 1..createColumnNum) {
            fixingColumns.add(ColAttribute(dataType = "Timestamp", valueType = "fixing", value = "2016/09/30 12:50:25.382124"))
        }

        val nowColumns = mutableListOf<ColAttribute>()
        for(i in 1..createColumnNum) {
            nowColumns.add(ColAttribute(dataType = "Timestamp", format="yyyy/MM/dd hh:mm:ss.SSSSSS", valueType = "variable", value = "now"))
        }

        val varColumns = mutableListOf<ColAttribute>()
        for(i in 1..createColumnNum) {
            varColumns.add(ColAttribute(dataType = "Timestamp", format="yyyy-MM-dd HH:mm:ss.SSSSSS", valueType = "variable", value = ""))
        }

        // execute test
        val purposeFixingTime = 100
        val purposeNowTime = 200
        val purposeVariableTime = 200

        println("Start Timestamp performance test.")
        println("  Benchmark data: Timestamp columns = $createColumnNum. create records = $createRecordCount.")

        printResultAndAssert((1..loopCntForAverage).map { benchMark(fixingColumns) }.average(), purposeFixingTime)
        printResultAndAssert((1..loopCntForAverage).map { benchMark(nowColumns) }.average(), purposeNowTime)
        printResultAndAssert((1..loopCntForAverage).map { benchMark(varColumns) }.average(), purposeVariableTime)
    }

    private fun benchMark(list: List<ColAttribute>): Long {
        val dataSphere = Sphere(list)
        return measureTimeMillis {
            for(i in 1..createRecordCount) {
                dataSphere.create()
            }
        }
    }

    private fun printResultAndAssert(avg: Double, purposeTime: Int) {
        println("   Average of $loopCntForAverage times: $avg ms")
        assert(avg < purposeTime) { "   Performance shortage! target $purposeTime ms under." }
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