package net.nergi.bedit4j

import IgnoreInRunner
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.junit.jupiter.api.io.TempDir
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

class FileHandlerTests {
    @JvmField
    @TempDir
    var tempFolder: Path? = null

    private val lines = listOf(
        "public class Hello {",
        "    public static void main(String[] args) {",
        "        System.out.println(\"Hello, world!\");",
        "    }",
        "}"
    )

    private fun initialize() {
        assert(tempFolder != null)
        val testFile = tempFolder?.resolve("Hello.java")

        if (Files.exists(testFile)) {
            return
        }

        // First off, make sure it works
        try {
            Files.write(testFile, lines)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        Assertions.assertAll(
            Executable {
                Assertions.assertTrue(
                    Files.exists(testFile)
                )
            },
            Executable {
                Assertions.assertLinesMatch(
                    lines,
                    Files.readAllLines(testFile)
                )
            }
        )
    }

    @Test
    @IgnoreInRunner
    fun `files are loaded correctly`() {
        initialize()

        val reader = FileHandler.loadFileAsReader(tempFolder?.resolve("Hello.java")?.toAbsolutePath().toString())

        val newFile = BFile()
        newFile.loadFromReader(reader)

        assertEquals(listOf("[ 10 ] public class Hello {"), newFile.getRawLines(10, 10))
        assertEquals(listOf("[ 20 ]     public static void main(String[] args) {"), newFile.getRawLines(20, 20))
        assertEquals(listOf("[ 30 ]         System.out.println(\"Hello, world!\");"), newFile.getRawLines(30, 30))
        assertEquals(listOf("[ 40 ]     }"), newFile.getRawLines(40, 40))
        assertEquals(listOf("[ 50 ] }"), newFile.getRawLines(50, 50))
    }
}
