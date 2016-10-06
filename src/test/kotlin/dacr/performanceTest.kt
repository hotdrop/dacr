package dacr

import dacr.indata.ColAttribute
import dacr.model.Sphere
import org.junit.Test
import kotlin.system.measureTimeMillis

class performanceTest {

    @Test
    fun charTest() {
        var fixingList = mutableListOf<ColAttribute>()
        fixingList.add(ColAttribute(dataType = "char", size = 20, valueType = "fixing", value = "12345678901234567890"))
        fixingList.add(ColAttribute(dataType = "char", size = 20, valueType = "fixing", value = "12345678901234567890"))
        fixingList.add(ColAttribute(dataType = "char", size = 20, valueType = "fixing", value = "12345678901234567890"))
        fixingList.add(ColAttribute(dataType = "char", size = 20, valueType = "fixing", value = "12345678901234567890"))
        fixingList.add(ColAttribute(dataType = "char", size = 20, valueType = "fixing", value = "12345678901234567890"))
        fixingList.add(ColAttribute(dataType = "char", size = 20, valueType = "fixing", value = "12345678901234567890"))
        fixingList.add(ColAttribute(dataType = "char", size = 20, valueType = "fixing", value = "12345678901234567890"))
        fixingList.add(ColAttribute(dataType = "char", size = 20, valueType = "fixing", value = "12345678901234567890"))
        fixingList.add(ColAttribute(dataType = "char", size = 20, valueType = "fixing", value = "12345678901234567890"))
        fixingList.add(ColAttribute(dataType = "char", size = 20, valueType = "fixing", value = "12345678901234567890"))

        var varList = mutableListOf<ColAttribute>()
        varList.add(ColAttribute(dataType = "char", size = 200, valueType = "variable", value = ""))
        varList.add(ColAttribute(dataType = "char", size = 200, valueType = "variable", value = ""))
        varList.add(ColAttribute(dataType = "char", size = 200, valueType = "variable", value = ""))
        varList.add(ColAttribute(dataType = "char", size = 200, valueType = "variable", value = ""))
        varList.add(ColAttribute(dataType = "char", size = 200, valueType = "variable", value = ""))
        varList.add(ColAttribute(dataType = "char", size = 200, valueType = "variable", value = ""))
        varList.add(ColAttribute(dataType = "char", size = 200, valueType = "variable", value = ""))
        varList.add(ColAttribute(dataType = "char", size = 200, valueType = "variable", value = ""))
        varList.add(ColAttribute(dataType = "char", size = 200, valueType = "variable", value = ""))
        varList.add(ColAttribute(dataType = "char", size = 200, valueType = "variable", value = ""))

        var varFillList = mutableListOf<ColAttribute>()
        varFillList.add(ColAttribute(dataType = "char", size = 200, fillMaxSize = true, valueType = "variable", value = ""))
        varFillList.add(ColAttribute(dataType = "char", size = 200, fillMaxSize = true, valueType = "variable", value = ""))
        varFillList.add(ColAttribute(dataType = "char", size = 200, fillMaxSize = true, valueType = "variable", value = ""))
        varFillList.add(ColAttribute(dataType = "char", size = 200, fillMaxSize = true, valueType = "variable", value = ""))
        varFillList.add(ColAttribute(dataType = "char", size = 200, fillMaxSize = true, valueType = "variable", value = ""))
        varFillList.add(ColAttribute(dataType = "char", size = 200, fillMaxSize = true, valueType = "variable", value = ""))
        varFillList.add(ColAttribute(dataType = "char", size = 200, fillMaxSize = true, valueType = "variable", value = ""))
        varFillList.add(ColAttribute(dataType = "char", size = 200, fillMaxSize = true, valueType = "variable", value = ""))
        varFillList.add(ColAttribute(dataType = "char", size = 200, fillMaxSize = true, valueType = "variable", value = ""))
        varFillList.add(ColAttribute(dataType = "char", size = 200, fillMaxSize = true, valueType = "variable", value = ""))

        println("fixing time avg:" + benchMark(fixingList))
        println("variable time avg:" + benchMark(varList))
        println("fill max time avg:" + benchMark(varFillList))
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