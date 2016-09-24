package dacr.model

import dacr.indata.ColAttribute

/**
 * char型カラムのGrainクラス
 */
class GrainChar(attr: ColAttribute) : IGrain {

    val name : String
    val primaryKey : Boolean
    val value : String
    val size : Int

    val isFixingValue: Boolean
    val isZeroPadding : Boolean
    val autoIncrements : Boolean
    val hasMultiByte : Boolean

    val otherCodeSystem : String

    init {
        name = attr.name
        primaryKey = attr.primaryKey
        value = attr.value
        size = attr.size

        isFixingValue = if(attr.valueType == ColAttribute.VALUE_TYPE_FIXING) true else false
        isZeroPadding = if(attr.format == ColAttribute.FORMAT_ZERO_PADDING) true else false
        autoIncrements = attr.autoIncrements
        hasMultiByte = attr.hasMultiByte

        otherCodeSystem = attr.otherCodeSystem
    }

    override fun create() : String {
        // TODO
        return value
    }
}