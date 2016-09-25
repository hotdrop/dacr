package dacr

import dacr.indata.ColAttribute
import dacr.model.GrainNumber
import org.junit.Assert
import org.junit.Test

class GrainNumberTest {

    @Test
    fun fixingNumberTest() {
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
    fun fixingNumberExceptionTest() {
        val grainNumber = GrainNumber(ColAttribute(name = "normal", dataType = "number", primaryKey = false,
                size = 5, format = "zeroPadding", autoIncrement = true, fillMaxSize = true,
                valueType = "fixing", value = "auei", hasMultiByte = true))
        grainNumber.create()
        Assert.assertTrue(false)
    }

    @Test
    fun variableNumberTest() {
        var grainNumber = GrainNumber(ColAttribute(name = "normal", dataType = "number", primaryKey = false,
                size = 5, format = "", autoIncrement = false, fillMaxSize = false,
                valueType = "variable", value = "", hasMultiByte = false))

        println("variableNumberTest digits5 val=" + grainNumber.create())
        println("variableNumberTest digits5 val=" + grainNumber.create())
        println("variableNumberTest digits5 val=" + grainNumber.create())
        Assert.assertTrue(grainNumber.create().toInt() < 100000)
        Assert.assertTrue(grainNumber.create().toInt() < 100000)
        Assert.assertTrue(grainNumber.create().toInt() < 100000)
        Assert.assertTrue(grainNumber.create().toInt() < 100000)
        Assert.assertTrue(grainNumber.create().toInt() < 100000)

        grainNumber = GrainNumber(ColAttribute(name = "normal", dataType = "number", primaryKey = false,
                size = 3, format = "zeroPadding", autoIncrement = false, fillMaxSize = true,
                valueType = "variable", value = "7", hasMultiByte = true))

        println("variableNumberTest digits3 val=" + grainNumber.create())
        println("variableNumberTest digits3 val=" + grainNumber.create())
        println("variableNumberTest digits3 val=" + grainNumber.create())
        Assert.assertTrue(grainNumber.create().toInt() < 1000)
        Assert.assertTrue(grainNumber.create().toInt() < 1000)
        Assert.assertTrue(grainNumber.create().toInt() < 1000)
        Assert.assertTrue(grainNumber.create().toInt() < 1000)
        Assert.assertTrue(grainNumber.create().toInt() < 1000)
    }

    @Test(expected = NumberFormatException::class)
    fun variableNumberExceptionTest() {
        val grainNumber = GrainNumber(ColAttribute(name = "normal", dataType = "number", primaryKey = false,
                size = 5, format = "zeroPadding", autoIncrement = true, fillMaxSize = true,
                valueType = "variable", value = "te", hasMultiByte = true))
        grainNumber.create()
        Assert.assertTrue(false)
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
    fun multiValuesTest() {
        val retList = intArrayOf(35, 53, 26, 692, 0, 1)

        val grainNumber = GrainNumber(ColAttribute(name = "normal", dataType = "number", primaryKey = false,
                size = 5, format = "", autoIncrement = false, fillMaxSize = false,
                valueType = "variable", value = "35,53,26,692,0,1", hasMultiByte = false))

        println("multiValueTest value=" + grainNumber.create())
        println("multiValueTest value=" + grainNumber.create())
        println("multiValueTest value=" + grainNumber.create())
        Assert.assertTrue(retList.contains(grainNumber.create().toInt()))
        Assert.assertTrue(retList.contains(grainNumber.create().toInt()))
        Assert.assertTrue(retList.contains(grainNumber.create().toInt()))
        Assert.assertTrue(retList.contains(grainNumber.create().toInt()))
    }

    @Test(expected = NumberFormatException::class)
    fun multiValuesExceptionTest() {
        val grainNumber = GrainNumber(ColAttribute(name = "normal", dataType = "number", primaryKey = false,
                size = 5, format = "", autoIncrement = false, fillMaxSize = false,
                valueType = "variable", value = "35,53,26,aiueo,0,1", hasMultiByte = false))
        grainNumber.create()
        Assert.assertTrue(false)
    }
}