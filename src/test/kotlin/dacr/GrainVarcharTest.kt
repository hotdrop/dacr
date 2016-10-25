package dacr

import dacr.indata.ColAttribute
import dacr.model.GrainVarchar
import org.junit.Assert
import org.junit.Test

class GrainVarcharTest {

    @Test
    fun fixingTest() {
        var gChar = GrainVarchar(ColAttribute(dataType = "varchar", valueType = "fixing", value = "test"))
        Assert.assertEquals(gChar.create(), gChar.create())

        gChar = GrainVarchar(ColAttribute(dataType = "varchar", valueType = "fixing", value = ""))
        Assert.assertEquals(gChar.create(), "")
    }

    @Test
    fun fixingMultipleValueTest() {
        var gChar = GrainVarchar(ColAttribute(dataType = "char", valueType = "fixing",
                value = "A01,A02,B03,B04,C05"))

        Assert.assertEquals(gChar.create(), "A01")
        Assert.assertEquals(gChar.create(), "A02")
        Assert.assertEquals(gChar.create(), "B03")
        Assert.assertEquals(gChar.create(), "B04")
        Assert.assertEquals(gChar.create(), "C05")
        Assert.assertEquals(gChar.create(), "A01")
        Assert.assertEquals(gChar.create(), "A02")
    }

    @Test
    fun variableTest() {

        var gChar = GrainVarchar(ColAttribute(dataType = "char", size = 5,
                valueType = "variable", value = "test"))
        val retStr = gChar.create()
        val retStr2 = gChar.create()
        Assert.assertNotEquals(retStr, "test")
        Assert.assertNotEquals(retStr2, "test")
        Assert.assertEquals(retStr.length, 1)
        Assert.assertEquals(retStr2.length, 1)

        gChar = GrainVarchar(ColAttribute(dataType = "char", size = 6, valueType = "variable"))
        Assert.assertEquals(gChar.create().length, 2)

        gChar = GrainVarchar(ColAttribute(dataType = "char", size = 12, valueType = "variable"))
        println("variableTest singleByteStr=" + gChar.create())
        Assert.assertEquals(gChar.create().length, 4)
        Assert.assertEquals(gChar.create().length, 4)
        Assert.assertEquals(gChar.create().length, 4)

        // hasMultiByte is true
        gChar = GrainVarchar(ColAttribute(dataType = "char", size = 60,
                valueType = "variable", hasMultiByte = true))
        println("variableTest multiByteStr=" + gChar.create())
        Assert.assertEquals(gChar.create().length, 20)
        Assert.assertEquals(gChar.create().length, 20)
        Assert.assertEquals(gChar.create().length, 20)
    }

    @Test
    fun variableMultipleValueTest() {
        val retList = arrayListOf("A01", "A02", "B03", "B04", "C05")
        var gChar = GrainVarchar(ColAttribute(dataType = "char", valueType = "variable",
                value = "A01,A02,B03,B04,C05"))
        Assert.assertTrue(retList.contains(gChar.create()))
        Assert.assertTrue(retList.contains(gChar.create()))
    }

    @Test
    fun autoIncrementTest() {

        // empty in value field
        var gChar = GrainVarchar(ColAttribute(dataType = "char", autoIncrement = true,
                valueType = "variable"))
        Assert.assertEquals(gChar.create(), "1")
        Assert.assertEquals(gChar.create(), "2")

        // string(not number) in value field
        gChar = GrainVarchar(ColAttribute(dataType = "char", autoIncrement = true,
                valueType = "variable", value = "test"))
        Assert.assertEquals(gChar.create(), "1")
        Assert.assertEquals(gChar.create(), "2")

        // number in value field
        gChar = GrainVarchar(ColAttribute(dataType = "char", autoIncrement = true,
                valueType = "variable", value = "10000"))
        Assert.assertEquals(gChar.create(), "10000")
        Assert.assertEquals(gChar.create(), "10001")
        Assert.assertEquals(gChar.create(), "10002")

        // specify more than one value. These are ignored
        gChar = GrainVarchar(ColAttribute(dataType = "char", autoIncrement = true,
                valueType = "variable", value = "50,20,70"))
        Assert.assertEquals(gChar.create(), "1")
        Assert.assertEquals(gChar.create(), "2")
        Assert.assertEquals(gChar.create(), "3")
    }

    @Test
    fun zeroPaddingTest() {

        var gChar = GrainVarchar(ColAttribute(dataType = "char", size = 4, format = "zeroPadding",
                valueType = "variable", value = ""))
        val retStr = gChar.create()
        Assert.assertEquals(retStr.length, 4)

        gChar = GrainVarchar(ColAttribute(dataType = "char", size = 10, format = "zeroPadding",
                valueType = "variable", value = ""))
        val retStr2 = gChar.create()
        Assert.assertEquals(retStr2.length, 10)

        // fixing(no zero padding)
        gChar = GrainVarchar(ColAttribute(dataType = "char", size = 10, format = "zeroPadding",
                valueType = "fixing", value = "test"))
        Assert.assertEquals(gChar.create(), "test")

        // auto increment
        gChar = GrainVarchar(ColAttribute(dataType = "char", size = 5, format = "zeroPadding",
                autoIncrement = true, valueType = "variable", value = ""))
        Assert.assertEquals(gChar.create(), "00001")
        Assert.assertEquals(gChar.create(), "00002")

        // auto increment and specif a value
        gChar = GrainVarchar(ColAttribute(dataType = "char", size = 5, format = "zeroPadding",
                autoIncrement = true, valueType = "variable", value = "10000"))
        Assert.assertEquals(gChar.create(), "10000")
        Assert.assertEquals(gChar.create(), "10001")
    }

