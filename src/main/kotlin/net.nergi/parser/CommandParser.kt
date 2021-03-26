package net.nergi.parser

import java.util.Collections
import java.util.StringTokenizer

object CommandParser {
    /**
     * Tokenise
     *
     * Tokenises a line into a command and its arguments. Tokens are space-delimited.
     *
     * @param line Line to parse
     * @return TokenGroup if a valid command is parsed, null otherwise.
     */
    @JvmStatic
    fun tokenise(line: String): TokenGroup? {
        if (line.isBlank()) {
            return null
        }

        val tokens = StringTokenizer(line, " ")

        val first = tokens.nextToken()
        return if (first.isNumber()) {
            TokenGroup(TokenLine(first.toInt()), listOf(TokenArg(line.dropWhile { it.isDigit() }.drop(1))))
        } else {
            TokenGroup(TokenCommand(first), Collections.list(tokens).map { TokenArg(it.toString()) })
        }
    }

    @JvmStatic
    private fun String.isNumber(): Boolean = all { it.isDigit() }
}
