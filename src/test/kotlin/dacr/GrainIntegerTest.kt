package dacr

import dacr.indata.ColAttribute
import dacr.model.GrainInteger
import org.junit.Assert
import org.junit.Test

class GrainIntegerTest {

    @Test
    fun fixingTest() {
        var gInt = GrainInteger(ColAttribute(valueType = "fixing", value = "1"))
        Assert.assertEquals(gInt.create(), "1")

        gInt = GrainInteger(ColAttribute(valueType = "fixing", value = "7", hasMultiByte = true))
        Assert.assertEquals(gInt.create(), "7")
    }

    @Test(expected = NumberFormatException::class)
    fun fixingExceptionTest() {
        val gInt = GrainInteger(ColAttribute(valueType = "fixing", value = "not Integer"))
        gInt.create()
        Assert.assertTrue(false)
    }

    @Test
    fun fixingMultipleValueTest() {
        val gInt = GrainInteger(ColAttribute(dataType = "integer", valueType = "fixing", value = "35,53,26,692,0,1"))
        Assert.assertEquals(gInt.create(), "35")
        Assert.assertEquals(gInt.create(), "53")
        Assert.assertEquals(gInt.create(), "26")
        Assert.assertEquals(gInt.create(), "692")
        Assert.assertEquals(gInt.create(), "0")
        Assert.assertEquals(gInt.create(), "1")
        Assert.assertEquals(gInt.create(), "35")
    }

    @Test
    fun autoIncrementTest() {
        var gInt = GrainInteger(ColAttribute(autoIncrement = true, valueType = "variable", value = ""))
        Assert.assertEquals(gInt.create(), "1")
        Assert.assertEquals(gInt.create(), "2")
        Assert.assertEquals(gInt.create(), "3")
        Assert.assertEquals(gInt.create(), "4")
        Assert.assertEquals(gInt.create(), "5")

        gInt = GrainInteger(ColAttribute(autoIncrement = true, valueType = "variable", value = "355"))
        Assert.assertEquals(gInt.create(), "355")
        Assert.assertEquals(gInt.create(), "356")
        Assert.assertEquals(gInt.create(), "357")
        Assert.assertEquals(gInt.create(), "358")
        Assert.assertEquals(gInt.create(), "359")

        // To begin the increment from negative number
        gInt = GrainInteger(ColAttribute( autoIncrement = true, valueType = "variable", value = "-30"))
        Assert.assertEquals(gInt.create(), "-30")
        Assert.assertEquals(gInt.create(), "-29")
        Assert.assertEquals(gInt.create(), "-28")
        Assert.assertEquals(gInt.create(), "-27")
        Assert.assertEquals(gInt.create(), "-26")

        // range
        gInt = GrainInteger(ColAttribute( autoIncrement = true, valueType = "variable", value = "23 to 25"))
        Assert.assertEquals(gInt.create(), "23")
        Assert.assertEquals(gInt.create(), "24")
        Assert.assertEquals(gInt.create(), "25")
        Assert.assertEquals(gInt.create(), "23")
        Assert.assertEquals(gInt.create(), "24")
        Assert.assertEquals(gInt.create(), "25")

        gInt = GrainInteger(ColAttribute(autoIncrement = true, size=2, valueType = "variable", value = ""))
        for(i in 1..100) {
            Assert.assertTrue(gInt.create().toInt() in 1..99)
        }
    }

    @Test
    fun variableTest() {

        var gInt = GrainInteger(ColAttribute(size = 5, valueType = "variable", value = ""))
        for(i in 1..100) {
            val test = gInt.create().toInt()
            Assert.assertTrue(test in 1..99999)
        }

        gInt = GrainInteger(ColAttribute(size = 5, valueType = "variable", value = "7"))
        for(i in 1..100) {
            val test = gInt.create().toInt()
            Assert.assertTrue(test in 1..99999)
        }

        // size exceed the maximum value of Int
        gInt = GrainInteger(ColAttribute(size = 99999999, valueType = "variable", value = ""))
        for(i in 1..10000) {
            val test = gInt.create().toInt()
            Assert.assertTrue(0 < test && test < Int.MAX_VALUE)
        }
    }

    @Test(expected = NumberFormatException::class)
    fun variableExceptionTest() {
        val gInt = GrainInteger(ColAttribute(size = 5, valueType = "variable", value = "te"))
        gInt.create()
        Assert.assertTrue(false)
    }

    @Test
    fun variableMultipleValueTest() {

        val retList = intArrayOf(35, 53, 26, 692, 0, 1)
        val gInt = GrainInteger(ColAttribute(size = 5, valueType = "variable", value = "35,53,26,692,0,1"))

        Assert.assertTrue(retList.contains(gInt.create().toInt()))
        Assert.assertTrue(retList.contains(gInt.create().toInt()))
        Assert.assertTrue(retList.contains(gInt.create().toInt()))
        Assert.assertTrue(retList.contains(gInt.create().toInt()))
    }

