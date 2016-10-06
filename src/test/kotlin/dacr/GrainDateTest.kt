package dacr

import dacr.indata.ColAttribute
import dacr.model.GrainDate
import org.junit.Assert
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class GrainDateTest {

    @Test
    fun fixingTest() {
        val grainDate = GrainDate(ColAttribute(dataType = "date", valueType = "fixing", value = "2016/09/25"))
        Assert.assertEquals(grainDate.create(), "2016/09/25")
    }

    @Test
    fun fixingMultipleValuesTest() {
        val grainDate = GrainDate(ColAttribute(dataType = "date", format = "YYYY/MM/dd",
                valueType = "fixing", value = "2016/09/23,2016/09/24,2016/09/25"))
        Assert.assertEquals(grainDate.create(), "2016/09/23")
        Assert.assertEquals(grainDate.create(), "2016/09/24")
        Assert.assertEquals(grainDate.create(), "2016/09/25")
        Assert.assertEquals(grainDate.create(), "2016/09/23")
    }

    @Test
    fun nowTest() {

        var grainDate = GrainDate(ColAttribute(dataType = "date", format = "YYYY/MM/dd", valueType = "fixing", value = "now"))
        var sdf = SimpleDateFormat("YYYY/MM/dd")
        var retStr = sdf.format(Date())
        Assert.assertEquals(grainDate.create(), retStr)

        grainDate = GrainDate(ColAttribute(dataType = "date", format = "YYYY-MM-dd", valueType = "variable", value = "now"))
        sdf = SimpleDateFormat("YYYY-MM-dd")
        retStr = sdf.format(Date())
        Assert.assertEquals(grainDate.create(), retStr)

        // upper cast
        grainDate = GrainDate(ColAttribute(dataType = "DATE", format = "YYYY-MM-dd", valueType = "variable", value = "NOW"))
        Assert.assertEquals(grainDate.create(), retStr)
    }

    @Test(expected = IllegalArgumentException::class)
    fun formatExceptionTest() {
        val grainDate = GrainDate(ColAttribute(dataType = "date", format = "test-test", valueType = "fixing", value = "now"))
        grainDate.create()
        Assert.assertTrue(false)
    }

    @Test
    fun variableTest() {
        val grainDate = GrainDate(ColAttribute(dataType = "date", format = "YYYY/MM/dd", valueType = "variable", value = ""))
        println("variableTest date=" + grainDate.create())
        println("variableTest date=" + grainDate.create())
        println("variableTest date=" + grainDate.create())
        Assert.assertTrue(true)
    }

    @Test
    fun variableMultipleValueTest() {
        val retList = arrayOf("2016/09/23", "2016/09/24", "2016/09/25")
        val grainDate = GrainDate(ColAttribute(dataType = "date", format = "YYYY/MM/dd",
                valueType = "variable", value = "2016/09/23,2016/09/24,2016/09/25"))

        Assert.assertTrue(retList.contains(grainDate.create()))
        Assert.assertTrue(retList.contains(grainDate.create()))
        Assert.assertTrue(retList.contains(grainDate.create()))
    }

    @Test
    fun dateInTimeTest() {

        var grainDate = GrainDate(ColAttribute(dataType = "date", format = "YYYY/MM/dd hh:mm:ss",
                valueType = "variable", value = "now"))
        var sdf = SimpleDateFormat("YYYY/MM/dd hh:mm:ss")
        var retStr = sdf.format(Date())
        println("dateInTimeTest retStr=" + retStr)
        println("dateInTimeTest createDate=" + grainDate.create())
        Assert.assertEquals(grainDate.create(), retStr)

        // dataType is date. fixing hour, minute and second if hh:mm:ss in format
        grainDate = GrainDate(ColAttribute(dataType = "date", format = "YYYY/MM/dd hh:mm:ss",
                valueType = "variable", value = ""))
        println("variableDateTest date=" + grainDate.create())
        println("variableDateTest date=" + grainDate.create())
        println("variableDateTest date=" + grainDate.create())
        Assert.assertTrue(true)

        // dateTime is date. variable hour, minute and second
        grainDate = GrainDate(ColAttribute(dataType = "datetime", format = "YYYY/MM/dd hh:mm:ss",
                valueType = "variable", value = ""))
        println("variableDateTest dateTime=" + grainDate.create())
        println("variableDateTest dateTime=" + grainDate.create())
        println("variableDateTest dateTime=" + grainDate.create())
        Assert.assertTrue(true)
    }
}