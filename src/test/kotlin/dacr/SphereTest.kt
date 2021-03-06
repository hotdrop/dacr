package dacr

import dacr.indata.ColAttribute
import dacr.model.Sphere
import org.junit.Assert
import org.junit.Test

class SphereTest {

    @Test
    fun createTest() {
        val colAttrList = mutableListOf<ColAttribute>()
        colAttrList.add(ColAttribute(name = "col1", dataType = "char", size = 5, format = "zeroPadding",
                valueType = "variable", value = ""))
        colAttrList.add(ColAttribute(name = "col2", dataType = "varchar", size = 10, format = "",
                valueType = "fixing", value = "A01,A02,B03,B04,C05"))
        colAttrList.add(ColAttribute(name = "col3", dataType = "varchar", size = 5, format = "",
                valueType = "variable", value = ""))

        val dataSphere = Sphere(colAttrList)
        val mutableList = mutableListOf<String>()
        for(i in 1..5) {
            mutableList.add(dataSphere.create().joinToString(","))
        }
        Assert.assertEquals(mutableList.size, 5)
    }

    @Test
    fun createWithPKTest() {
        val colAttrList = mutableListOf<ColAttribute>()
        colAttrList.add(ColAttribute(name = "pk1", dataType = "char", primaryKey = true,
                size = 5, valueType = "variable", value = ""))

        val dataSphere = Sphere(colAttrList)
        val duplicateMap = mutableMapOf<String, Boolean>()
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
        val colAttrList = mutableListOf<ColAttribute>()
        // this attribute is primary key
        colAttrList.add(ColAttribute(name = "pk1", dataType = "char", primaryKey = true, size = 1, valueType = "variable", value = ""))
        // this attribute also is primary key
        colAttrList.add(ColAttribute(name = "pk2", dataType = "varchar", primaryKey = true, valueType = "variable", value = "A01,A02"))
        // this attribute is not key. because valueType is fixing
        colAttrList.add(ColAttribute(name = "pk3", dataType = "varchar", primaryKey = true, valueType = "fixing", value = "test"))

        val dataSphere = Sphere(colAttrList)
        val duplicateMap = mutableMapOf<String, Boolean>()
        for(i in 0..9) {
            val tmpList = dataSphere.create()
            val key = tmpList[0] + tmpList[1]
            if(duplicateMap.containsKey(key)) {
                Assert.assertFalse(true)
                break
            } else {
                duplicateMap.put(key, true)
            }
        }
        Assert.assertTrue(true)
    }

    @Test(expected = IllegalStateException::class)
    fun illegalDataTypeTest() {
        val colAttrList = mutableListOf<ColAttribute>()
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
        val colAttrList = mutableListOf<ColAttribute>()
        colAttrList.add(ColAttribute(dataType = "char", primaryKey = true, valueType = "variable", value = "A01,A02"))
        val dataSphere = Sphere(colAttrList)
        for(i in 1..10) {
            dataSphere.create()
        }
        Assert.assertTrue(false)
    }
}