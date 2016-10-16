package dacr

import dacr.indata.ColAttribute
import dacr.model.GrainInteger
import org.junit.Assert
import org.junit.Test

class GrainIntegerTest {

    @Test
    fun fixingTest() {

        var grainInteger = GrainInteger(ColAttribute(dataType = "integer", valueType = "fixing", value = "1"))
        Assert.assertEquals(grainInteger.create(), "1")

        grainInteger = GrainInteger(ColAttribute(dataType = "integer",
                size = 5, format = "zeroPadding", autoIncrement = true, fillMaxSize = true,
                valueType = "fixing", value = "7", hasMultiByte = true))
        Assert.assertEquals(grainInteger.create(), "7")
    }

    @Test(expected = NumberFormatException::class)
    fun fixingExceptionTest() {
        val grainInteger = GrainInteger(ColAttribute(dataType = "integer", valueType = "fixing", value = "not Integer"))
        grainInteger.create()
        Assert.assertTrue(false)
    }

    @Test
    fun fixingMultipleValueTest() {
        val grainInteger = GrainInteger(ColAttribute(dataType = "integer",
                valueType = "fixing", value = "35,53,26,692,0,1"))
        Assert.assertEquals(grainInteger.create(), "35")
        Assert.assertEquals(grainInteger.create(), "53")
        Assert.assertEquals(grainInteger.create(), "26")
        Assert.assertEquals(grainInteger.create(), "692")
        Assert.assertEquals(grainInteger.create(), "0")
        Assert.assertEquals(grainInteger.create(), "1")
        Assert.assertEquals(grainInteger.create(), "35")
    }

    @Test
    fun autoIncrementTest() {
        var grainInteger = GrainInteger(ColAttribute(dataType = "integer", autoIncrement = true,
                valueType = "variable", value = ""))

        Assert.assertEquals(grainInteger.create(), "1")
        Assert.assertEquals(grainInteger.create(), "2")
        Assert.assertEquals(grainInteger.create(), "3")
        Assert.assertEquals(grainInteger.create(), "4")
        Assert.assertEquals(grainInteger.create(), "5")

        grainInteger = GrainInteger(ColAttribute(dataType = "integer", autoIncrement = true,
                valueType = "variable", value = "355"))

        Assert.assertEquals(grainInteger.create(), "355")
        Assert.assertEquals(grainInteger.create(), "356")
        Assert.assertEquals(grainInteger.create(), "357")
        Assert.assertEquals(grainInteger.create(), "358")
        Assert.assertEquals(grainInteger.create(), "359")
    }

    @Test
    fun variableTest() {

        var grainInteger = GrainInteger(ColAttribute(dataType = "integer", size = 5, valueType = "variable", value = ""))
        for(i in 1..100) {
            val test = grainInteger.create().toInt()
            Assert.assertTrue(0 < test && test < 100000)
        }

        grainInteger = GrainInteger(ColAttribute(dataType = "integer", size = 5, valueType = "variable", value = "7"))
        for(i in 1..100) {
            val test = grainInteger.create().toInt()
            Assert.assertTrue(0 < test && test < 100000)
        }
    }

    @Test(expected = NumberFormatException::class)
    fun variableExceptionTest() {
        val grainInteger = GrainInteger(ColAttribute(dataType = "integer", size = 5, valueType = "variable", value = "te"))
        grainInteger.create()
        Assert.assertTrue(false)
    }

    @Test
    fun variableMultipleValueTest() {

        val retList = intArrayOf(35, 53, 26, 692, 0, 1)
        val grainInteger = GrainInteger(ColAttribute(dataType = "integer",
                size = 5, valueType = "variable", value = "35,53,26,692,0,1"))

        Assert.assertTrue(retList.contains(grainInteger.create().toInt()))
        Assert.assertTrue(retList.contains(grainInteger.create().toInt()))
        Assert.assertTrue(retList.contains(grainInteger.create().toInt()))
        Assert.assertTrue(retList.contains(grainInteger.create().toInt()))
    }

    @Test(expected = NumberFormatException::class)
    fun multipleValueExceptionTest() {
        val grainInteger = GrainInteger(ColAttribute(dataType = "integer",
                size = 5, valueType = "variable", value = "35,34,26,test,0,1"))
        grainInteger.create()
        Assert.assertTrue(false)
    }
}