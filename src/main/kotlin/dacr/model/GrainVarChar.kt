package dacr.model

import dacr.indata.ColAttribute

/**
 * varchar型カラムのGrainクラス
 */
class GrainVarChar(attr: ColAttribute) : IGrain {

    val name : String
    val primaryKey : Boolean
    val value : String
    val size : Int

    val isFixingValue: Boolean
    val isZeroPadding : Boolean
    val fillMaxSize : Boolean
    val autoIncrement : Boolean
    val hasMultiByte : Boolean

    init {
        name = attr.name
        primaryKey = attr.primaryKey
        value = attr.value
        size = attr.size

        isFixingValue = if(attr.valueType == ColAttribute.VALUE_TYPE_FIXING) true else false
        isZeroPadding = if(attr.format == ColAttribute.FORMAT_ZERO_PADDING) true else false
        fillMaxSize = attr.fillMaxSize
        autoIncrement = attr.autoIncrement
        hasMultiByte = attr.hasMultiByte

    }

    override fun create() : String {
        // TODO charとほとんど同じになりそう・・
        return value
    }
}