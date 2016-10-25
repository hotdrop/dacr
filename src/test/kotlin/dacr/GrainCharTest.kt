package dacr

import dacr.indata.ColAttribute
import dacr.model.GrainChar
import org.junit.Assert
import org.junit.Test

class GrainCharTest {

    @Test
    fun fixingTest() {
        var gChar = GrainChar(ColAttribute(size = 5, valueType = "fixing", value = "test"))
        Assert.assertEquals(gChar.create(), gChar.create())

        gChar = GrainChar(ColAttribute(size = 5, valueType = "fixing", value = ""))
        Assert.assertEquals(gChar.create(), "     ")
    }

    @Test
    fun fixingMultipleValueTest() {
        var gChar = GrainChar(ColAttribute(valueType = "fixing", size =5 , value = "A01,A02,B03,B04,C05"))
        Assert.assertEquals(gChar.create(), "A01  ")
        Assert.assertEquals(gChar.create(), "A02  ")
        Assert.assertEquals(gChar.create(), "B03  ")
        Assert.assertEquals(gChar.create(), "B04  ")
        Assert.assertEquals(gChar.create(), "C05  ")
        Assert.assertEquals(gChar.create(), "A01  ")
        Assert.assertEquals(gChar.create(), "A02  ")
    }

    @Test
    fun variableTest() {
        var gChar = GrainChar(ColAttribute(size = 5, valueType = "variable", value = "test "))
        val retStr = gChar.create()
        val retStr2 = gChar.create()

        Assert.assertNotEquals(retStr, "test ")
        Assert.assertNotEquals(retStr2, "test ")
        Assert.assertEquals(retStr.length, 5)
        Assert.assertEquals(retStr2.length, 5)

        gChar = GrainChar(ColAttribute(size = 6, valueType = "variable"))
        Assert.assertEquals(gChar.create().length, 6)

        gChar = GrainChar(ColAttribute(size = 12, valueType = "variable"))
        Assert.assertEquals(gChar.create().length, 12)
        Assert.assertEquals(gChar.create().length, 12)
        Assert.assertEquals(gChar.create().length, 12)

        // hasMultiByte is true
        gChar = GrainChar(ColAttribute(size = 60, valueType = "variable", hasMultiByte = true))
        Assert.assertEquals(gChar.create().length, 60)
        Assert.assertEquals(gChar.create().length, 60)
        Assert.assertEquals(gChar.create().length, 60)
    }

    @Test
    fun variableMultipleValueTest() {
        val retList = arrayListOf("A01  ", "A02  ", "B03  ", "B04  ", "C05  ")
        var gChar = GrainChar(ColAttribute(valueType = "variable", size = 5, value = "A01,A02,B03,B04,C05"))
        Assert.assertTrue(retList.contains(gChar.create()))
        Assert.assertTrue(retList.contains(gChar.create()))
    }

    @Test
    fun autoIncrementTest() {
        // empty in value field
        var gChar = GrainChar(ColAttribute(autoIncrement = true, size = 5, valueType = "variable"))
        Assert.assertEquals(gChar.create(), "1    ")
        Assert.assertEquals(gChar.create(), "2    ")

        // string(not number) in value field
        gChar = GrainChar(ColAttribute(autoIncrement = true, size = 5, valueType = "variable", value = "test"))
        Assert.assertEquals(gChar.create(), "1    ")
        Assert.assertEquals(gChar.create(), "2    ")

        // number in value field
        gChar = GrainChar(ColAttribute(autoIncrement = true, size = 5, valueType = "variable", value = "10000"))
        Assert.assertEquals(gChar.create(), "10000")
        Assert.assertEquals(gChar.create(), "10001")
        Assert.assertEquals(gChar.create(), "10002")

        // specify more than one value. These are ignored
        gChar = GrainChar(ColAttribute(autoIncrement = true, size = 5, valueType = "variable", value = "50,20,70"))
        Assert.assertEquals(gChar.create(), "1    ")
        Assert.assertEquals(gChar.create(), "2    ")
        Assert.assertEquals(gChar.create(), "3    ")
    }

    @Test
    fun zeroPaddingTest() {

        var gChar = GrainChar(ColAttribute(size = 4, format = "zeroPadding", valueType = "variable", value = ""))
        val retStr = gChar.create()
        Assert.assertEquals(retStr.length, 4)

        gChar = GrainChar(ColAttribute(size = 10, format = "zeroPadding", valueType = "variable", value = ""))
        val retStr2 = gChar.create()
        Assert.assertEquals(retStr2.length, 10)

        // fixing(no zero padding)
        gChar = GrainChar(ColAttribute(size = 10, format = "zeroPadding", valueType = "fixing", value = "test"))
        Assert.assertEquals(gChar.create(), "test      ")

        // auto increment
        gChar = GrainChar(ColAttribute(size = 5, format = "zeroPadding", autoIncrement = true, valueType = "variable", value = ""))
        Assert.assertEquals(gChar.create(), "00001")
        Assert.assertEquals(gChar.create(), "00002")

        // auto increment and specif a value
        gChar = GrainChar(ColAttribute(size = 5, format = "zeroPadding", autoIncrement = true, valueType = "variable", value = "10000"))
        Assert.assertEquals(gChar.create(), "10000")
        Assert.assertEquals(gChar.create(), "10001")
    }

