package dacr.model

import dacr.indata.ColAttribute

/**
 * Grain
 *
 * 1つのカラム情報を保持するモデルクラス。
 * すでにRecordAttributeDataがあるがそっちはあくまで外部から入ってきた情報の受け皿クラス
 * 本クラスは、このツール内でやりとりしやすいようにデータを加工する目的を持つ。
 */
class Grain(attr: ColAttribute) {

    val columnName : String
    val primaryKey: Boolean

    val dataType : String
    val size : Int
    val dateFormat : String
    val isZeroPadding : Boolean

    val autoIncrements : Boolean
    val fillMaxSize : Boolean

    val isFixingValue: Boolean
    val value : String
    val hasMultiByte : Boolean

    //val otherCodeSystem : String

    init {
        columnName = attr.name
        primaryKey = attr.primaryKey

        dataType = attr.dataType
        size = attr.size

        // formatはあまり増えないと思うので今はcaseでいちいち初期化する
        when (attr.format) {
            ColAttribute.FORMAT_ZERO_PADDING -> {
                isZeroPadding = true
                dateFormat = ""
            }
            else -> {
                isZeroPadding = false
                dateFormat = attr.format
            }
        }

        autoIncrements = attr.autoIncrements
        fillMaxSize = attr.fillMaxSize

        isFixingValue = if(attr.valueType.equals(ColAttribute.VALUE_TYPE_FIXING)) true else false
        value = attr.value
        hasMultiByte = attr.hasMultiByte
    }

    fun <T> createValue(body: () -> T ): T {
        return body()
    }

    /**
     * カラム定義情報を元に、値を生成して取得する。
     * TODO 実装中
     */
    fun createData() : String {
        // TODO ここはやり方変える。init内で生成関数を一意に決定したほうがいい
        return createValue( {
            when (dataType) {
                ColAttribute.DATA_TYPE_CHAR -> {
                    createCharVal()
                }
                else -> {
                    createDateVal()
                }
            }
        })
    }

    private fun createCharVal() : String {
        return ""
    }

    private fun createVarCharVal() : String {
        return ""
    }

    private fun createNumberVal() : String {
        return ""
    }

    private fun createDateVal() : String {
        return ""
    }

    private fun createTimeStampVal() : String {
        return ""
    }
}