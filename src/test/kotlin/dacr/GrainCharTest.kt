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

        val grainChar = GrainChar(ColAttribute(name = "normal", dataType = "char", primaryKey = false,
                size = 5, format = "none", autoIncrement = false, fillMaxSize = false,
                valueType = "variable", value = "test", hasMultiByte = false))

        val retStr = grainChar.create()
        val retStr2 = grainChar.create()

        Assert.assertNotEquals(retStr, "test")
        Assert.assertNotEquals(retStr2, "test")
        Assert.assertEquals(retStr.length, 1)
        Assert.assertEquals(retStr2.length, 1)
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

        Assert.assertEquals(grainChar.create(), "10001")
        Assert.assertEquals(grainChar.create(), "10002")
    }

    // TODO size test
    // TODO fillMaxSize test
    // TODO valueEmpty test
    // TODO hasMultiByte test
    // TODO complex condition test
}