    @Test(expected = IllegalStateException::class)
    fun illegalFormatTest() {
        val gChar = GrainChar(ColAttribute(size = 5, format = "zerroPPPaddgn", valueType = "variable", value = ""))
        gChar.create()
        Assert.assertTrue(false)
    }

    @Test
    fun fillMaxSizeTest() {

        var gChar = GrainChar(ColAttribute(size = 5, fillMaxSize = true, valueType = "variable", value = ""))
        val retStr = gChar.create()
        Assert.assertEquals(retStr.length, 5)
        Assert.assertEquals(gChar.create().length, 5)

        // ignore zeroPadding when fillMaxSize is true
        gChar = GrainChar(ColAttribute(size = 10, format = "zeroPadding", fillMaxSize = true, valueType = "variable", value = ""))
        Assert.assertEquals(gChar.create().length, 10)
        Assert.assertEquals(gChar.create().length, 10)

        // japanese(multi byte char)
        gChar = GrainChar(ColAttribute(size = 10, fillMaxSize = true, valueType = "variable", value = "", hasMultiByte = true))
        Assert.assertEquals(gChar.create().length, 10)
        Assert.assertEquals(gChar.create().length, 10)

        // ignore fillMaxSize and hasMultiByte when autoIncrement is true
        gChar = GrainChar(ColAttribute(size = 5, autoIncrement = true, fillMaxSize = true, valueType = "variable", value = "", hasMultiByte = true))
        Assert.assertEquals(gChar.create(), "1    ")
        Assert.assertEquals(gChar.create(), "2    ")
    }

    @Test
    fun upperCastTest() {
        var gChar = GrainChar(ColAttribute(size = 5, format = "ZEROPADDING", fillMaxSize = true, valueType = "VARIABLE", value = "0"))
        Assert.assertEquals(gChar.create().length, 5)
    }

    @Test
    fun encloseCharTest() {
        // single quotation mark
        var gChar = GrainChar(ColAttribute(size = 10, valueType = "FIXING", value = "test", encloseChar = "SingleQuotation"))
        var correctStr = "'test      '"
        Assert.assertEquals(gChar.create(), correctStr)

        gChar = GrainChar(ColAttribute(size = 10, format = "ZeroPadding", valueType = "Variable", value = "test", encloseChar = "SingleQuotation"))
        Assert.assertEquals(gChar.create().length, 12)

        gChar = GrainChar(ColAttribute(size = 10, valueType = "variable", value = "", encloseChar = "SingleQuotation"))
        Assert.assertEquals(gChar.create().length, 12)

        val retList = arrayListOf("'A01'", "'A02'", "'B03'")
        gChar = GrainChar(ColAttribute(valueType = "variable", value = "A01,A02,B03", encloseChar = "SingleQuotation"))

        Assert.assertTrue(retList.contains(gChar.create()))
        Assert.assertTrue(retList.contains(gChar.create()))
        Assert.assertTrue(retList.contains(gChar.create()))

        // double quotation mark
        gChar = GrainChar(ColAttribute(size = 10, valueType = "fixing", value = "testtest", encloseChar = "DoubleQuotation"))
        correctStr = """"testtest  """"
        Assert.assertEquals(gChar.create(), correctStr)

        gChar = GrainChar(ColAttribute(size = 10, format = "ZeroPadding", valueType = "variable", value = "", encloseChar = "DoubleQuotation"))
        Assert.assertEquals(gChar.create().length, 12)

        gChar = GrainChar(ColAttribute(size = 10, valueType = "variable", value = "", encloseChar = "DoubleQuotation"))
        Assert.assertEquals(gChar.create().length, 12)

        val retList2 = arrayListOf(""""A01  """", """"A02  """", """"B03  """")
        gChar = GrainChar(ColAttribute(size = 5, valueType = "variable", value = "A01,A02,B03", encloseChar = "DoubleQuotation"))
        Assert.assertTrue(retList2.contains(gChar.create()))
        Assert.assertTrue(retList2.contains(gChar.create()))
        Assert.assertTrue(retList2.contains(gChar.create()))
    }

    @Test(expected = IllegalStateException::class)
    fun illegalEncloseCharTest() {
        val gChar = GrainChar(ColAttribute(size = 10, valueType = "variable", value = "", encloseChar = """""""))
        gChar.create()
        Assert.assertTrue(false)
    }

}