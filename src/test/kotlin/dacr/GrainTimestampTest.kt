package dacr

import dacr.indata.ColAttribute
import dacr.model.GrainTimestamp
import org.junit.Assert
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class GrainTimestampTest {

    @Test
    fun fixingTest() {
        val gTime = GrainTimestamp(ColAttribute(valueType = "fixing", value = "2016/09/25 18:32:56.132565"))
        Assert.assertEquals(gTime.create(), "2016/09/25 18:32:56.132565")
    }

    @Test
    fun fixingMultipleValueTest() {
        val gTime = GrainTimestamp(ColAttribute(format = "YYYY/MM/dd HH:mm:ss.SSS", valueType = "fixing",
                value = "2016/09/23 10:40:23.435,2016/09/24 12:15:34.378,2016/09/25 18:36:21.859"))

        Assert.assertEquals(gTime.create(), "2016/09/23 10:40:23.435")
        Assert.assertEquals(gTime.create(), "2016/09/24 12:15:34.378")
        Assert.assertEquals(gTime.create(), "2016/09/25 18:36:21.859")
        Assert.assertEquals(gTime.create(), "2016/09/23 10:40:23.435")
    }

    @Test
    fun nowTest() {
        var gTime = GrainTimestamp(ColAttribute(format = "YYYY/MM/dd HH:mm:ss.SSS", valueType = "fixing", value = "now"))

        // ミリ秒を合わせるのが辛いので照合は秒までのTimestampでテストする
        var sdf = SimpleDateFormat("YYYY/MM/dd HH:mm:ss")
        var retStr = sdf.format(Date())
        Assert.assertTrue(gTime.create().contains(retStr))

        gTime = GrainTimestamp(ColAttribute(format = "YYYY-MM-dd HH:mm:ss.SSS", valueType = "variable", value = "now"))

        // ミリ秒を合わせるのが辛いので照合は秒までのTimestampでテストする
        sdf = SimpleDateFormat("YYYY-MM-dd HH:mm:ss")
        retStr = sdf.format(Date())
        Assert.assertTrue(gTime.create().contains(retStr))
    }

    @Test(expected = IllegalArgumentException::class)
    fun formatExceptionTest() {
        val gTime = GrainTimestamp(ColAttribute(format = "testtest", valueType = "variable", value = "now"))
        gTime.create()
        Assert.assertTrue(false)
    }

    @Test
    fun variableTest() {
        var gTime = GrainTimestamp(ColAttribute(format = "YYYY/MM/dd HH:mm:ss.SSS",  valueType = "variable", value = ""))
        println("variableTest value=" + gTime.create())
        println("variableTest value=" + gTime.create())
        Assert.assertTrue(true)

        gTime = GrainTimestamp(ColAttribute(format = "YYYY-MM-dd HH:mm:ss.SSSSSS", valueType = "variable", value = ""))
        println("variableTest value=" + gTime.create())
        println("variableTest value=" + gTime.create())
        Assert.assertTrue(true)
    }

    @Test
    fun variableMultipleValueTest() {
        val retList = arrayOf("2016/09/23 10:40:23.435", "2016/09/24 12:15:34.378", "2016/09/25 18:36:21.859")
        val gTime = GrainTimestamp(ColAttribute(format = "YYYY/MM/dd HH:mm:ss.SSS", valueType = "variable",
                value = "2016/09/23 10:40:23.435,2016/09/24 12:15:34.378,2016/09/25 18:36:21.859"))
        Assert.assertTrue(retList.contains(gTime.create()))
        Assert.assertTrue(retList.contains(gTime.create()))
        Assert.assertTrue(retList.contains(gTime.create()))
    }
}