package info.kinterest.functional

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


class TestTry {
    @Test
    fun testSimple() {
        val n = Try {1}
        assertTrue(n.isSuccess)
        assertEquals(1, n.getOrElse { throw it })
    }

    @Test
    fun testFail() {
        val t = Try {throw Exception()}
        assertTrue(t.isFailure)
        val ex = t.getOrElse { it }
        assertTrue(ex is Exception)
    }

}