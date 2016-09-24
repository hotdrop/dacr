package dacr

import dacr.indata.ColAttribute
import dacr.model.GrainChar
import org.junit.Assert

class GrainCharTest {

    @org.junit.Test
    fun fixingTest() {

        val grainChar = GrainChar(ColAttribute(name = "normal", dataType = "char", primaryKey = false,
                size = 5, format = "none", autoIncrement = false, fillMaxSize = false,
                valueType = "fixing", value = "test", hasMultiByte = false))

        val retStr = grainChar.create()
        val retStr2 = grainChar.create()

        Assert.assertEquals(retStr, retStr2)
    }

    @org.junit.Test
    fun variableNormalTest() {

        var grainChar = GrainChar(ColAttribute(name = "normal", dataType = "char", primaryKey = false,
                size = 5, format = "none", autoIncrement = false, fillMaxSize = false,
                valueType = "variable", value = "test", hasMultiByte = false))

        val retStr = grainChar.create()
        val retStr2 = grainChar.create()

        Assert.assertNotEquals(retStr, "test")
        Assert.assertNotEquals(retStr2, "test")
        Assert.assertEquals(retStr.length, 1)
        Assert.assertEquals(retStr2.length, 1)

        grainChar = GrainChar(ColAttribute(name = "normal", dataType = "char", primaryKey = false,
                size = 6, format = "none", autoIncrement = false, fillMaxSize = false,
                valueType = "variable", value = "", hasMultiByte = false))
        Assert.assertEquals(grainChar.create().length, 2)

        grainChar = GrainChar(ColAttribute(name = "normal", dataType = "char", primaryKey = false,
                size = 60, format = "none", autoIncrement = false, fillMaxSize = false,
                valueType = "variable", value = "", hasMultiByte = false))
        println("normalTest singleByteStr20=" + grainChar.create())
        Assert.assertEquals(grainChar.create().length, 20)
        Assert.assertEquals(grainChar.create().length, 20)
        Assert.assertEquals(grainChar.create().length, 20)

        grainChar = GrainChar(ColAttribute(name = "normal", dataType = "char", primaryKey = false,
                size = 60, format = "none", autoIncrement = false, fillMaxSize = false,
                valueType = "variable", value = "", hasMultiByte = true))
        println("normalTest multiByteStr20=" + grainChar.create())
        Assert.assertEquals(grainChar.create().length, 20)
        Assert.assertEquals(grainChar.create().length, 20)
        Assert.assertEquals(grainChar.create().length, 20)
    }

    @org.junit.Test
    fun autoIncrementTest() {

        // empty in value field
        var grainChar = GrainChar(ColAttribute(name = "normal", dataType = "char", primaryKey = false,
                size = 5, format = "none", autoIncrement = true, fillMaxSize = false,
                valueType = "variable", value = "", hasMultiByte = false))

        Assert.assertEquals(grainChar.create(), "1")
        Assert.assertEquals(grainChar.create(), "2")

        // string(not number) in value field
        grainChar = GrainChar(ColAttribute(name = "normal", dataType = "char", primaryKey = false,
                size = 5, format = "none", autoIncrement = true, fillMaxSize = false,
                valueType = "variable", value = "test", hasMultiByte = false))

        Assert.assertEquals(grainChar.create(), "1")
        Assert.assertEquals(grainChar.create(), "2")

        // number in value field
        grainChar = GrainChar(ColAttribute(name = "normal", dataType = "char", primaryKey = false,
                size = 5, format = "none", autoIncrement = true, fillMaxSize = false,
                valueType = "variable", value = "10000", hasMultiByte = false))

        Assert.assertEquals(grainChar.create(), "10000")
        Assert.assertEquals(grainChar.create(), "10001")
        Assert.assertEquals(grainChar.create(), "10002")
    }

