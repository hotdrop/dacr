package dacr

import dacr.indata.ColAttribute
import dacr.model.Sphere
import org.junit.Test
import kotlin.system.measureTimeMillis

class performanceTest {

    @Test
    fun charTest() {
        var fixingList = mutableListOf<ColAttribute>()
        fixingList.add(ColAttribute(name = "col", dataType = "char", primaryKey = false, size = 20, format = "", autoIncrement = false, fillMaxSize = false, valueType = "fixing", value = "12345678901234567890", hasMultiByte = false))
        fixingList.add(ColAttribute(name = "col", dataType = "char", primaryKey = false, size = 20, format = "", autoIncrement = false, fillMaxSize = false, valueType = "fixing", value = "12345678901234567890", hasMultiByte = false))
        fixingList.add(ColAttribute(name = "col", dataType = "char", primaryKey = false, size = 20, format = "", autoIncrement = false, fillMaxSize = false, valueType = "fixing", value = "12345678901234567890", hasMultiByte = false))
        fixingList.add(ColAttribute(name = "col", dataType = "char", primaryKey = false, size = 20, format = "", autoIncrement = false, fillMaxSize = false, valueType = "fixing", value = "12345678901234567890", hasMultiByte = false))
        fixingList.add(ColAttribute(name = "col", dataType = "char", primaryKey = false, size = 20, format = "", autoIncrement = false, fillMaxSize = false, valueType = "fixing", value = "12345678901234567890", hasMultiByte = false))
        fixingList.add(ColAttribute(name = "col", dataType = "char", primaryKey = false, size = 20, format = "", autoIncrement = false, fillMaxSize = false, valueType = "fixing", value = "12345678901234567890", hasMultiByte = false))
        fixingList.add(ColAttribute(name = "col", dataType = "char", primaryKey = false, size = 20, format = "", autoIncrement = false, fillMaxSize = false, valueType = "fixing", value = "12345678901234567890", hasMultiByte = false))
        fixingList.add(ColAttribute(name = "col", dataType = "char", primaryKey = false, size = 20, format = "", autoIncrement = false, fillMaxSize = false, valueType = "fixing", value = "12345678901234567890", hasMultiByte = false))
        fixingList.add(ColAttribute(name = "col", dataType = "char", primaryKey = false, size = 20, format = "", autoIncrement = false, fillMaxSize = false, valueType = "fixing", value = "12345678901234567890", hasMultiByte = false))
        fixingList.add(ColAttribute(name = "col", dataType = "char", primaryKey = false, size = 20, format = "", autoIncrement = false, fillMaxSize = false, valueType = "fixing", value = "12345678901234567890", hasMultiByte = false))

        var varList = mutableListOf<ColAttribute>()
        varList.add(ColAttribute(name = "col", dataType = "char", primaryKey = false, size = 200, format = "", autoIncrement = false, fillMaxSize = false, valueType = "variable", value = "", hasMultiByte = false))
        varList.add(ColAttribute(name = "col", dataType = "char", primaryKey = false, size = 200, format = "", autoIncrement = false, fillMaxSize = false, valueType = "variable", value = "", hasMultiByte = false))
        varList.add(ColAttribute(name = "col", dataType = "char", primaryKey = false, size = 200, format = "", autoIncrement = false, fillMaxSize = false, valueType = "variable", value = "", hasMultiByte = false))
        varList.add(ColAttribute(name = "col", dataType = "char", primaryKey = false, size = 200, format = "", autoIncrement = false, fillMaxSize = false, valueType = "variable", value = "", hasMultiByte = false))
        varList.add(ColAttribute(name = "col", dataType = "char", primaryKey = false, size = 200, format = "", autoIncrement = false, fillMaxSize = false, valueType = "variable", value = "", hasMultiByte = false))
        varList.add(ColAttribute(name = "col", dataType = "char", primaryKey = false, size = 200, format = "", autoIncrement = false, fillMaxSize = false, valueType = "variable", value = "", hasMultiByte = false))
        varList.add(ColAttribute(name = "col", dataType = "char", primaryKey = false, size = 200, format = "", autoIncrement = false, fillMaxSize = false, valueType = "variable", value = "", hasMultiByte = false))
        varList.add(ColAttribute(name = "col", dataType = "char", primaryKey = false, size = 200, format = "", autoIncrement = false, fillMaxSize = false, valueType = "variable", value = "", hasMultiByte = false))
        varList.add(ColAttribute(name = "col", dataType = "char", primaryKey = false, size = 200, format = "", autoIncrement = false, fillMaxSize = false, valueType = "variable", value = "", hasMultiByte = false))
        varList.add(ColAttribute(name = "col", dataType = "char", primaryKey = false, size = 200, format = "", autoIncrement = false, fillMaxSize = false, valueType = "variable", value = "", hasMultiByte = false))

        var varFillList = mutableListOf<ColAttribute>()
        varFillList.add(ColAttribute(name = "col", dataType = "char", primaryKey = false, size = 200, format = "", autoIncrement = false, fillMaxSize = true, valueType = "variable", value = "", hasMultiByte = false))
        varFillList.add(ColAttribute(name = "col", dataType = "char", primaryKey = false, size = 200, format = "", autoIncrement = false, fillMaxSize = true, valueType = "variable", value = "", hasMultiByte = false))
        varFillList.add(ColAttribute(name = "col", dataType = "char", primaryKey = false, size = 200, format = "", autoIncrement = false, fillMaxSize = true, valueType = "variable", value = "", hasMultiByte = false))
        varFillList.add(ColAttribute(name = "col", dataType = "char", primaryKey = false, size = 200, format = "", autoIncrement = false, fillMaxSize = true, valueType = "variable", value = "", hasMultiByte = false))
        varFillList.add(ColAttribute(name = "col", dataType = "char", primaryKey = false, size = 200, format = "", autoIncrement = false, fillMaxSize = true, valueType = "variable", value = "", hasMultiByte = false))
        varFillList.add(ColAttribute(name = "col", dataType = "char", primaryKey = false, size = 200, format = "", autoIncrement = false, fillMaxSize = true, valueType = "variable", value = "", hasMultiByte = false))
        varFillList.add(ColAttribute(name = "col", dataType = "char", primaryKey = false, size = 200, format = "", autoIncrement = false, fillMaxSize = true, valueType = "variable", value = "", hasMultiByte = false))
        varFillList.add(ColAttribute(name = "col", dataType = "char", primaryKey = false, size = 200, format = "", autoIncrement = false, fillMaxSize = true, valueType = "variable", value = "", hasMultiByte = false))
        varFillList.add(ColAttribute(name = "col", dataType = "char", primaryKey = false, size = 200, format = "", autoIncrement = false, fillMaxSize = true, valueType = "variable", value = "", hasMultiByte = false))
        varFillList.add(ColAttribute(name = "col", dataType = "char", primaryKey = false, size = 200, format = "", autoIncrement = false, fillMaxSize = true, valueType = "variable", value = "", hasMultiByte = false))

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