package dacr.model

import dacr.indata.ColAttribute

/**
 * date型カラムのGrainクラス
 */
class GrainTimestamp(attr: ColAttribute) : IGrain {

    val name : String
    val primaryKey : Boolean
    val value : String

    val format : String
    val isFixingValue: Boolean

    init {
        name = attr.name
        primaryKey = attr.primaryKey
        value = attr.value

        isFixingValue = if(attr.valueType.equals(ColAttribute.VALUE_TYPE_FIXING)) true else false
        format = attr.format
    }

    override fun create() : String {
        // TODO
        return value
    }
}