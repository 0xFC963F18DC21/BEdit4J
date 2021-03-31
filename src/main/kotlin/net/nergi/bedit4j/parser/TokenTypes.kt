package net.nergi.bedit4j.parser

abstract class Token<T> {
    abstract fun getContent(): T
}

data class TokenLine(val line: Int) : Token<Int>() {
    override fun getContent(): Int = line
}

data class TokenCommand(val cmd: String) : Token<String>() {
    override fun getContent(): String = cmd
}

data class TokenArg(val arg: String) : Token<String>() {
    override fun getContent(): String = arg
}
