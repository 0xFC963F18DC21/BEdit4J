package net.nergi.parser

abstract class Token

data class TokenLine(val line: Int) : Token()
data class TokenCommand(val cmd: String) : Token()
data class TokenArg(val arg: String) : Token()