    @Test(expected = NumberFormatException::class)
    fun multipleValueExceptionTest() {
        val gInt = GrainInteger(ColAttribute(size = 5, valueType = "variable", value = "35,34,26,test,0,1"))
        gInt.create()
        Assert.assertTrue(false)
    }

    @Test
    fun allowBlankTest() {
        var gInt = GrainInteger(ColAttribute(autoIncrement = true, size = 5, valueType = "variable", value = " 35 "))
        Assert.assertEquals(gInt.create(), "35")
        Assert.assertEquals(gInt.create(), "36")

        val retList = intArrayOf(35, 53, 26, 692, 0, 1)
        gInt = GrainInteger(ColAttribute(size = 5, valueType = "variable", value = "35, 53, 26"))

        Assert.assertTrue(retList.contains(gInt.create().toInt()))
        Assert.assertTrue(retList.contains(gInt.create().toInt()))
        Assert.assertTrue(retList.contains(gInt.create().toInt()))
        Assert.assertTrue(retList.contains(gInt.create().toInt()))
        Assert.assertTrue(retList.contains(gInt.create().toInt()))
    }

    @Test
    fun rangeVariableTest() {
        // from positive number to positive number
        var gInt = GrainInteger(ColAttribute(valueType = "variable", value = "0 to 5"))
        for(i in 1..10) {
            Assert.assertTrue((gInt.create().toInt() in 0..5))
        }

        gInt = GrainInteger(ColAttribute(valueType = "variable", value = "250 to 300"))
        for(i in 1..100) {
            Assert.assertTrue((gInt.create().toInt() in 250..300))
        }

        // TINYINT
        gInt = GrainInteger(ColAttribute(valueType = "variable", value = "0 to 255"))
        for(i in 1..100) {
            Assert.assertTrue((gInt.create().toInt() in 0..65535))
        }

        // SMALLINT
        gInt = GrainInteger(ColAttribute(valueType = "variable", value = "0 to 65535"))
        for(i in 1..100) {
            Assert.assertTrue((gInt.create().toInt() in 0..65535))
        }

        // MEDIUMINT
        gInt = GrainInteger(ColAttribute(valueType = "variable", value = "0 to 16777215"))
        for(i in 1..100) {
            Assert.assertTrue((gInt.create().toInt() in 0..16777215))
        }

        // from negative number to positive number
        gInt = GrainInteger(ColAttribute(valueType = "variable", value = "-10 to 20"))
        for(i in 1..100) {
            val creVal = gInt.create().toInt()
            Assert.assertTrue(creVal >= -10 && creVal <= 20)
        }

        // TINYINT with signed
        gInt = GrainInteger(ColAttribute(valueType = "variable", value = "-128 to 127"))
        for(i in 1..100) {
            val creVal = gInt.create().toInt()
            Assert.assertTrue(creVal >= -128 && creVal <= 127)
        }

        // SMALLINT with singed
        gInt = GrainInteger(ColAttribute(valueType = "variable", value = "-32768 to 32767"))
        for(i in 1..100) {
            val creVal = gInt.create().toInt()
            Assert.assertTrue(creVal >= -32768 && creVal <= 32767)
        }

        // MEDIUMINT with singed
        gInt = GrainInteger(ColAttribute(valueType = "variable", value = "-8388608 to 8388607"))
        for(i in 1..100) {
            val creVal = gInt.create().toInt()
            Assert.assertTrue(creVal >= -8388608 && creVal <= 8388607)
        }

        // INT with signed
        gInt = GrainInteger(ColAttribute(valueType = "variable", value = "-2147483648 to 2147483647"))
        for(i in 1..100) {
            val creVal = gInt.create().toInt()
            Assert.assertTrue(creVal > -2147483648 && creVal < 2147483647)
        }

        // from negative number to negative number
        gInt = GrainInteger(ColAttribute(valueType = "variable", value = "-30 to -10"))
        for(i in 1..100) {
            val creVal = gInt.create().toInt()
            Assert.assertTrue(creVal >= -30 && creVal <= -10)
        }

        // exceed MAX INT VALUE
        val rangeMin = Int.MAX_VALUE - 2
        val setValue = rangeMin.toString() + " to 9999999999"
        gInt = GrainInteger(ColAttribute(valueType = "variable", value = setValue))

        for(i in 1..100) {
            val creVal = gInt.create().toInt()
            Assert.assertTrue(creVal >= rangeMin && creVal < Int.MAX_VALUE)
        }
    }
}