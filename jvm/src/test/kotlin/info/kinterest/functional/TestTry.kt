package info.kinterest.functional

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


class TestTry : Spek( {
    given("a successfull try") {
        on("creating it") {
            val n = Try {1}
            it("should be a success") {
                assertTrue(n.isSuccess)
            }
            it("should deliver a proper result") {
                assertEquals(1, n.getOrElse { throw it })
            }
        }

    }

    given("a failed try") {
        val t = Try {throw Exception()}
        on("creating it") {
            it("should be a failure") {
                assertTrue(t.isFailure)
            }
        }

        on("getting its value") {
            val ex = t.getOrElse { it }
            it("should be an exception") {
                assertTrue(ex is Exception)
            }
        }


    }


})