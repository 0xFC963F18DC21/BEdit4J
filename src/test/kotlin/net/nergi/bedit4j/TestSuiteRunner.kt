package net.nergi.bedit4j

import IgnoreInRunner
import net.nergi.bedit4j.parser.CommandParserTests
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.lang.reflect.Method

object TestSuiteRunner {
    @JvmStatic
    fun main(args: Array<String>) {
        // Collect classes and their test methods
        val classes = setOf(
            BFileTests::class.java,
            CommandParserTests::class.java,
            FileHandlerTests::class.java,
        )

        val instances = classes.map {
            try {
                // TEST CLASS INSTANCE | COLLECTION OF METHODS MARKED AS @Test
                it.getConstructor().newInstance() to collectTestMethods(it)
            } catch (e: Exception) {
                null
            }
        }

        // Get all instances and run their tests
        instances.forEach {
            if (it != null) {
                val (instance, tests) = it

                // Print name, run tests and collect all exceptions and print them
                println("--- Failures / Exceptions for ${instance.javaClass} ---\n")
                val exceptions: MutableSet<Exception> = mutableSetOf()

                tests.forEach { test ->
                    try {
                        test.invoke(instance)
                    } catch (e: Exception) {
                        exceptions.add(e)
                        e.printStackTrace()
                        print("\n")
                    }
                }

                println("Failed: ${exceptions.size} / ${tests.size}\n")
            }
        }
    }

    // Uses Java's reflection library
    private fun collectTestMethods(clazz: Class<*>): Set<Method> =
        clazz.methods.filter {
            it.isAnnotationPresent(Test::class.java) &&
                !it.isAnnotationPresent(Disabled::class.java) &&
                !it.isAnnotationPresent(IgnoreInRunner::class.java)
        }.toSet()
}
