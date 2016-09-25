package dacr.model

import dacr.indata.ColAttribute
import java.util.*

/**
 * char型とvarchar型カラムのGrainクラス
 * 最初はcharとvarchar分けていたが、データ生成は同じロジックで良さそう
 * なので一旦まとめる。後で不都合が出てきたら分割する
 */
class GrainChar(attr: ColAttribute) : IGrain {

    val name : String
    val primaryKey : Boolean

    private val value : String
    private val values: List<String>?
    private var valueIdx = 0

    private var sequence : Int = 1

    private val size : Int

    private val isFixingValue: Boolean
    private val isZeroPadding : Boolean
    private val fillMaxSize : Boolean
    private val autoIncrement : Boolean
    private val hasMultiByte : Boolean

    init {
        name = attr.name
        primaryKey = attr.primaryKey
        size = attr.size

        isFixingValue = if(attr.valueType == ColAttribute.VALUE_TYPE_FIXING) true else false
        autoIncrement = attr.autoIncrement

        value = attr.value
        // valueが空の場合を除外している理由はsequenceを初期値のままにするため。
        if(autoIncrement && value != "") {
            sequence = try { value.toInt() } catch (e : NumberFormatException) { 1 }
        }
        values = if(value.contains(",")) value.split(",") else null

        fillMaxSize = attr.fillMaxSize
        isZeroPadding = if(attr.format == ColAttribute.FORMAT_ZERO_PADDING && !fillMaxSize) true else false
        hasMultiByte = attr.hasMultiByte
    }

    /**
     * charの値を生成する
     */
    override fun create() : String {

        if(isFixingValue) {
            return makeFixingValue()
        }

        if(autoIncrement) {
            return makeAutoIncrement()
        }

        return makeVariableValue()
    }

    /**
     * 固定値作成
     * 表明:isFixingValue=True
     */
    private fun makeFixingValue() : String {

        if(values == null) {
            return value
        }

        val retVal = values[valueIdx]
        valueIdx++

        if(valueIdx >= values.size) {
            valueIdx = 0
        }
        return retVal
    }

    /**
     * 自動連番値作成
     * 表明:isFixingValue=false
     */
    private fun makeAutoIncrement() : String {
        var retVal = sequence.toString()
        sequence++
        return if(isZeroPadding) retVal.padStart(size, '0') else retVal
    }

    /**
     * 可変値作成
     * 表明:isFixingValue=false
     *     makeAutoIncrement=false
     */
    private fun makeVariableValue() : String {

        if(values != null) {
            return values[Random().nextInt(values.size)]
        }

        var retVal = if(hasMultiByte) makeMultiByteString() else makeSingleByteString()
        return if(isZeroPadding) retVal.padStart(size, '0') else retVal
    }

    /**
     * MultiByteという名の実態は日本語ひらがなで構成された文字列を作成する関数。
     */
    private fun makeMultiByteString() :String {

        val rand =Random()
        // 日本語文字はUTF8だと1文字3バイトになるため、例えばデータ長をバイトで計算するOracleとかでも
        // 入れられるよう1/3とした。fillMaxSizeを指定してしまった場合は仕方ないのでinsertエラーになってもらう。
        val makeSize = if(fillMaxSize) size else if (size >= 6) size/3 else 1
        var buff = ""

        for(idx in 1..makeSize) {
            buff += MULTI_BYTE_WORDS[rand.nextInt(MULTI_BYTE_WORDS.size)]
        }
        return buff
    }

    /**
     * バリエーションのないアルファベットと数値で構成された文字列を作成する関数。
     */
    private fun makeSingleByteString() : String {

        val rand =Random()
        // Multibyteに合わせて1/3にした。半分でもよかったがそんないらねーから生成スピードあげてくれという
        // 天の声に従う。
        val makeSize = if(fillMaxSize) size else if (size >= 6) size/3 else 1
        var buff = ""

        for(idx in 1..makeSize) {
            buff += WORDS[rand.nextInt(WORDS.size)]
        }
        return buff
    }

    private companion object {
        // もっと増やしてもいいかもしれないが、ランダム生成のコストが大きくなることを恐れて一旦この数にした
        private val MULTI_BYTE_WORDS = arrayOf("あ","い","う","え","お")
        private val WORDS = arrayOf("A","B","C","D","E","1","2","3","4","5")
    }
}