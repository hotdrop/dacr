package dacr.model

/**
 * Sphere
 *
 * Grainの集合体を表すモデルクラス。
 * ここでGrainのvalueを一定の数だけ生成し、何らかの形で取得できるようにする。
 * 一度に指定数を生成すると、数百万を超えるレコード生成にヒープが耐えられない・・多分
 */
class Sphere {

    var grainList: MutableList<Grain> = mutableListOf()

    fun add(grain : Grain) {
        grainList.add(grain)
    }

    fun create() : List<String> {
        return grainList.map { v -> v.createData() }
    }
}