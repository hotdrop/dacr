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
        val gDate = GrainDate(ColAttribute(valueType = "fixing", value = "2016/09/25"))
        Assert.assertEquals(gDate.create(), "2016/09/25")
    }

    @Test
    fun fixingMultipleValuesTest() {
        val gDate = GrainDate(ColAttribute(format = "YYYY/MM/dd", valueType = "fixing", value = "2016/09/23,2016/09/24,2016/09/25"))
        Assert.assertEquals(gDate.create(), "2016/09/23")
        Assert.assertEquals(gDate.create(), "2016/09/24")
        Assert.assertEquals(gDate.create(), "2016/09/25")
        Assert.assertEquals(gDate.create(), "2016/09/23")
    }

    @Test
    fun nowTest() {

        var gDate = GrainDate(ColAttribute(format = "YYYY/MM/dd", valueType = "fixing", value = "now"))
        var sdf = SimpleDateFormat("YYYY/MM/dd")
        var retStr = sdf.format(Date())
        Assert.assertEquals(gDate.create(), retStr)

        gDate = GrainDate(ColAttribute(format = "YYYY-MM-dd", valueType = "variable", value = "now"))
        sdf = SimpleDateFormat("YYYY-MM-dd")
        retStr = sdf.format(Date())
        Assert.assertEquals(gDate.create(), retStr)

        // upper cast
        gDate = GrainDate(ColAttribute(format = "YYYY-MM-dd", valueType = "variable", value = "NOW"))
        Assert.assertEquals(gDate.create(), retStr)
    }

    @Test(expected = IllegalArgumentException::class)
    fun formatExceptionTest() {
        val gDate = GrainDate(ColAttribute(format = "test-test", valueType = "fixing", value = "now"))
        gDate.create()
        Assert.assertTrue(false)
    }

    @Test
    fun variableTest() {
        val gDate = GrainDate(ColAttribute(format = "YYYY/MM/dd", valueType = "variable", value = ""))
        println("variableTest date=" + gDate.create())
        println("variableTest date=" + gDate.create())
        println("variableTest date=" + gDate.create())
        Assert.assertTrue(true)
    }

    @Test
    fun variableMultipleValueTest() {
        val retList = arrayOf("2016/09/23", "2016/09/24", "2016/09/25")
        val gDate = GrainDate(ColAttribute(format = "YYYY/MM/dd", valueType = "variable", value = "2016/09/23,2016/09/24,2016/09/25"))

        Assert.assertTrue(retList.contains(gDate.create()))
        Assert.assertTrue(retList.contains(gDate.create()))
        Assert.assertTrue(retList.contains(gDate.create()))
    }

    @Test
    fun dateInTimeTest() {

        var gDate = GrainDate(ColAttribute(format = "YYYY/MM/dd hh:mm:ss", valueType = "variable", value = "now"))
        var sdf = SimpleDateFormat("YYYY/MM/dd hh:mm:ss")
        var retStr = sdf.format(Date())
        Assert.assertEquals(gDate.create(), retStr)

        // dataType is date. fixing hour, minute and second if hh:mm:ss in format
        gDate = GrainDate(ColAttribute(format = "YYYY/MM/dd hh:mm:ss", valueType = "variable", value = ""))
        println("dateInTimeTest variable date=" + gDate.create())
        println("dateInTimeTest variable date=" + gDate.create())
        Assert.assertTrue(true)

        // dateTime is date. variable hour, minute and second
        gDate = GrainDate(ColAttribute(dataType = "datetime", format = "YYYY/MM/dd hh:mm:ss", valueType = "variable", value = ""))
        println("dateInTimeTest variable dateTime=" + gDate.create())
        println("dateInTimeTest variable dateTime=" + gDate.create())
        Assert.assertTrue(true)
    }
}