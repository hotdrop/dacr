package dacr

import dacr.indata.ColAttribute
import dacr.model.GrainChar
import org.junit.Assert
import org.junit.Test

class GrainCharTest {

    @Test
    fun fixingTest() {

        var grainChar = GrainChar(ColAttribute(dataType = "char", size = 5,
                valueType = "fixing", value = "test"))
        Assert.assertEquals(grainChar.create(), grainChar.create())

        grainChar = GrainChar(ColAttribute(dataType = "char", size = 5, valueType = "fixing", value = ""))
        Assert.assertEquals(grainChar.create(), "     ")
    }

    @Test
    fun fixingMultipleValueTest() {
        var grainChar = GrainChar(ColAttribute(dataType = "char", valueType = "fixing", size =5 ,
                        value = "A01,A02,B03,B04,C05"))

        Assert.assertEquals(grainChar.create(), "A01  ")
        Assert.assertEquals(grainChar.create(), "A02  ")
        Assert.assertEquals(grainChar.create(), "B03  ")
        Assert.assertEquals(grainChar.create(), "B04  ")
        Assert.assertEquals(grainChar.create(), "C05  ")
        Assert.assertEquals(grainChar.create(), "A01  ")
        Assert.assertEquals(grainChar.create(), "A02  ")
    }

    @Test
    fun variableTest() {

        var grainChar = GrainChar(ColAttribute(dataType = "char", size = 5,
                valueType = "variable", value = "test "))
        val retStr = grainChar.create()
        val retStr2 = grainChar.create()
        Assert.assertNotEquals(retStr, "test ")
        Assert.assertNotEquals(retStr2, "test ")
        Assert.assertEquals(retStr.length, 5)
        Assert.assertEquals(retStr2.length, 5)

        grainChar = GrainChar(ColAttribute(dataType = "char", size = 6, valueType = "variable"))
        Assert.assertEquals(grainChar.create().length, 6)

        grainChar = GrainChar(ColAttribute(dataType = "char", size = 12, valueType = "variable"))
        println("variableTest singleByteStr=" + grainChar.create())
        Assert.assertEquals(grainChar.create().length, 12)
        Assert.assertEquals(grainChar.create().length, 12)
        Assert.assertEquals(grainChar.create().length, 12)

        // hasMultiByte is true
        grainChar = GrainChar(ColAttribute(dataType = "char", size = 60,
                valueType = "variable", hasMultiByte = true))
        println("variableTest multiByteStr=" + grainChar.create())
        Assert.assertEquals(grainChar.create().length, 60)
        Assert.assertEquals(grainChar.create().length, 60)
        Assert.assertEquals(grainChar.create().length, 60)
    }

    @Test
    fun variableMultipleValueTest() {
        val retList = arrayListOf("A01  ", "A02  ", "B03  ", "B04  ", "C05  ")
        var grainChar = GrainChar(ColAttribute(dataType = "char", valueType = "variable", size = 5,
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
        grainChar = GrainChar(ColAttribute(dataType = "char", autoIncrement = true, size = 5,
                valueType = "variable", value = "test"))
        Assert.assertEquals(grainChar.create(), "1    ")
        Assert.assertEquals(grainChar.create(), "2    ")

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
        Assert.assertEquals(grainChar.create(), "test      ")

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

    @Test(expected = IllegalStateException::class)
    fun illegalFormatTest() {
        val gChar = GrainChar(ColAttribute(dataType = "char", size = 5, format = "zerroPPPaddgn",
                valueType = "variable", value = ""))
        gChar.create()
        Assert.assertTrue(false)
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
        grainChar = GrainChar(ColAttribute(dataType = "char", size = 5, autoIncrement = true,
                fillMaxSize = true, valueType = "variable", value = "", hasMultiByte = true))
        Assert.assertEquals(grainChar.create(), "1    ")
        Assert.assertEquals(grainChar.create(), "2    ")
    }

    @Test
    fun upperCastTest() {
        var grainChar = GrainChar(ColAttribute(dataType = "CHAR", size = 5, format = "ZEROPADDING",
                fillMaxSize = true, valueType = "VARIABLE", value = "0"))
        Assert.assertEquals(grainChar.create().length, 5)
    }

    @Test
    fun encloseCharTest() {
        // single quotation mark
        var grainChar = GrainChar(ColAttribute(dataType = "CHAR", size = 10,
                valueType = "FIXING", value = "test", encloseChar = "SingleQuotation"))
        var correctStr = "'test      '"
        Assert.assertEquals(grainChar.create(), correctStr)

        grainChar = GrainChar(ColAttribute(dataType = "CHAR", size = 10, format = "ZeroPadding",
                valueType = "Variable", value = "test", encloseChar = "SingleQuotation"))
        Assert.assertEquals(grainChar.create().length, 12)

        grainChar = GrainChar(ColAttribute(dataType = "CHAR", size = 10,
                valueType = "variable", value = "", encloseChar = "SingleQuotation"))
        Assert.assertEquals(grainChar.create().length, 12)

        val retList = arrayListOf("'A01'", "'A02'", "'B03'")
        grainChar = GrainChar(ColAttribute(dataType = "char", valueType = "variable",
                value = "A01,A02,B03", encloseChar = "SingleQuotation"))

        Assert.assertTrue(retList.contains(grainChar.create()))
        Assert.assertTrue(retList.contains(grainChar.create()))
        Assert.assertTrue(retList.contains(grainChar.create()))

        // double quotation mark
        grainChar = GrainChar(ColAttribute(dataType = "CHAR", size = 10,
                valueType = "fixing", value = "testtest", encloseChar = "DoubleQuotation"))
        correctStr = """"testtest  """"
        Assert.assertEquals(grainChar.create(), correctStr)

        grainChar = GrainChar(ColAttribute(dataType = "CHAR", size = 10, format = "ZeroPadding",
                valueType = "variable", value = "", encloseChar = "DoubleQuotation"))
        Assert.assertEquals(grainChar.create().length, 12)

        grainChar = GrainChar(ColAttribute(dataType = "CHAR", size = 10,
                valueType = "variable", value = "", encloseChar = "DoubleQuotation"))
        Assert.assertEquals(grainChar.create().length, 12)

        val retList2 = arrayListOf(""""A01"""", """"A02"""", """"B03"""")
        grainChar = GrainChar(ColAttribute(dataType = "char", valueType = "variable",
                value = "A01,A02,B03", encloseChar = "DoubleQuotation"))
        Assert.assertTrue(retList2.contains(grainChar.create()))
        Assert.assertTrue(retList2.contains(grainChar.create()))
        Assert.assertTrue(retList2.contains(grainChar.create()))
    }

    @Test(expected = IllegalStateException::class)
    fun illegalEncloseCharTest() {
        val gChar = GrainChar(ColAttribute(dataType = "CHAR", size = 10,
                valueType = "variable", value = "", encloseChar = """""""))
        gChar.create()
        Assert.assertTrue(false)
    }

}