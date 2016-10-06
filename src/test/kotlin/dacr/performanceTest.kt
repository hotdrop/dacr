package dacr

import dacr.indata.ColAttribute
import dacr.model.Sphere
import org.junit.Assert
import org.junit.Test
import kotlin.system.measureTimeMillis

class performanceTest {

    @Test
    fun charTest() {
        var fixingList = mutableListOf<ColAttribute>()
        for(i in 1..10) {
            fixingList.add(ColAttribute(dataType = "char", size = 20, valueType = "fixing", value = "12345678901234567890"))
        }

        var varList = mutableListOf<ColAttribute>()
        for(i in 1..10) {
            varList.add(ColAttribute(dataType = "char", size = 200, valueType = "variable", value = ""))
        }

        var varFillList = mutableListOf<ColAttribute>()
        for(i in 1..10) {
            varFillList.add(ColAttribute(dataType = "char", size = 200, fillMaxSize = true, valueType = "variable", value = ""))
        }

        var varEncloseMarkList = mutableListOf<ColAttribute>()
        for(i in 1..10) {
            varEncloseMarkList.add(ColAttribute(dataType = "char", size = 200, valueType = "variable", value = "", encloseChar = ColAttribute.ENCLOSE_CHAR_DOUBLE_QUOTATION))
        }

        // These Test TimeValues on Mac Book Pro Retina
        //  OS: OS X El Capitan
        //  CPU: Intel Core i5 2.7GHx
        //  Memory: 8GB 1867 MHz DDR3
        val fixingTimeAvg = benchMark(fixingList)
        println("fixing time avg:" + fixingTimeAvg)
        Assert.assertTrue(fixingTimeAvg < 100)

        val variableTimeAvg = benchMark(varList)
        println("variable time avg:" + variableTimeAvg)
        Assert.assertTrue(fixingTimeAvg < 1500)

        val charEncloseTimeAvg = benchMark(varEncloseMarkList)
        println("char enclose time avg:" + charEncloseTimeAvg)
        Assert.assertTrue(fixingTimeAvg < 1500)

        val fillMaxTimeAvg = benchMark(varFillList)
        println("fill max time avg:" + fillMaxTimeAvg)
        Assert.assertTrue(fixingTimeAvg < 4000)
    }

    private fun benchMark(list: List<ColAttribute>): Long {
        val dataSphere = Sphere(list)
        return measureTimeMillis {
            for(i in 1..100000) {
                dataSphere.create()
            }
        }
    }
}