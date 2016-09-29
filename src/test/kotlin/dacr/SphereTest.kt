package dacr

import dacr.indata.ColAttribute
import dacr.model.Sphere
import org.junit.Assert
import org.junit.Test

class SphereTest {

    @Test
    fun createTest() {
        var colAttrList = mutableListOf<ColAttribute>()
        colAttrList.add(ColAttribute(name = "pk1", dataType = "char", primaryKey = false,
                size = 5, format = "zeroPadding", autoIncrement = false, fillMaxSize = false,
                valueType = "variable", value = "", hasMultiByte = false))
        colAttrList.add(ColAttribute(name = "pk2", dataType = "char", primaryKey = false,
                size = 10, format = "", autoIncrement = false, fillMaxSize = false,
                valueType = "fixing", value = "A01,A02,B03,B04,C05", hasMultiByte = false))
        colAttrList.add(ColAttribute(name = "create_code", dataType = "char", primaryKey = false,
                size = 5, format = "", autoIncrement = false, fillMaxSize = false,
                valueType = "variable", value = "", hasMultiByte = false))

        val dataSphere = Sphere(colAttrList)
        for(i in 0..5) {
            // 適当なデータが5レコードできれば良い
            println(dataSphere.create())
        }
        Assert.assertTrue(true)
    }

    @Test
    fun createWithPKTest() {
        var colAttrList = mutableListOf<ColAttribute>()
        colAttrList.add(ColAttribute(name = "pk1", dataType = "char", primaryKey = true,
                size = 5, format = "", autoIncrement = false, fillMaxSize = false,
                valueType = "variable", value = "", hasMultiByte = false))

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
}