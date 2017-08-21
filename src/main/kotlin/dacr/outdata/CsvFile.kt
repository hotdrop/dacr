package dacr.outdata

import dacr.model.Sphere
import java.io.BufferedWriter
import java.io.File

/**
 * Create and Output CSV File
 */
class CsvFile(sphere: Sphere, outPath: String, lineNumber: Int) {

    private val dataSphere = sphere
    private val outputPath = outPath
    private val outLineNum = lineNumber

    private fun BufferedWriter.writeLine(line : String) {
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