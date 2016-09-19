package dacr

import dacr.model.RecordProperty
import junit.framework.Assert

class RecordPropertyTest {

    @org.junit.Test
    fun readJson() {
        val path = System.getProperty("user.dir") + "/src/test/kotlin/dacr/testdata/sample.json"
        var rp = RecordProperty(path)
        Assert.assertEquals(rp.data.columnName, "test")
    }
}