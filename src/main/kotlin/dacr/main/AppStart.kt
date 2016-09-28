package dacr.main

import dacr.indata.RecordAttributeJson
import dacr.model.Sphere
import dacr.outdata.CsvFile

object AppStart {

    @JvmStatic fun main(args: Array<String>) {

        if(args.size != 3) {
            println("usage: dacr [<inputFilePath>] [<outputFilePath>] [<create record number>]")
            return
        }

        run(args[0], args[1], args[2].toInt())
    }

    fun run(inPath: String, outPath: String, createNum: Int) {
        print("start create data")
        val columnList = RecordAttributeJson(inPath).parse()
        val dataSphere = Sphere(columnList)
        val csvFile = CsvFile(dataSphere, outPath, createNum)
        csvFile.output()
        print("complete!!")
    }
}