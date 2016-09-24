package dacr.model

import dacr.indata.ColAttribute

/**
 * char型カラムのGrainクラス
 */
class GrainChar(attr: ColAttribute) : IGrain {

    val name : String
    val primaryKey : Boolean

    val value : String
    val multiValues : List<String>?

    val size : Int

    val isFixingValue: Boolean
    val isZeroPadding : Boolean
    val fillMaxSize : Boolean
    val autoIncrement : Boolean
    val hasMultiByte : Boolean

    init {
        name = attr.name
        primaryKey = attr.primaryKey
        size = attr.size

        isFixingValue = if(attr.valueType == ColAttribute.VALUE_TYPE_FIXING) true else false
        isZeroPadding = if(attr.format == ColAttribute.FORMAT_ZERO_PADDING) true else false
        fillMaxSize = attr.fillMaxSize
        autoIncrement = attr.autoIncrement
        hasMultiByte = attr.hasMultiByte

        value = if(isFixingValue) attr.value else ""
        multiValues = if(attr.value.contains(",")) attr.value.split(",") else null
    }

    /**
     * charの値を生成する
     * 値が固定の場合：無条件にvalueを返す
     * 値が可変＋autoIncrement：1からの連番
     */
    override fun create() : String {
        
        if(isFixingValue) {
            return value
        }

        var retVal = value

        if(autoIncrement) {
            retVal = Sequence.increment()
        } else if(retVal == "") {
            // 自動生成（PK判定、maxまで入れるか、マルチバイトありか）
        } else if(multiValues != null) {
            // リストからランダムに選択
        }

        if(isZeroPadding) {
            retVal = retVal.padStart(size, '0')
        }

        return retVal
    }

    private companion object Sequence {
        private var numVal = 0
        fun increment() : String {
            numVal++
            return numVal.toString()
        }
    }
}