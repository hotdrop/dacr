package dacr.model

/**
 * 1つのカラム情報を保持するGrainクラスのインタフェース。
 *
 * ColAttributeクラスは外部から入ってきた情報の受け皿の役割であり
 * Grainはプログラムに合わせて情報を加工し使いやすくする役割となる。
 * このクラスが持つ情報は1つのカラムだけなので意味を成さない。
 * そのため、Grainの集合体であるSphereクラスを形成して処理を行う。
 */
interface IGrain {

    fun create() : String
}