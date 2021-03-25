package net.nergi

import kotlin.math.floor
import kotlin.math.log10

class BFile {
    /**
     * Lines - Line content of file
     */
    private val lines: MutableMap<Int, String> = mutableMapOf()

    /**
     * Modify content
     *
     * Modify the content of one of the lines.
     *
     * @param line    Line number to modify
     * @param content Content to set the line to
     */
    fun modifyContent(line: Int, content: String) {
        lines[line] = content
    }

    /**
     * Get raw lines
     *
     * Get the lines as they are numbered inside the map
     *
     * @param from Line to start from
     * @param to   Line to end on
     * @return Lines grabbed from map
     */
    @Throws(IllegalArgumentException::class)
    fun getRawLines(from: Int = -1, to: Int = -1): List<String> {
        if (to < from) {
            throw IllegalArgumentException("Illegal range of lines")
        }

        val filtered = if (from < 0 || to < 0) lines else lines.filter { it.key in from..to }
        val digits = floor(log10((filtered.keys.maxOrNull() ?: 1).toDouble())).toInt()

        return filtered.map { "[ ${digits}d ] %s".format(it.key, it.value) }
    }

    /**
     * Get actual lines
     *
     * Get the lines as they would be numbered in a real file
     *
     * @param from Line to start from
     * @param to   Line to end on
     * @return Lines grabbed from map
     */
    @Throws(IllegalArgumentException::class)
    fun getActualLines(from: Int = -1, to: Int = -1): List<String> {
        if (to < from) {
            throw IllegalArgumentException("Illegal range of lines")
        }

        var line = 1
        val sorted = lines.toSortedMap { i1, i2 -> Integer.compare(i1, i2) }
            .mapKeys { line++ }
        val filtered = if (from < 0 || to < 0) sorted else sorted.filter { it.key in from..to }
        val digits = floor(log10((filtered.keys.maxOrNull() ?: 1).toDouble())).toInt()

        return filtered.map { "[ ${digits}d ] %s".format(it.key, it.value) }
    }
}
