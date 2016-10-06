package dacr

import dacr.indata.ColAttribute
import dacr.model.GrainNumber
import org.junit.Assert
import org.junit.Test

class GrainNumberTest {

    @Test
    fun fixingTest() {

        var grainNumber = GrainNumber(ColAttribute(dataType = "number", valueType = "fixing", value = "1"))
        Assert.assertEquals(grainNumber.create(), "1")

        grainNumber = GrainNumber(ColAttribute(dataType = "number",
                size = 5, format = "zeroPadding", autoIncrement = true, fillMaxSize = true,
                valueType = "fixing", value = "7", hasMultiByte = true))
        Assert.assertEquals(grainNumber.create(), "7")
    }

    @Test(expected = NumberFormatException::class)
    fun fixingExceptionTest() {
        val grainNumber = GrainNumber(ColAttribute(dataType = "number", valueType = "fixing", value = "not number"))
        grainNumber.create()
        Assert.assertTrue(false)
    }

    @Test
    fun fixingMultipleValueTest() {
        val grainNumber = GrainNumber(ColAttribute(dataType = "number",
                valueType = "fixing", value = "35,53,26,692,0,1"))
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
        var grainNumber = GrainNumber(ColAttribute(dataType = "number", autoIncrement = true,
                valueType = "variable", value = ""))

        Assert.assertEquals(grainNumber.create(), "1")
        Assert.assertEquals(grainNumber.create(), "2")
        Assert.assertEquals(grainNumber.create(), "3")
        Assert.assertEquals(grainNumber.create(), "4")
        Assert.assertEquals(grainNumber.create(), "5")

        grainNumber = GrainNumber(ColAttribute(dataType = "number", autoIncrement = true,
                valueType = "variable", value = "355"))

        Assert.assertEquals(grainNumber.create(), "355")
        Assert.assertEquals(grainNumber.create(), "356")
        Assert.assertEquals(grainNumber.create(), "357")
        Assert.assertEquals(grainNumber.create(), "358")
        Assert.assertEquals(grainNumber.create(), "359")
    }

    @Test
    fun variableTest() {

        var grainNumber = GrainNumber(ColAttribute(dataType = "number", size = 5, valueType = "variable", value = ""))
        for(i in 1..100) {
            val test = grainNumber.create().toInt()
            Assert.assertTrue(0 < test && test < 100000)
        }

        grainNumber = GrainNumber(ColAttribute(dataType = "number", size = 5, valueType = "variable", value = "7"))
        for(i in 1..100) {
            val test = grainNumber.create().toInt()
            Assert.assertTrue(0 < test && test < 100000)
        }
    }

    @Test(expected = NumberFormatException::class)
    fun variableExceptionTest() {
        val grainNumber = GrainNumber(ColAttribute(dataType = "number", size = 5, valueType = "variable", value = "te"))
        grainNumber.create()
        Assert.assertTrue(false)
    }

    @Test
    fun variableMultipleValueTest() {

        val retList = intArrayOf(35, 53, 26, 692, 0, 1)
        val grainNumber = GrainNumber(ColAttribute(dataType = "number",
                size = 5, valueType = "variable", value = "35,53,26,692,0,1"))

        Assert.assertTrue(retList.contains(grainNumber.create().toInt()))
        Assert.assertTrue(retList.contains(grainNumber.create().toInt()))
        Assert.assertTrue(retList.contains(grainNumber.create().toInt()))
        Assert.assertTrue(retList.contains(grainNumber.create().toInt()))
    }

    @Test(expected = NumberFormatException::class)
    fun multipleValueExceptionTest() {
        val grainNumber = GrainNumber(ColAttribute(dataType = "number",
                size = 5, valueType = "variable", value = "35,34,26,test,0,1"))
        grainNumber.create()
        Assert.assertTrue(false)
    }
}