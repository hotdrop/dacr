package dacr.model

import dacr.indata.RecordAttributeData

/**
 * Grain
 *
 * 1つのカラム情報を保持するモデルクラス。
 * すでにRecordAttributeDataがあるがそっちはあくまで外部から入ってきた情報の受け皿クラス
 * 本クラスは、このツール内でやりとりしやすいようにデータを加工する目的を持つ。
 */
class Grain(attrData : RecordAttributeData) {

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
        columnName = attrData.columnName
        if(attrData.valueType.equals(RecordAttributeData.VALUE_TYPE_FIXING)) {
            value = attrData.value
        } else {
            value = ""
        }
        // PKならvalueの重複なし
        primarykey = attrData.primaryKey
        // TODO この下実装なし
    }

}