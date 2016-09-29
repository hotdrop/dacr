package dacr.outdata

import dacr.model.Sphere
import java.io.BufferedWriter
import java.io.File

/**
 * データを生成しCSV出力する。
 *
 * 1テーブル分のCsvFileを作成する。
 * CSV形式以外にも対応したいので後でインタフェースを実装したい。
 */
class CsvFile(sphere: Sphere, outPath : String, lineNumber : Int) {

    val dataSphere = sphere
    val outputPath = outPath
    val outLineNum = lineNumber

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

    fun outputWithDoubleQuote() {
        File(outputPath).bufferedWriter().use { out ->
            for (i in 1..outLineNum) {
                out.write(""""""")
                out.write(dataSphere.create().joinToString("""",""""))
                out.writeLine(""""""")
            }
        }
    }
}