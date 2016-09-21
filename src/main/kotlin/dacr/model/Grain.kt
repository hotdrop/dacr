package dacr.model

import dacr.indata.ColumnAttributeData

/**
 * Grain
 *
 * 1つのカラム情報を保持するモデルクラス。
 * すでにRecordAttributeDataがあるがそっちはあくまで外部から入ってきた情報の受け皿クラス
 * 本クラスは、このツール内でやりとりしやすいようにデータを加工する目的を持つ。
 */
class Grain(attrData : ColumnAttributeData) {

    val columnName : String
    val value : String

    val primarykey : Boolean
    //val dataType : String
    //val size : Int

    //val format : String
    //val otherCodeSystem : String
    //val hasMultiByte : Boolean
    //val fillMaxSize : Boolean

    init {
        // TODO no implements
        columnName = attrData.name
        if(attrData.valueType.equals(ColumnAttributeData.VALUE_TYPE_FIXING)) {
            value = attrData.value
        } else {
            value = ""
        }
        primarykey = attrData.primaryKey

    }

    /**
     * カラム定義情報を元に、値を生成して取得する。
     * ここはかなりfat化する可能性が高い
     */
    fun createData() : String {
        // TODO variableの場合は値を生成する。
        return value
    }

    private fun attributeCheck() : Boolean {
        // TODO 未実装 ここでチェックするのがいいのか、読み込み時がいいのか・・
        return true
    }
}