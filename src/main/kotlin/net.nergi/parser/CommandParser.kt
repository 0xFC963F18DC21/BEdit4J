package net.nergi.parser

object CommandParser {
    /**
     * Tokenise
     *
     * Tokenises a line into a command and its arguments. Tokens are space-delimited.
     *
     * @param line Line to parse
     * @return TokenGroup if a valid command is parsed, null otherwise.
     */
    fun tokenise(line: String): TokenGroup? {
        if (line.isBlank()) {
            return null
        }

        val splitted = line.trimStart().split(' ')

        val first = splitted.first()
        return if (first.isNumber()) {
            TokenGroup(TokenLine(first.toInt()), listOf(TokenArg(line.dropWhile { it.isDigit() }.drop(1))))
        } else {
            TokenGroup(TokenCommand(first), splitted.drop(1).map { TokenArg(it) })
        }
    }

    private fun String.isNumber(): Boolean = all { it.isDigit() }
}
