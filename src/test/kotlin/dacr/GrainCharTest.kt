package dacr

import dacr.indata.ColAttribute
import dacr.model.GrainChar
import org.junit.Assert
import org.junit.Test

class GrainCharTest {

    @Test
    fun fixingTest() {

        var grainChar = GrainChar(ColAttribute(dataType = "char",
                valueType = "fixing", value = "test"))
        val retStr = grainChar.create()
        val retStr2 = grainChar.create()
        Assert.assertEquals(retStr, retStr2)

        grainChar = GrainChar(ColAttribute(dataType = "char", valueType = "fixing", value = ""))
        val retStr3 = grainChar.create()
        Assert.assertEquals(retStr3, "")
    }

    @Test
    fun fixingMultipleValueTest() {
        var grainChar = GrainChar(ColAttribute(dataType = "char", valueType = "fixing",
                        value = "A01,A02,B03,B04,C05"))

        Assert.assertEquals(grainChar.create(), "A01")
        Assert.assertEquals(grainChar.create(), "A02")
        Assert.assertEquals(grainChar.create(), "B03")
        Assert.assertEquals(grainChar.create(), "B04")
        Assert.assertEquals(grainChar.create(), "C05")
        Assert.assertEquals(grainChar.create(), "A01")
        Assert.assertEquals(grainChar.create(), "A02")
    }

    @Test
    fun variableTest() {

        var grainChar = GrainChar(ColAttribute(dataType = "char", size = 5,
                valueType = "variable", value = "test"))
        val retStr = grainChar.create()
        val retStr2 = grainChar.create()
        Assert.assertNotEquals(retStr, "test")
        Assert.assertNotEquals(retStr2, "test")
        Assert.assertEquals(retStr.length, 1)
        Assert.assertEquals(retStr2.length, 1)

        grainChar = GrainChar(ColAttribute(dataType = "char", size = 6, valueType = "variable"))
        Assert.assertEquals(grainChar.create().length, 2)

        grainChar = GrainChar(ColAttribute(dataType = "char", size = 12, valueType = "variable"))
        println("variableTest singleByteStr=" + grainChar.create())
        Assert.assertEquals(grainChar.create().length, 4)
        Assert.assertEquals(grainChar.create().length, 4)
        Assert.assertEquals(grainChar.create().length, 4)

        // hasMultiByte is true
        grainChar = GrainChar(ColAttribute(dataType = "char", size = 60,
                valueType = "variable", hasMultiByte = true))
        println("variableTest multiByteStr=" + grainChar.create())
        Assert.assertEquals(grainChar.create().length, 20)
        Assert.assertEquals(grainChar.create().length, 20)
        Assert.assertEquals(grainChar.create().length, 20)
    }

    @Test
    fun variableMultipleValueTest() {
        val retList = arrayListOf("A01", "A02", "B03", "B04", "C05")
        var grainChar = GrainChar(ColAttribute(dataType = "char", valueType = "variable",
                value = "A01,A02,B03,B04,C05"))
        Assert.assertTrue(retList.contains(grainChar.create()))
        Assert.assertTrue(retList.contains(grainChar.create()))
    }

    @Test
    fun autoIncrementTest() {

        // empty in value field
        var grainChar = GrainChar(ColAttribute(dataType = "char", autoIncrement = true,
                valueType = "variable"))
        Assert.assertEquals(grainChar.create(), "1")
        Assert.assertEquals(grainChar.create(), "2")

        // string(not number) in value field
        grainChar = GrainChar(ColAttribute(dataType = "char", autoIncrement = true,
                valueType = "variable", value = "test"))
        Assert.assertEquals(grainChar.create(), "1")
        Assert.assertEquals(grainChar.create(), "2")

        // number in value field
        grainChar = GrainChar(ColAttribute(dataType = "char", autoIncrement = true,
                valueType = "variable", value = "10000"))
        Assert.assertEquals(grainChar.create(), "10000")
        Assert.assertEquals(grainChar.create(), "10001")
        Assert.assertEquals(grainChar.create(), "10002")

        // specify more than one value. These are ignored
        grainChar = GrainChar(ColAttribute(dataType = "char", autoIncrement = true,
                valueType = "variable", value = "50,20,70"))
        Assert.assertEquals(grainChar.create(), "1")
        Assert.assertEquals(grainChar.create(), "2")
        Assert.assertEquals(grainChar.create(), "3")
    }

    @Test
    fun zeroPaddingTest() {

        var grainChar = GrainChar(ColAttribute(dataType = "char", size = 4, format = "zeroPadding",
                valueType = "variable", value = ""))
        val retStr = grainChar.create()
        Assert.assertEquals(retStr.length, 4)

        grainChar = GrainChar(ColAttribute(dataType = "char", size = 10, format = "zeroPadding",
                valueType = "variable", value = ""))
        val retStr2 = grainChar.create()
        Assert.assertEquals(retStr2.length, 10)

        // fixing(no zero padding)
        grainChar = GrainChar(ColAttribute(dataType = "char", size = 10, format = "zeroPadding",
                valueType = "fixing", value = "test"))
        Assert.assertEquals(grainChar.create(), "test")

        // auto increment
        grainChar = GrainChar(ColAttribute(dataType = "char", size = 5, format = "zeroPadding",
                autoIncrement = true, valueType = "variable", value = ""))
        Assert.assertEquals(grainChar.create(), "00001")
        Assert.assertEquals(grainChar.create(), "00002")

        // auto increment and specif a value
        grainChar = GrainChar(ColAttribute(dataType = "char", size = 5, format = "zeroPadding",
                autoIncrement = true, valueType = "variable", value = "10000"))
        Assert.assertEquals(grainChar.create(), "10000")
        Assert.assertEquals(grainChar.create(), "10001")
    }

    @Test
    fun fillMaxSizeTest() {

        var grainChar = GrainChar(ColAttribute(dataType = "char", size = 5,
                fillMaxSize = true, valueType = "variable", value = ""))
        val retStr = grainChar.create()
        Assert.assertEquals(retStr.length, 5)
        Assert.assertEquals(grainChar.create().length, 5)

        // ignore zeroPadding when fillMaxSize is true
        grainChar = GrainChar(ColAttribute(dataType = "char", size = 10, format = "zeroPadding",
                fillMaxSize = true, valueType = "variable", value = ""))
        val retStr2 = grainChar.create()
        println("fillMaxSizeTest create length 10=" + retStr2)
        Assert.assertEquals(retStr2.length, 10)
        Assert.assertEquals(grainChar.create().length, 10)

        // japanese(multi byte char)
        grainChar = GrainChar(ColAttribute(dataType = "char", size = 10,
                fillMaxSize = true, valueType = "variable", value = "", hasMultiByte = true))
        val retStr3 = grainChar.create()
        println("fillMaxSizeTest create japanese=" + retStr3)
        Assert.assertEquals(retStr3.length, 10)
        Assert.assertEquals(grainChar.create().length, 10)

        // ignore fillMaxSize and hasMultiByte when autoIncrement is true
        grainChar = GrainChar(ColAttribute(dataType = "char", size = 10, autoIncrement = true,
                fillMaxSize = true, valueType = "variable", value = "", hasMultiByte = true))
        Assert.assertEquals(grainChar.create(), "1")
        Assert.assertEquals(grainChar.create(), "2")
    }

    @Test
    fun upperCastTest() {

        var grainChar = GrainChar(ColAttribute(dataType = "CHAR", size = 5, format = "ZEROPADDING",
                fillMaxSize = true, valueType = "FIXING", value = "0"))
        val retStr = grainChar.create()
        Assert.assertEquals(retStr.length, 1)
    }
}