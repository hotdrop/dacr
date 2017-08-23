package dacr.outdata

import dacr.model.Sphere
import java.io.BufferedWriter
import java.io.File

/**
 * Create and Output CSV File
 */
class CsvFile(private val sphere: Sphere,
              private val outputPath: String,
              private val lineNumber: Int) {

    private fun BufferedWriter.writeLine(line : String) {
        this.write(line)
        this.newLine()
    }

    fun output() {
        File(outputPath).bufferedWriter().use { out ->
            for (i in 1..lineNumber) {
                out.writeLine(sphere.create().joinToString(","))
            }
        }
    }
}