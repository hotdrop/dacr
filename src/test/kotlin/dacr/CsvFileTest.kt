package dacr

import dacr.indata.RecordAttributeJson
import dacr.outdata.CsvFile
import org.junit.Assert
import java.io.File

class CsvFileTest {

    @org.junit.Test
    fun makeCsvFile() {
        val parentPath = System.getProperty("user.dir") + "/src/test/kotlin/dacr/testdata/"

        val inPath = parentPath + "sample.json"
        val outPath = parentPath + "outfile.csv"

        val columnList = RecordAttributeJson(inPath).parse()

        val csvFile = CsvFile(columnList, outPath, 3)
        csvFile.output()
        
        File(outPath).bufferedReader().forEachLine { line ->
            Assert.assertTrue(if(line.contains("firstValue")) true else false)
            Assert.assertFalse(if(line.contains("secondValue")) true else false)
            Assert.assertTrue(if(line.contains("thirdValue")) true else false)
        }

        // ここは必要に応じて変更する
        File(outPath).delete()
    }
}