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
        columnName = attrData.name
        if(attrData.valueType.equals(ColumnAttributeData.VALUE_TYPE_FIXING)) {
            value = attrData.value
        } else {
            value = ""
        }
        // PKならvalueの重複なし
        primarykey = attrData.primaryKey
        // TODO この下実装なし
    }

    /**
     * カラム定義情報を元に、値を生成して取得する。
     * ここはかなりfat化する可能性が高い
     */
    public fun createData() : String {
        // TODO variableの場合は値を生成する。
        return value
    }

    /**
     * 定義情報をチェックする。
     * ここでチェックするのがいいのか、読み込み時がいいのか・・
     */
    private fun attributeCheck() {

    }
}