    @Test(expected = IllegalStateException::class)
    fun illegalFormatTest() {
        val gChar = GrainVarchar(ColAttribute(dataType = "char", size = 5, format = "zerroPPPaddgn",
                valueType = "variable", value = ""))
        gChar.create()
        Assert.assertTrue(false)
    }

    @Test
    fun fillMaxSizeTest() {

        var gChar = GrainVarchar(ColAttribute(dataType = "char", size = 5,
                fillMaxSize = true, valueType = "variable", value = ""))
        val retStr = gChar.create()
        Assert.assertEquals(retStr.length, 5)
        Assert.assertEquals(gChar.create().length, 5)

        // ignore zeroPadding when fillMaxSize is true
        gChar = GrainVarchar(ColAttribute(dataType = "char", size = 10, format = "zeroPadding",
                fillMaxSize = true, valueType = "variable", value = ""))
        val retStr2 = gChar.create()
        println("fillMaxSizeTest create length 10=" + retStr2)
        Assert.assertEquals(retStr2.length, 10)
        Assert.assertEquals(gChar.create().length, 10)

        // japanese(multi byte char)
        gChar = GrainVarchar(ColAttribute(dataType = "char", size = 10,
                fillMaxSize = true, valueType = "variable", value = "", hasMultiByte = true))
        val retStr3 = gChar.create()
        println("fillMaxSizeTest create japanese=" + retStr3)
        Assert.assertEquals(retStr3.length, 10)
        Assert.assertEquals(gChar.create().length, 10)

        // ignore fillMaxSize and hasMultiByte when autoIncrement is true
        gChar = GrainVarchar(ColAttribute(dataType = "char", size = 10, autoIncrement = true,
                fillMaxSize = true, valueType = "variable", value = "", hasMultiByte = true))
        Assert.assertEquals(gChar.create(), "1")
        Assert.assertEquals(gChar.create(), "2")
    }

    @Test
    fun upperCastTest() {
        var gChar = GrainVarchar(ColAttribute(dataType = "CHAR", size = 5, format = "ZEROPADDING",
                fillMaxSize = true, valueType = "VARIABLE", value = "0"))
        Assert.assertEquals(gChar.create().length, 5)
    }

    @Test
    fun encloseCharTest() {
        // single quotation mark
        var gChar = GrainVarchar(ColAttribute(dataType = "CHAR", size = 10,
                valueType = "FIXING", value = "test", encloseChar = "SingleQuotation"))
        var correctStr = "'test'"
        Assert.assertEquals(gChar.create(), correctStr)

        gChar = GrainVarchar(ColAttribute(dataType = "CHAR", size = 10, format = "ZeroPadding",
                valueType = "Variable", value = "test", encloseChar = "SingleQuotation"))
        Assert.assertEquals(gChar.create().length, 12)

        gChar = GrainVarchar(ColAttribute(dataType = "CHAR", size = 10,
                valueType = "variable", value = "", encloseChar = "SingleQuotation"))
        Assert.assertEquals(gChar.create().length, 5)

        val retList = arrayListOf("'A01'", "'A02'", "'B03'")
        gChar = GrainVarchar(ColAttribute(dataType = "char", valueType = "variable",
                value = "A01,A02,B03", encloseChar = "SingleQuotation"))

        Assert.assertTrue(retList.contains(gChar.create()))
        Assert.assertTrue(retList.contains(gChar.create()))
        Assert.assertTrue(retList.contains(gChar.create()))

        // double quotation mark
        gChar = GrainVarchar(ColAttribute(dataType = "CHAR", size = 10,
                valueType = "fixing", value = "test", encloseChar = "DoubleQuotation"))
        correctStr = """"test""""
        Assert.assertEquals(gChar.create(), correctStr)
        gChar = GrainVarchar(ColAttribute(dataType = "CHAR", size = 10, format = "ZeroPadding",
                valueType = "variable", value = "", encloseChar = "DoubleQuotation"))
        Assert.assertEquals(gChar.create().length, 12)

        gChar = GrainVarchar(ColAttribute(dataType = "CHAR", size = 10,
                valueType = "variable", value = "", encloseChar = "DoubleQuotation"))
        Assert.assertEquals(gChar.create().length, 5)

        val retList2 = arrayListOf(""""A01"""", """"A02"""", """"B03"""")
        gChar = GrainVarchar(ColAttribute(dataType = "char", valueType = "variable",
                value = "A01,A02,B03", encloseChar = "DoubleQuotation"))
        Assert.assertTrue(retList2.contains(gChar.create()))
        Assert.assertTrue(retList2.contains(gChar.create()))
        Assert.assertTrue(retList2.contains(gChar.create()))
    }
    
    @Test(expected = IllegalStateException::class)
    fun illegalEncloseCharTest() {
        val gChar = GrainVarchar(ColAttribute(dataType = "CHAR", size = 10,
                valueType = "variable", value = "", encloseChar = """""""))
        gChar.create()
        Assert.assertTrue(false)
    }

    @Test
    fun varchar2Test() {
        var gChar = GrainVarchar(ColAttribute(dataType = "varchar2", size = 10, format = "",
                fillMaxSize = true, valueType = "VARIABLE", value = ""))
        Assert.assertEquals(gChar.create().length, 10)
        gChar = GrainVarchar(ColAttribute(dataType = "varChar2", size = 20, format = "",
                fillMaxSize = true, valueType = "VARIABLE", value = ""))
        Assert.assertEquals(gChar.create().length, 20)
    }
}