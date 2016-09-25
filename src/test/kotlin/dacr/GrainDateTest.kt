package dacr

import dacr.indata.ColAttribute
import dacr.model.GrainDate
import org.junit.Assert
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class GrainDateTest {

    @Test
    fun fixingDateTest() {

        val grainDate = GrainDate(ColAttribute(name = "normal", dataType = "date", primaryKey = false,
                size = 5, format = "", autoIncrement = false, fillMaxSize = false,
                valueType = "fixing", value = "2016/09/25", hasMultiByte = false))

        Assert.assertEquals(grainDate.create(), "2016/09/25")
    }

    @Test
    fun nowDateTest() {
        var grainDate = GrainDate(ColAttribute(name = "normal", dataType = "date", primaryKey = false,
                size = 5, format = "YYYY/MM/dd", autoIncrement = false, fillMaxSize = false,
                valueType = "fixing", value = "now", hasMultiByte = false))

        var sdf = SimpleDateFormat("YYYY/MM/dd")
        var retStr = sdf.format(Date())
        println("nowDateTest retStr=" + retStr)
        println("nowDateTest createDate=" + grainDate.create())
        Assert.assertEquals(grainDate.create(), retStr)

        grainDate = GrainDate(ColAttribute(name = "normal", dataType = "date", primaryKey = false,
                size = 5, format = "YYYY-MM-dd", autoIncrement = false, fillMaxSize = false,
                valueType = "variable", value = "now", hasMultiByte = false))

        sdf = SimpleDateFormat("YYYY-MM-dd")
        retStr = sdf.format(Date())
        println("nowDateTest retStr=" + retStr)
        println("nowDateTest createDate=" + grainDate.create())
        Assert.assertEquals(grainDate.create(), retStr)
    }

    @Test(expected = IllegalArgumentException::class)
    fun formatExceptionTest() {
        val grainDate = GrainDate(ColAttribute(name = "normal", dataType = "date", primaryKey = false,
                size = 5, format = "test-test", autoIncrement = false, fillMaxSize = false,
                valueType = "fixing", value = "now", hasMultiByte = false))
        grainDate.create()
        Assert.assertTrue(false)
    }

    @Test
    fun multiValuesTest() {
        val retList = arrayOf("2016/09/23", "2016/09/24", "2016/09/25")
        val grainDate = GrainDate(ColAttribute(name = "normal", dataType = "date", primaryKey = false,
                size = 5, format = "YYYY/MM/dd", autoIncrement = false, fillMaxSize = false,
                valueType = "variable", value = "2016/09/23,2016/09/24,2016/09/25", hasMultiByte = false))

        println("multiValuesTest date=" + grainDate.create())
        println("multiValuesTest date=" + grainDate.create())
        println("multiValuesTest date=" + grainDate.create())
        Assert.assertTrue(retList.contains(grainDate.create()))
        Assert.assertTrue(retList.contains(grainDate.create()))
        Assert.assertTrue(retList.contains(grainDate.create()))
    }

    @Test
    fun variableDateTest() {
        val grainDate = GrainDate(ColAttribute(name = "normal", dataType = "date", primaryKey = false,
                size = 5, format = "YYYY/MM/dd", autoIncrement = false, fillMaxSize = false,
                valueType = "variable", value = "", hasMultiByte = false))

        println("variableDateTest date=" + grainDate.create())
        println("variableDateTest date=" + grainDate.create())
        println("variableDateTest date=" + grainDate.create())
        Assert.assertTrue(true)
    }

    @Test
    fun dateInTimeTest() {
        var grainDate = GrainDate(ColAttribute(name = "normal", dataType = "date", primaryKey = false,
                size = 5, format = "YYYY/MM/dd hh:mm:ss", autoIncrement = false, fillMaxSize = false,
                valueType = "variable", value = "now", hasMultiByte = false))

        var sdf = SimpleDateFormat("YYYY/MM/dd hh:mm:ss")
        var retStr = sdf.format(Date())
        println("dateInTimeTest retStr=" + retStr)
        println("dateInTimeTest createDate=" + grainDate.create())
        Assert.assertEquals(grainDate.create(), retStr)

        // dataType is date. fixing hour, minute and second if hh:mm:ss in format
        grainDate = GrainDate(ColAttribute(name = "normal", dataType = "date", primaryKey = false,
                size = 5, format = "YYYY/MM/dd hh:mm:ss", autoIncrement = false, fillMaxSize = false,
                valueType = "variable", value = "", hasMultiByte = false))

        println("variableDateTest date=" + grainDate.create())
        println("variableDateTest date=" + grainDate.create())
        println("variableDateTest date=" + grainDate.create())
        Assert.assertTrue(true)

        // dateTime is date. variable hour, minute and second
        grainDate = GrainDate(ColAttribute(name = "normal", dataType = "datetime", primaryKey = false,
                size = 5, format = "YYYY/MM/dd hh:mm:ss", autoIncrement = false, fillMaxSize = false,
                valueType = "variable", value = "", hasMultiByte = false))

        println("variableDateTest dateTime=" + grainDate.create())
        println("variableDateTest dateTime=" + grainDate.create())
        println("variableDateTest dateTime=" + grainDate.create())
        Assert.assertTrue(true)
    }
}