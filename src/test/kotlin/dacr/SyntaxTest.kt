package dacr

import org.junit.Assert

/**
 * ツールとは無関係のKotlin文法テストクラス
 */
class SyntaxTest {

    @org.junit.Test
    fun stringEqualsTest() {
        val str1 : String = "test"
        val str2 : String = "test"

        Assert.assertTrue(str1.equals(str2))
        Assert.assertTrue(str1 == str2)
        Assert.assertTrue(str1 === str2)
    }
}