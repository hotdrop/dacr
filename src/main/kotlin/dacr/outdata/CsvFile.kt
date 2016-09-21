package dacr.outdata

import dacr.indata.ColumnAttributeData
import dacr.model.Grain
import dacr.model.Sphere
import java.io.BufferedWriter
import java.io.File

/**
 * データを生成しCSV出力する。
 *
 * 1テーブル分のCsvFileを作成する。
 * CSV形式以外にも対応したいので後でインタフェースを実装したい。
 */
class CsvFile(columnList : List<ColumnAttributeData>, outPath : String, lineNumber : Int) {

    val outputPath = outPath
    val outLineNum = lineNumber

    val dataSphere = Sphere()

    init {
        for(column in columnList) {
            val grain = Grain(column)
            dataSphere.add(grain)
        }
    }

    fun BufferedWriter.writeLine(line : String) {
        this.write(line)
        this.newLine()
    }

    fun output() {
        File(outputPath).bufferedWriter().use { out ->
            for (i in 1..outLineNum) {
                out.writeLine(dataSphere.create().joinToString(","))
            }
        }
    }
}