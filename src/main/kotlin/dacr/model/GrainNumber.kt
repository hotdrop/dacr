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
    val autoIncrement : Boolean

    init {
        name = attr.name
        primaryKey = attr.primaryKey
        value = attr.value
        size = attr.size

        isFixingValue = if(attr.valueType == ColAttribute.VALUE_TYPE_FIXING) true else false
        autoIncrement = attr.autoIncrement
    }

    override fun create() : String {
        // TODO
        return value
    }
}