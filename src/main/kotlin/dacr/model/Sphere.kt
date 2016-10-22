package dacr.model

import dacr.indata.ColAttribute

/**
 * Sphere
 *
 * Grainの集合体を表すモデルクラス。
 * Grainで生成した値をリスト形式で取得する。
 * また、一度に1つのrowを生成する。
 */
class Sphere(colList: List<ColAttribute>) {

    private var grainList: MutableList<IGrain> = mutableListOf()

    /**
     * PK判定をするカラムのカウントとPrimaryKeyのMap
     *
     * PK判定は以下の場合に行う。
     * 1. PK指定カラムがvariableの場合（fixingの場合はPK指定されていても無視する）
     * 2. PK指定カラムにautoIncrementが1つも含まれていない
     *
     * なお、カウントに−1を設定した場合、以後カウントは行わない
     */
    private var pkMap = mutableMapOf<String, Boolean>()
    private var judgePKCnt = 0

    init {
        fun setPKInformation(grain: IGrain) {
            if(!grain.primaryKey || grain.isFixingValue || judgePKCnt == -1) {
                return
            } else if(grain.autoIncrement) {
                judgePKCnt = -1
            } else {
                judgePKCnt++
            }
        }

        var grain: IGrain
        for(column in colList) {
            when (column.dataType.toUpperCase()) {
                ColAttribute.DATA_TYPE_CHAR      -> grain = GrainChar(column)
                ColAttribute.DATA_TYPE_VARCHAR   -> grain = GrainChar(column)
                ColAttribute.DATA_TYPE_VARCHAR2  -> grain = GrainChar(column)
                ColAttribute.DATA_TYPE_INT       -> grain = GrainInteger(column)
                ColAttribute.DATA_TYPE_INTEGER   -> grain = GrainInteger(column)
                ColAttribute.DATA_TYPE_DATE      -> grain = GrainDate(column)
                ColAttribute.DATA_TYPE_DATETIME  -> grain = GrainDate(column)
                ColAttribute.DATA_TYPE_TIMESTAMP -> grain = GrainTimestamp(column)
                else ->  throw IllegalStateException("unsupported DataType. " +
                        " columnName=" + column.name + " dataType=" + column.dataType)
            }

            setPKInformation(grain)
            grainList.add(grain)
        }
    }

    fun create(): List<String> = if(judgePKCnt > 0) createWithPK()
                                 else grainList.map(IGrain::create)

    private fun createWithPK(): List<String> {

        var valueList = mutableListOf<String>()
        var duplicateCount = 0
        var maxDuplicateCount = 100 // この値は本当は生成数によって変動すべき

        PKisDuplicate@ while(true) {

            if(duplicateCount > maxDuplicateCount) {
                throw IllegalStateException("PKに指定したカラムの生成試行回数が上限値に達しました。" +
                        "これ以上は無限ループの可能性があったため中断しました。")
            }

            valueList.clear()
            var pkCnt = judgePKCnt
            var pkConcatStr = ""

            for(grain in grainList) {
                val value = grain.create()
                if(grain.primaryKey && !grain.isFixingValue) {
                    pkConcatStr += value
                    pkCnt--
                    if(pkCnt == 0) {
                        if(pkMap.containsKey(pkConcatStr)) {
                            duplicateCount++
                            continue@PKisDuplicate
                        } else {
                            pkMap.put(pkConcatStr, true)
                        }
                    }
                }
                valueList.add(value)
            }
            break
        }
        return valueList
    }
}