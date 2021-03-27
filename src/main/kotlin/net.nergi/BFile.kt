package net.nergi

import java.io.Reader
import java.io.StringReader
import java.util.stream.Stream
import kotlin.math.floor
import kotlin.math.log10

class BFile {
    /**
     * Lines - Line content of file
     */
    private var lines: MutableMap<Int, String> = mutableMapOf()

    /**
     * Modify content
     *
     * Modify the content of one of the lines.
     *
     * @param line    Line number to modify
     * @param content Content to set the line to
     */
    @Throws(IllegalArgumentException::class)
    fun modifyContent(line: Int, content: String) {
        if (line <= 0) {
            throw IllegalArgumentException("Not a valid line")
        }

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
        val digits = floor(log10((filtered.keys.maxOrNull() ?: 1).toDouble())).toInt() + 1

        return filtered.map { "[ %${digits}d ] %s".format(it.key, it.value) }
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
        val digits = floor(log10((filtered.keys.maxOrNull() ?: 1).toDouble())).toInt() + 1

        return filtered.map { "[ %${digits}d ] %s".format(it.key, it.value) }
    }

    /**
     * Push lines forward from
     *
     * Push lines forward by a given number of lines, starting from a given line.
     *
     * @param from   Line to start pushing forward from
     * @param offset How much to push forward by
     */
    fun pushLinesForwardFrom(from: Int, offset: Int) {
        lines = lines.mapKeys { if (it.key >= from) it.key + offset else it.key }.toMutableMap()
    }

    /**
     * Push lines back from
     *
     * Push lines back towards the start of the file. If any lines are displaced past the start of the file when the push occurs, the push fails.
     *
     * @param from   Line to start pushing back from
     * @param offset How much to push back by
     */
    @Throws(IllegalArgumentException::class)
    fun pushLinesBackFrom(from: Int, offset: Int) {
        if (lines.keys.any { it <= offset }) {
            throw IllegalArgumentException("Pushing lines past line 0")
        }

        lines = lines.mapKeys { if (it.key <= from) it.key - offset else it.key }.toMutableMap()
    }

    /**
     * Load from string
     *
     * Load a new file from a string.
     *
     * @param from String to load from
     * @param gap  Gap between line numbers
     */
    fun loadFromString(from: String, gap: Int = 10) {
        clear()

        val reader = StringReader(from)
        reader.useLines {
            var line = 0
            for (str in it) {
                lines[++line * gap] = str
            }
        }
    }

    /**
     * Load from reader
     *
     * Load a new file from a reader object
     *
     * @param reader Reader to load from
     * @param gap    Gap between line numbers
     */
    fun loadFromReader(reader: Reader, gap: Int = 10) {
        clear()

        var line = 0
        reader.forEachLine {
            lines[++line * gap] = it
        }
    }

    /**
     * Unload lines
     *
     * Loads the lines from this file without numbers, as a stream.
     *
     * @return Stream of lines in this file
     */
    fun unloadLines(): Stream<String> =
        lines.toSortedMap { i1, i2 -> Integer.compare(i1, i2) }.values.stream()

    /**
     * Clear
     *
     * Clear all the lines from the file
     */
    fun clear() {
        lines.clear()
    }
}
