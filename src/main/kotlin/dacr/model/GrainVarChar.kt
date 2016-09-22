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
    val autoIncrements : Boolean
    val hasMultiByte : Boolean

    val otherCodeSystem : String

    init {
        name = attr.name
        primaryKey = attr.primaryKey
        value = attr.value
        size = attr.size

        isFixingValue = if(attr.valueType.equals(ColAttribute.VALUE_TYPE_FIXING)) true else false
        isZeroPadding = if(attr.format.equals(ColAttribute.FORMAT_ZERO_PADDING)) true else false
        autoIncrements = attr.autoIncrements
        hasMultiByte = attr.hasMultiByte

        otherCodeSystem = attr.otherCodeSystem
    }

    override fun create() : String {
        // TODO charとほとんど同じになりそう・・
        return value
    }
}