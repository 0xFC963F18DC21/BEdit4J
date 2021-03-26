package net.nergi

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.StringReader

class BFileTest {
    private val file: BFile = BFile()

    @Test
    fun `files accept new lines`() {
        file.clear()
        file.modifyContent(10, "PRINT \"HELLO, WORLD!\"")
        assertEquals(listOf("[ 10 ] PRINT \"HELLO, WORLD!\""), file.getRawLines())
        assertEquals(listOf("[ 1 ] PRINT \"HELLO, WORLD!\""), file.getActualLines())
    }

    @Test
    fun `file clears successfully`() {
        file.modifyContent(10, "test")
        file.clear()

        assertTrue(file.getRawLines().isEmpty())
        assertTrue(file.getActualLines().isEmpty())
    }

    @Test
    fun `files can push down lines correctly`() {
        file.clear()
        file.modifyContent(10, "PRINT \"HELLO, WORLD!\"")
        file.pushLinesForwardFrom(0, 10)

        assertEquals(listOf("[ 20 ] PRINT \"HELLO, WORLD!\""), file.getRawLines())
        assertEquals(listOf("[ 1 ] PRINT \"HELLO, WORLD!\""), file.getActualLines())
    }

    @Test
    fun `files can push up lines correctly`() {
        file.clear()
        file.modifyContent(10, "PRINT \"HELLO, WORLD!\"")
        file.pushLinesBackFrom(15, 5)

        assertEquals(listOf("[ 5 ] PRINT \"HELLO, WORLD!\""), file.getRawLines())
        assertEquals(listOf("[ 1 ] PRINT \"HELLO, WORLD!\""), file.getActualLines())
    }

    @Test
    fun `files can load from strings`() {
        file.clear()
        file.loadFromString("Hello\nThere")

        assertEquals(listOf("[ 10 ] Hello", "[ 20 ] There"), file.getRawLines())
        assertEquals(listOf("[ 1 ] Hello", "[ 2 ] There"), file.getActualLines())
    }

    @Test
    fun `files can load from readers`() {
        file.clear()
        file.loadFromReader(StringReader("Hello\nThere"))

        assertEquals(listOf("[ 10 ] Hello", "[ 20 ] There"), file.getRawLines())
        assertEquals(listOf("[ 1 ] Hello", "[ 2 ] There"), file.getActualLines())
    }
}
