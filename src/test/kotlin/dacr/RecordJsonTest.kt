package dacr

import dacr.indata.RecordAttributeJson
import org.junit.Assert

class RecordJsonTest {

    @org.junit.Test
    fun readJson() {
        val path = System.getProperty("user.dir") + "/src/test/kotlin/dacr/testdata/sample.json"
        val colAttributeList = RecordAttributeJson(path).parse()

        Assert.assertEquals(colAttributeList[0].name, "first")
        Assert.assertEquals(colAttributeList[1].name, "second")
        Assert.assertEquals(colAttributeList[2].name, "third")
    }
}