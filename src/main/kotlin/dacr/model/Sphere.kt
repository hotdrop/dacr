package dacr.model

import dacr.indata.ColAttribute

/**
 * Sphere
 *
 * Grainの集合体を表すモデルクラス。
 * Grainで生成した値をリスト形式で取得する。
 * また、一度に1つのrowを生成する。
 */
class Sphere(colList : List<ColAttribute>) {

    var grainList : MutableList<IGrain> = mutableListOf()
    /**
     * PK判定について
     * Mapを使用してPKが一意になるようにする。
     *
     * PK判定をするのは以下のパターンのみとする。
     * valueType = variable and autoIncrement=false
     *
     * valueで複数指定した場合でもvariable指定ならランダムに値を選択するのでPK判定対象とする。
     * ただし、同じ値でループする恐れがあるので実装時は注意する・・
     */
    var pkMap = mutableMapOf<String, Boolean>()

    var unnecessaryPK = false
    var primaryKeyCount = 0

    init {
        var grain : IGrain
        for(column in colList) {
            when (column.dataType) {
                ColAttribute.DATA_TYPE_CHAR, ColAttribute.DATA_TYPE_VARCHAR -> {
                    grain = GrainChar(column)
                    grainList.add(grain)
                }
                ColAttribute.DATA_TYPE_NUMBER -> {
                    grain = GrainNumber(column)
                    grainList.add(grain)
                }
                ColAttribute.DATA_TYPE_DATE, ColAttribute.DATA_TYPE_DATETIME -> {
                    grain = GrainDate(column)
                    grainList.add(grain)
                }
                ColAttribute.DATA_TYPE_TIMESTAMP -> {
                    grain = GrainTimestamp(column)
                    grainList.add(grain)
                }
                else -> {
                    throw IllegalStateException("DataTypeが不正です。指定されたType=" + column.dataType)
                }
            }

            if(grain.primaryKey) {
                setPKInformation(grain)
            }
        }
    }

    fun create(): List<String> {

        fun decisionPK(): Boolean {
             return if(!unnecessaryPK && primaryKeyCount > 0) true else false
        }

        if(decisionPK()) {
            return createWithPK()
        }
        return grainList.map(IGrain::create)


    }

    private fun setPKInformation(grain : IGrain) {

        if(grain.isFixingValue) {
            return
        }

        if(grain.autoIncrement) {
            unnecessaryPK = true
        }

        primaryKeyCount++
    }

    private fun createWithPK(): List<String> {

        var valueList = mutableListOf<String>()

        PKisDuplicate@ while(true) {

            valueList.clear()
            var pkCnt = primaryKeyCount
            var pkConcatStr = ""

            for(grain in grainList) {
                val value = grain.create()
                if(grain.primaryKey) {
                    pkConcatStr += value
                    pkCnt--
                    if(pkCnt == 0) {
                        if(pkMap.containsKey(pkConcatStr)) {
                            break@PKisDuplicate
                        } else {
                            pkMap.put(pkConcatStr, true)
                        }
                    }
                }
                valueList.add(value)
            }
        }

        return valueList
    }
}