    @org.junit.Test
    fun zeroPaddingTest() {
        var grainChar = GrainChar(ColAttribute(name = "normal", dataType = "char", primaryKey = false,
                size = 4, format = "zeroPadding", autoIncrement = false, fillMaxSize = false,
                valueType = "variable", value = "", hasMultiByte = false))

        val retStr = grainChar.create()
        println("zeroPaddingTest length4=" + retStr)
        Assert.assertEquals(retStr.length, 4)

        grainChar = GrainChar(ColAttribute(name = "normal", dataType = "char", primaryKey = false,
                size = 10, format = "zeroPadding", autoIncrement = false, fillMaxSize = false,
                valueType = "variable", value = "", hasMultiByte = false))

        val retStr2 = grainChar.create()
        println("zeroPaddingTest length10=" + retStr2)
        Assert.assertEquals(retStr2.length, 10)

        // fixing(no zero padding)
        grainChar = GrainChar(ColAttribute(name = "normal", dataType = "char", primaryKey = false,
                size = 10, format = "zeroPadding", autoIncrement = false, fillMaxSize = false,
                valueType = "fixing", value = "test", hasMultiByte = false))

        Assert.assertEquals(grainChar.create(), "test")

        // auto increment
        grainChar = GrainChar(ColAttribute(name = "normal", dataType = "char", primaryKey = false,
                size = 5, format = "zeroPadding", autoIncrement = true, fillMaxSize = false,
                valueType = "variable", value = "", hasMultiByte = false))

        Assert.assertEquals(grainChar.create(), "00001")
        Assert.assertEquals(grainChar.create(), "00002")

        grainChar = GrainChar(ColAttribute(name = "normal", dataType = "char", primaryKey = false,
                size = 5, format = "zeroPadding", autoIncrement = true, fillMaxSize = false,
                valueType = "variable", value = "10000", hasMultiByte = false))

        Assert.assertEquals(grainChar.create(), "10000")
        Assert.assertEquals(grainChar.create(), "10001")
    }

    @org.junit.Test
    fun fillMaxSizeTest() {
        var grainChar = GrainChar(ColAttribute(name = "normal", dataType = "char", primaryKey = false,
                size = 5, format = "none", autoIncrement = false, fillMaxSize = true,
                valueType = "variable", value = "", hasMultiByte = false))

        val retStr = grainChar.create()
        println("fillMaxSizeTest length5=" + retStr)
        Assert.assertEquals(retStr.length, 5)
        Assert.assertEquals(grainChar.create().length, 5)

        // ignore zeroPadding when fillMaxSize is true
        grainChar = GrainChar(ColAttribute(name = "normal", dataType = "char", primaryKey = false,
                size = 10, format = "zeroPadding", autoIncrement = false, fillMaxSize = true,
                valueType = "variable", value = "", hasMultiByte = false))

        val retStr2 = grainChar.create()
        println("fillMaxSizeTest length10=" + retStr2)
        Assert.assertEquals(retStr2.length, 10)
        Assert.assertEquals(grainChar.create().length, 10)

        // japanese(multi byte char)
        grainChar = GrainChar(ColAttribute(name = "normal", dataType = "char", primaryKey = false,
                size = 10, format = "", autoIncrement = false, fillMaxSize = true,
                valueType = "variable", value = "", hasMultiByte = true))

        val retStr3 = grainChar.create()
        println("fillMaxSizeTest japanese=" + retStr3)
        Assert.assertEquals(retStr3.length, 10)
        Assert.assertEquals(grainChar.create().length, 10)

        // ignore fillMaxSize and hasMultiByte when autoIncrement is true
        grainChar = GrainChar(ColAttribute(name = "normal", dataType = "char", primaryKey = false,
                size = 10, format = "", autoIncrement = true, fillMaxSize = true,
                valueType = "variable", value = "", hasMultiByte = true))

        Assert.assertEquals(grainChar.create(), "1")
        Assert.assertEquals(grainChar.create(), "2")
    }
}