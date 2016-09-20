package dacr

import dacr.indata.RecordAttributeJson
import junit.framework.Assert

class RecordPropertyTest {

    @org.junit.Test
    fun readJson() {
        val path = System.getProperty("user.dir") + "/src/test/kotlin/dacr/testdata/sample.json"
        var attr = RecordAttributeJson(path).parse()
        Assert.assertEquals(attr.columnName, "test")
    }
}