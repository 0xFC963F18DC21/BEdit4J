package net.nergi.bedit4j.parser

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CommandParserTests {
    @Test
    fun `parser returns null on empty string`() {
        assertNull(CommandParser.tokenise(""), "CommandParser has returned a non-null on empty string!")
    }

    @Test
    fun `parser returns only the command on one-word string`() {
        val group = CommandParser.tokenise("cmd")

        assertNotNull(group)
        assertEquals(TokenCommand("cmd"), group?.cmd)
        group?.args?.isEmpty()?.let { assertTrue(it) }
    }

    @Test
    fun `parser returns the command and arguments on valid command and arg string`() {
        val group = CommandParser.tokenise("cmd 0 1")

        assertNotNull(group)
        assertEquals(TokenCommand("cmd"), group?.cmd)
        assertEquals(listOf(TokenArg("0"), TokenArg("1")), group?.args)
    }

    @Test
    fun `parser returns a clear line if an empty line number is given`() {
        val group = CommandParser.tokenise("1")

        assertNotNull(group)
        assertEquals(TokenLine(1), group?.cmd)
        assertEquals(listOf(TokenArg("")), group?.args)
    }

    @Test
    fun `parser returns the line number and line content when given a number first`() {
        val group = CommandParser.tokenise("1 printf(\"hello world\")")

        assertNotNull(group)
        assertEquals(TokenLine(1), group?.cmd)
        assertEquals(listOf(TokenArg("printf(\"hello world\")")), group?.args)
    }
}
