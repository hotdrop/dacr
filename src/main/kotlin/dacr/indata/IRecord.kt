package dacr.indata

interface IRecord {
    fun parse() : List<ColAttribute>
}