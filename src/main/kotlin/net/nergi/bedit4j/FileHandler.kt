package net.nergi.bedit4j

import java.io.BufferedReader
import java.io.FileReader
import java.io.Reader

object FileHandler {
    @JvmStatic
    fun loadFileAsReader(path: String): Reader =
        BufferedReader(FileReader(path))
}
