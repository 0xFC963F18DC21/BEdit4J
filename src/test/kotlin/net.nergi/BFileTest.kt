package net.nergi

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class BFileTest {
    private val file: BFile = BFile()

    @Test
    fun `files accept new lines`() {
        file.modifyContent(10, "PRINT \"HELLO, WORLD!\"")
        assertEquals(listOf("[ 10 ] PRINT \"HELLO, WORLD!\""), file.getRawLines())
        assertEquals(listOf("[ 1 ] PRINT \"HELLO, WORLD!\""), file.getActualLines())
    }

    @Test
    fun `file clears successfully`() {
        file.clear()
        assertTrue(file.getRawLines().isEmpty())
        assertTrue(file.getActualLines().isEmpty())
    }
}
