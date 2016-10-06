package dacr.outdata

import dacr.model.Sphere
import java.io.BufferedWriter
import java.io.File

/**
 * データを生成しCSV出力する。
 *
 * 1テーブル分のCsvFileを作成する。
 */
class CsvFile(sphere: Sphere, outPath: String, lineNumber: Int) {

    private val dataSphere = sphere
    private val outputPath = outPath
    private val outLineNum = lineNumber

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