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
        val grainTimestamp = GrainTimestamp(ColAttribute(name = "normal", dataType = "timestamp",
                primaryKey = false,
                size = 5, format = "", autoIncrement = false, fillMaxSize = false,
                valueType = "fixing", value = "2016/09/25 18:32:56.132565", hasMultiByte = false))

        Assert.assertEquals(grainTimestamp.create(), "2016/09/25 18:32:56.132565")
    }

    @Test
    fun fixingMultipleValueTest() {
        val grainTimestamp = GrainTimestamp(ColAttribute(name = "normal", dataType = "timestamp",
                primaryKey = false,
                size = 5, format = "YYYY/MM/dd hh:mm:ss.SSS", autoIncrement = false, fillMaxSize = false,
                valueType = "fixing",
                value = "2016/09/23 10:40:23.435,2016/09/24 12:15:34.378,2016/09/25 18:36:21.859",
                hasMultiByte = false))

        Assert.assertEquals(grainTimestamp.create(), "2016/09/23 10:40:23.435")
        Assert.assertEquals(grainTimestamp.create(), "2016/09/24 12:15:34.378")
        Assert.assertEquals(grainTimestamp.create(), "2016/09/25 18:36:21.859")
        Assert.assertEquals(grainTimestamp.create(), "2016/09/23 10:40:23.435")
    }

    @Test
    fun nowTest() {
        var grainTimestamp = GrainTimestamp(ColAttribute(name = "normal", dataType = "timestamp",
                primaryKey = false,
                size = 5, format = "YYYY/MM/dd hh:mm:ss.SSS", autoIncrement = false, fillMaxSize = false,
                valueType = "fixing", value = "now", hasMultiByte = false))

        // ミリ秒を合わせるのが辛いので照合は秒までのTimestampで作成する
        var sdf = SimpleDateFormat("YYYY/MM/dd hh:mm:ss")
        var retStr = sdf.format(Date())
        println("nowTest retStr =" + retStr)
        println("nowTest makeStr=" + grainTimestamp.create())

        Assert.assertTrue(grainTimestamp.create().contains(retStr))

        grainTimestamp = GrainTimestamp(ColAttribute(name = "normal", dataType = "timestamp",
                primaryKey = false,
                size = 5, format = "YYYY-MM-dd hh:mm:ss.SSS", autoIncrement = false, fillMaxSize = false,
                valueType = "variable", value = "now", hasMultiByte = false))

        // 上に同じ
        sdf = SimpleDateFormat("YYYY-MM-dd hh:mm:ss")
        retStr = sdf.format(Date())
        println("nowTest retStr =" + retStr)
        println("nowTest makeStr=" + grainTimestamp.create())

        Assert.assertTrue(grainTimestamp.create().contains(retStr))
    }

    @Test(expected = IllegalArgumentException::class)
    fun formatExceptionTest() {
        val grainTimestamp = GrainTimestamp(ColAttribute(name = "normal", dataType = "timestamp",
                primaryKey = false,
                size = 5, format = "testtest", autoIncrement = false, fillMaxSize = false,
                valueType = "variable", value = "now", hasMultiByte = false))
        grainTimestamp.create()
        Assert.assertTrue(false)
    }

    @Test
    fun variableTest() {
        var grainTimestamp = GrainTimestamp(ColAttribute(name = "normal", dataType = "timestamp",
                primaryKey = false,
                size = 5, format = "YYYY/MM/dd hh:mm:ss.SSS", autoIncrement = false, fillMaxSize = false,
                valueType = "variable", value = "", hasMultiByte = false))

        println("variableTest value=" + grainTimestamp.create())
        println("variableTest value=" + grainTimestamp.create())
        println("variableTest value=" + grainTimestamp.create())
        Assert.assertTrue(true)

        grainTimestamp = GrainTimestamp(ColAttribute(name = "normal", dataType = "timestamp",
                primaryKey = false,
                size = 5, format = "YYYY-MM-dd hh:mm:ss.SSSSSS", autoIncrement = false, fillMaxSize = false,
                valueType = "variable", value = "", hasMultiByte = false))

        println("variableTest value=" + grainTimestamp.create())
        println("variableTest value=" + grainTimestamp.create())
        println("variableTest value=" + grainTimestamp.create())
        Assert.assertTrue(true)
    }

    @Test
    fun variableMultipleValueTest() {
        val retList = arrayOf("2016/09/23 10:40:23.435", "2016/09/24 12:15:34.378", "2016/09/25 18:36:21.859")
        val grainTimestamp = GrainTimestamp(ColAttribute(name = "normal", dataType = "timestamp",
                primaryKey = false,
                size = 5, format = "YYYY/MM/dd hh:mm:ss.SSS", autoIncrement = false, fillMaxSize = false,
                valueType = "variable",
                value = "2016/09/23 10:40:23.435,2016/09/24 12:15:34.378,2016/09/25 18:36:21.859",
                hasMultiByte = false))

        println("variableMultipleValueTest val=" + grainTimestamp.create())
        println("variableMultipleValueTest val=" + grainTimestamp.create())
        println("variableMultipleValueTest val=" + grainTimestamp.create())
        Assert.assertTrue(retList.contains(grainTimestamp.create()))
        Assert.assertTrue(retList.contains(grainTimestamp.create()))
        Assert.assertTrue(retList.contains(grainTimestamp.create()))
    }
}