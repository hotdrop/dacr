package dacr.indata

/**
 * レコード情報のインタフェース
 *
 * レコード情報を読み込む媒体はなんでもいいので
 * このインタフェースを使用してparseの中身を実装してください。
 */
interface IRecord {

    fun parse() : RecordAttributeData
}