package dacr.model

/**
 * Interface of Grain class holding one column information.
 *
 * The ColAttribute class is the role of the receiver of information entered from the outside,
 * and Grain is the role of processing information according to the program and making it easy to use.
 * Since this class has only one column of information, it does not make sense.
 * Therefore, we form Sphere class which is an aggregate of Grain and process it.
 */
interface IGrain {
    fun create() : String
    fun isPrimaryKey(): Boolean
    fun isFixingValue(): Boolean
    fun isAutoIncrement(): Boolean
}