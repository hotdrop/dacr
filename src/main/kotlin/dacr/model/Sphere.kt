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

    var grainList: MutableList<IGrain> = mutableListOf()
    // TODO PKはここで判定する

    init {
        for(column in colList) {
            when (column.dataType) {
                ColAttribute.DATA_TYPE_CHAR, ColAttribute.DATA_TYPE_VARCHAR -> {
                    grainList.add(GrainChar(column))
                }
                ColAttribute.DATA_TYPE_NUMBER -> {
                    grainList.add(GrainNumber(column))
                }
                ColAttribute.DATA_TYPE_DATE -> {
                    grainList.add(GrainDate(column))
                }
                ColAttribute.DATA_TYPE_TIMESTAMP -> {
                    grainList.add(GrainTimestamp(column))
                }
                else -> {
                    throw IllegalStateException("DataTypeが不正です。指定されたType=" + column.dataType)
                }
            }
        }
    }

    fun create() : List<String> {
        return grainList.map(IGrain::create)
    }
}