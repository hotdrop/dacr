package dacr

import dacr.indata.ColAttribute
import dacr.model.Sphere
import org.junit.Assert
import org.junit.Test

class SphereTest {

    @Test
    fun createTest() {
        var colAttrList = mutableListOf<ColAttribute>()
        colAttrList.add(ColAttribute(name = "col1", dataType = "char", size = 5, format = "zeroPadding",
                valueType = "variable", value = ""))
        colAttrList.add(ColAttribute(name = "col2", dataType = "char", size = 10, format = "",
                valueType = "fixing", value = "A01,A02,B03,B04,C05"))
        colAttrList.add(ColAttribute(name = "col3", dataType = "char", size = 5, format = "",
                valueType = "variable", value = ""))

        val dataSphere = Sphere(colAttrList)
        var mutableList = mutableListOf<String>()
        for(i in 1..5) {
            // 適当なデータが5レコードできれば良い
            mutableList.add(dataSphere.create().joinToString(","))
        }
        Assert.assertEquals(mutableList.size, 5)
    }

    @Test
    fun createWithPKTest() {
        var colAttrList = mutableListOf<ColAttribute>()
        colAttrList.add(ColAttribute(name = "pk1", dataType = "char", primaryKey = true,
                size = 5, valueType = "variable", value = ""))

        val dataSphere = Sphere(colAttrList)
        var duplicateMap = mutableMapOf<String, Boolean>()
        for(i in 0..9) {
            val tmpList = dataSphere.create()
            if(duplicateMap.containsKey(tmpList[0])) {
                Assert.assertFalse(true)
                break
            } else {
                duplicateMap.put(tmpList[0], true)
            }
        }
        Assert.assertTrue(true)
    }

    @Test
    fun createWithMultipleValuePKTest() {
        var colAttrList = mutableListOf<ColAttribute>()
        colAttrList.add(ColAttribute(name = "pk1", dataType = "char", primaryKey = true,
                size = 20, valueType = "variable", value = ""))
        colAttrList.add(ColAttribute(name = "pk2", dataType = "char", primaryKey = true,
                size = 5, valueType = "variable", value = "A01,A02"))
        colAttrList.add(ColAttribute(name = "pk3", dataType = "char", primaryKey = true,
                size = 5, valueType = "fixing", value = "test"))

        val dataSphere = Sphere(colAttrList)
        var duplicateMap = mutableMapOf<String, Boolean>()
        for(i in 0..9) {
            val tmpList = dataSphere.create()
            if(duplicateMap.containsKey(tmpList[0])) {
                Assert.assertFalse(true)
                break
            } else {
                duplicateMap.put(tmpList[0], true)
            }
        }
        Assert.assertTrue(true)
    }

    @Test(expected = IllegalStateException::class)
    fun illegalDataTypeTest() {
        var colAttrList = mutableListOf<ColAttribute>()
        colAttrList.add(ColAttribute(dataType = "CHAR", valueType = "variable"))
        colAttrList.add(ColAttribute(dataType = "VARCHAR", valueType = "variable"))
        colAttrList.add(ColAttribute(dataType = "DATE", valueType = "variable"))
        colAttrList.add(ColAttribute(dataType = "DATETIME", valueType = "variable"))
        colAttrList.add(ColAttribute(dataType = "TIMESTAMP", valueType = "variable"))
        colAttrList.add(ColAttribute(dataType = "UNKNOWN", valueType = "variable"))

        Sphere(colAttrList)
        Assert.assertTrue(false)
    }

    @Test(expected = IllegalStateException::class)
    fun numberOfTrialErrorTest() {
        var colAttrList = mutableListOf<ColAttribute>()
        colAttrList.add(ColAttribute(dataType = "char", primaryKey = true, valueType = "variable", value = "A01,A02"))
        val dataSphere = Sphere(colAttrList)
        for(i in 1..10) {
            dataSphere.create()
        }
        Assert.assertTrue(false)
    }
}