package dacr

import dacr.indata.RecordAttributeJson
import org.junit.Assert

class RecordPropertyTest {

    @org.junit.Test
    fun readJson() {
        val path = System.getProperty("user.dir") + "/src/test/kotlin/dacr/testdata/sample.json"
        val columnList = RecordAttributeJson(path).parse()

        Assert.assertEquals(columnList[0].name, "first")
        Assert.assertEquals(columnList[1].name, "second")
    }
}