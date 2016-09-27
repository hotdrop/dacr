package dacr

import dacr.indata.ColAttribute
import dacr.indata.RecordAttributeJson
import dacr.model.Sphere
import dacr.outdata.CsvFile
import org.junit.Assert
import org.junit.Test
import java.io.File

class CsvFileTest {

    @Test
    fun readJsonAndOutCsvFileTest() {
        val parentPath = System.getProperty("user.dir") + "/src/test/kotlin/dacr/testdata/"

        val inPath = parentPath + "sample.json"
        val outPath = parentPath + "outfile.csv"

        val columnList = RecordAttributeJson(inPath).parse()
        val dataSphere = Sphere(columnList)
        val csvFile = CsvFile(dataSphere, outPath, 3)
        csvFile.output()

        File(outPath).bufferedReader().forEachLine { line ->
            Assert.assertTrue(if(line.contains("firstValue")) true else false)
            Assert.assertTrue(if(line.contains("secondValue")) true else false)
            Assert.assertTrue(if(line.contains("thirdValue")) true else false)
        }

        File(outPath).delete()
    }

    @Test
    fun makeComplexCsvFileTest() {
        var colAttrList = mutableListOf<ColAttribute>()
        colAttrList.add(ColAttribute(name = "char_no", dataType = "char", primaryKey = true,
                size = 10, format = "zeroPadding", autoIncrement = true, fillMaxSize = false,
                valueType = "variable", value = "2", hasMultiByte = false))
        colAttrList.add(ColAttribute(name = "select_code", dataType = "char", primaryKey = false,
                size = 10, format = "", autoIncrement = false, fillMaxSize = false,
                valueType = "fixing", value = "A01,A02,B03,B04,C05", hasMultiByte = false))
        colAttrList.add(ColAttribute(name = "create_code", dataType = "char", primaryKey = false,
                size = 5, format = "", autoIncrement = false, fillMaxSize = false,
                valueType = "variable", value = "", hasMultiByte = false))
        colAttrList.add(ColAttribute(name = "code_name_japanese", dataType = "varchar", primaryKey = true,
                size = 30, format = "", autoIncrement = false, fillMaxSize = true,
                valueType = "variable", value = "", hasMultiByte = true))
        colAttrList.add(ColAttribute(name = "specific_number", dataType = "number", primaryKey = false,
                size = 1, format = "", autoIncrement = false, fillMaxSize = false,
                valueType = "variable", value = "0,1,2", hasMultiByte = false))
        colAttrList.add(ColAttribute(name = "price", dataType = "number", primaryKey = false,
                size = 5, format = "", autoIncrement = false, fillMaxSize = false,
                valueType = "variable", value = "", hasMultiByte = false))
        colAttrList.add(ColAttribute(name = "register_date", dataType = "date", primaryKey = false,
                size = 5, format = "YYYY/MM/dd", autoIncrement = false, fillMaxSize = false,
                valueType = "fixing", value = "now", hasMultiByte = false))
        colAttrList.add(ColAttribute(name = "random_date", dataType = "date", primaryKey = false,
                size = 5, format = "YYYY/MM/dd", autoIncrement = false, fillMaxSize = false,
                valueType = "variable", value = "", hasMultiByte = false))
        colAttrList.add(ColAttribute(name = "random_dateTime", dataType = "dateTime", primaryKey = false,
                size = 5, format = "YYYY/MM/dd hh:mm:ss", autoIncrement = false, fillMaxSize = false,
                valueType = "variable", value = "", hasMultiByte = false))
        colAttrList.add(ColAttribute(name = "update_timestamp", dataType = "timestamp",
                primaryKey = false,
                size = 10, format = "YYYY/MM/dd hh:mm:ss.SSS", autoIncrement = false, fillMaxSize = false,
                valueType = "variable", value = "", hasMultiByte = false))

        val parentPath = System.getProperty("user.dir") + "/src/test/kotlin/dacr/testdata/"
        val outPath = parentPath + "outComplexTest.csv"

        val dataSphere = Sphere(colAttrList)
        val csvFile = CsvFile(dataSphere, outPath, 10)
        csvFile.output()

        // 出力ファイルを目視する目的なので、Assertでのテストもファイル削除もしない。
        Assert.assertTrue(true)
    }

    @Test
    fun makeCsvFileWithPKTest() {
        var colAttrList = mutableListOf<ColAttribute>()
        colAttrList.add(ColAttribute(name = "pk1", dataType = "char", primaryKey = true,
                size = 5, format = "zeroPadding", autoIncrement = false, fillMaxSize = false,
                valueType = "variable", value = "", hasMultiByte = false))
        colAttrList.add(ColAttribute(name = "pk2", dataType = "char", primaryKey = true,
                size = 10, format = "", autoIncrement = false, fillMaxSize = false,
                valueType = "fixing", value = "A01,A02,B03,B04,C05", hasMultiByte = false))
        colAttrList.add(ColAttribute(name = "create_code", dataType = "char", primaryKey = false,
                size = 5, format = "", autoIncrement = false, fillMaxSize = false,
                valueType = "variable", value = "", hasMultiByte = false))

        val parentPath = System.getProperty("user.dir") + "/src/test/kotlin/dacr/testdata/"
        val outPath = parentPath + "outWithPKTest.csv"

        val dataSphere = Sphere(colAttrList)
        val csvFile = CsvFile(dataSphere, outPath, 5)
        csvFile.output()

        // 出力ファイルを目視する目的なので、Assertでのテストもファイル削除もしない。
        Assert.assertTrue(true)
    }
}