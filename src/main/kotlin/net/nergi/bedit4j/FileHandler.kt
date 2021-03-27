package net.nergi.bedit4j

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.FileReader
import java.io.FileWriter
import java.io.Reader
import java.util.stream.Stream

object FileHandler {
    @JvmStatic
    fun loadFileAsReader(path: String): Reader =
        BufferedReader(FileReader(path))

    @JvmStatic
    fun saveFileFromStream(stream: Stream<String>, path: String) {
        val writer = BufferedWriter(FileWriter(path))
        writer.use { thiz ->
            stream.forEach {
                thiz.write(it)
                thiz.newLine()
            }
        }
    }
}
