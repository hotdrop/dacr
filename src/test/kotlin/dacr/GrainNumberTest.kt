package dacr

import dacr.indata.ColAttribute
import dacr.model.GrainNumber
import org.junit.Assert
import org.junit.Test

class GrainNumberTest {

    @Test
    fun fixingTest() {
        var grainNumber = GrainNumber(ColAttribute(name = "normal", dataType = "number", primaryKey = false,
                size = 5, format = "", autoIncrement = false, fillMaxSize = false,
                valueType = "fixing", value = "1", hasMultiByte = false))

        Assert.assertEquals(grainNumber.create(), "1")

        grainNumber = GrainNumber(ColAttribute(name = "normal", dataType = "number", primaryKey = false,
                size = 5, format = "zeroPadding", autoIncrement = true, fillMaxSize = true,
                valueType = "fixing", value = "7", hasMultiByte = true))

        Assert.assertEquals(grainNumber.create(), "7")
    }

    @Test(expected = NumberFormatException::class)
    fun fixingExceptionTest() {
        val grainNumber = GrainNumber(ColAttribute(name = "normal", dataType = "number", primaryKey = false,
                size = 5, format = "zeroPadding", autoIncrement = true, fillMaxSize = true,
                valueType = "fixing", value = "auei", hasMultiByte = true))
        grainNumber.create()
        Assert.assertTrue(false)
    }

    @Test
    fun fixingMultipleValueTest() {
        val grainNumber = GrainNumber(ColAttribute(name = "normal", dataType = "number", primaryKey = false,
                size = 5, format = "", autoIncrement = false, fillMaxSize = false,
                valueType = "fixing", value = "35,53,26,692,0,1", hasMultiByte = false))
        Assert.assertEquals(grainNumber.create(), "35")
        Assert.assertEquals(grainNumber.create(), "53")
        Assert.assertEquals(grainNumber.create(), "26")
        Assert.assertEquals(grainNumber.create(), "692")
        Assert.assertEquals(grainNumber.create(), "0")
        Assert.assertEquals(grainNumber.create(), "1")
        Assert.assertEquals(grainNumber.create(), "35")
    }

    @Test
    fun autoIncrementTest() {
        var grainNumber = GrainNumber(ColAttribute(name = "normal", dataType = "number", primaryKey = false,
                size = 5, format = "", autoIncrement = true, fillMaxSize = false,
                valueType = "variable", value = "", hasMultiByte = false))

        Assert.assertEquals(grainNumber.create(), "1")
        Assert.assertEquals(grainNumber.create(), "2")
        Assert.assertEquals(grainNumber.create(), "3")
        Assert.assertEquals(grainNumber.create(), "4")
        Assert.assertEquals(grainNumber.create(), "5")

        grainNumber = GrainNumber(ColAttribute(name = "normal", dataType = "number", primaryKey = false,
                size = 5, format = "", autoIncrement = true, fillMaxSize = false,
                valueType = "variable", value = "355", hasMultiByte = false))

        Assert.assertEquals(grainNumber.create(), "355")
        Assert.assertEquals(grainNumber.create(), "356")
        Assert.assertEquals(grainNumber.create(), "357")
        Assert.assertEquals(grainNumber.create(), "358")
        Assert.assertEquals(grainNumber.create(), "359")
    }

    @Test
    fun variableTest() {
        var grainNumber = GrainNumber(ColAttribute(name = "normal", dataType = "number", primaryKey = false,
                size = 5, format = "", autoIncrement = false, fillMaxSize = false,
                valueType = "variable", value = "", hasMultiByte = false))

        println("variableTest digits5 val=" + grainNumber.create())
        println("variableTest digits5 val=" + grainNumber.create())
        println("variableTest digits5 val=" + grainNumber.create())
        Assert.assertTrue(grainNumber.create().toInt() < 100000)
        Assert.assertTrue(grainNumber.create().toInt() < 100000)
        Assert.assertTrue(grainNumber.create().toInt() < 100000)
        Assert.assertTrue(grainNumber.create().toInt() < 100000)
        Assert.assertTrue(grainNumber.create().toInt() < 100000)

        grainNumber = GrainNumber(ColAttribute(name = "normal", dataType = "number", primaryKey = false,
                size = 3, format = "zeroPadding", autoIncrement = false, fillMaxSize = true,
                valueType = "variable", value = "7", hasMultiByte = true))

        println("variableTest digits3 val=" + grainNumber.create())
        println("variableTest digits3 val=" + grainNumber.create())
        println("variableTest digits3 val=" + grainNumber.create())
        Assert.assertTrue(grainNumber.create().toInt() < 1000)
        Assert.assertTrue(grainNumber.create().toInt() < 1000)
        Assert.assertTrue(grainNumber.create().toInt() < 1000)
        Assert.assertTrue(grainNumber.create().toInt() < 1000)
        Assert.assertTrue(grainNumber.create().toInt() < 1000)
    }

    @Test(expected = NumberFormatException::class)
    fun variableExceptionTest() {
        val grainNumber = GrainNumber(ColAttribute(name = "normal", dataType = "number", primaryKey = false,
                size = 5, format = "zeroPadding", autoIncrement = true, fillMaxSize = true,
                valueType = "variable", value = "te", hasMultiByte = true))
        grainNumber.create()
        Assert.assertTrue(false)
    }

    @Test
    fun variableMultipleValueTest() {
        val retList = intArrayOf(35, 53, 26, 692, 0, 1)

        val grainNumber = GrainNumber(ColAttribute(name = "normal", dataType = "number", primaryKey = false,
                size = 5, format = "", autoIncrement = false, fillMaxSize = false,
                valueType = "variable", value = "35,53,26,692,0,1", hasMultiByte = false))

        println("variableMultipleValueTest value=" + grainNumber.create())
        println("variableMultipleValueTest value=" + grainNumber.create())
        println("variableMultipleValueTest value=" + grainNumber.create())
        Assert.assertTrue(retList.contains(grainNumber.create().toInt()))
        Assert.assertTrue(retList.contains(grainNumber.create().toInt()))
        Assert.assertTrue(retList.contains(grainNumber.create().toInt()))
        Assert.assertTrue(retList.contains(grainNumber.create().toInt()))
    }

    @Test(expected = NumberFormatException::class)
    fun multipleValueExceptionTest() {
        val grainNumber = GrainNumber(ColAttribute(name = "normal", dataType = "number", primaryKey = false,
                size = 5, format = "", autoIncrement = false, fillMaxSize = false,
                valueType = "variable", value = "35,34,26,test,0,1", hasMultiByte = false))
        grainNumber.create()
        Assert.assertTrue(false)
    }
}