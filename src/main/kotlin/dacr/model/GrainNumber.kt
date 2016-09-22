package dacr.model

import dacr.indata.ColAttribute

/**
 * number型カラムのGrainクラス
 */
class GrainNumber(attr: ColAttribute) : IGrain {

    val name : String
    val primaryKey : Boolean
    val value : String
    val size : Int

    val isFixingValue: Boolean
    val autoIncrements : Boolean

    init {
        name = attr.name
        primaryKey = attr.primaryKey
        value = attr.value
        size = attr.size

        isFixingValue = if(attr.valueType.equals(ColAttribute.VALUE_TYPE_FIXING)) true else false
        autoIncrements = attr.autoIncrements
    }

    override fun create() : String {
        // TODO
        return value
    }
}