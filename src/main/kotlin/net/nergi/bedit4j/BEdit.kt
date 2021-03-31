package net.nergi.bedit4j

import input
import net.nergi.bedit4j.parser.TokenCommand
import net.nergi.bedit4j.parser.TokenGroup
import net.nergi.bedit4j.parser.TokenLine
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class BEdit(private var path: String?, gap: Int?) {
    companion object {
        // A list of allowed commands
        @JvmStatic
        private val allowedCommands: Set<String> by lazy {
            setOf("exec", "list", "listr", "pushf", "pushb", "save", "quit")
        }

        @JvmStatic
        val cmdExecutor: ExecutorService by lazy {
            Executors.newSingleThreadExecutor()
        }
    }

    // This is the file that this editor instance uses
    private var internalFile: BFile = BFile()

    // Has the user saved since the last destructive modification?
    // Starts on true because no changes have been made
    private var hasSaved = true

    init {
        if (path != null) {
            val reader = FileHandler.loadFileAsReader(path!!)
            if (gap != null) {
                internalFile.loadFromReader(reader, gap)
            } else {
                internalFile.loadFromReader(reader)
            }
        }
    }

    /**
     * Accept command
     *
     * Accept a command token group to execute.
     *
     * @param cmd Command group to execute
     * @return true if the program should exit. false otherwise.
     */
    @Throws(IllegalArgumentException::class)
    fun acceptCommand(cmd: TokenGroup?): Boolean {
        if (cmd == null) {
            return false
        }

        val (lineOrCmd, args) = cmd

        when (lineOrCmd) {
            is TokenLine -> {
                internalFile.modifyContent(lineOrCmd.line, args[0].getContent())
                hasSaved = false
            }
            is TokenCommand -> {
                try {
                    if (lineOrCmd.cmd !in allowedCommands) {
                        throw IllegalArgumentException("Not a valid command")
                    }

                    when (lineOrCmd.cmd) {
                        "exec" -> {
                            execHandler(args.joinToString(separator = " ") { it.getContent() })
                            hasSaved = false
                        }
                        "save" -> saveHandler()
                        "quit" -> {
                            if (!hasSaved) {
                                print("Are you sure? [Y]es/[N]o\n>>> ")
                                return (input.readLine() ?: "").toLowerCase().startsWith("y")
                            }

                            return true
                        }
                        "pushf" -> {
                            argCheck(args.size, 2)
                            internalFile.pushLinesForwardFrom(
                                args[0].getContent().toInt(),
                                args[1].getContent().toInt(),
                            )
                        }
                        "pushb" -> {
                            argCheck(args.size, 2)
                            internalFile.pushLinesBackFrom(
                                args[0].getContent().toInt(),
                                args[1].getContent().toInt(),
                            )
                        }
                        "list" -> {
                            argCheck(args.size, 0, 1, 2)
                            val lines = when (args.size) {
                                0 -> internalFile.getRawLines()
                                1 -> internalFile.getRawLines(args[0].getContent().toInt())
                                2 -> internalFile.getRawLines(
                                    args[0].getContent().toInt(),
                                    args[1].getContent().toInt(),
                                )
                                else -> listOf()
                            }

                            lines.forEach(::println)
                        }
                        "listr" -> {
                            argCheck(args.size, 0, 1, 2)
                            val lines = when (args.size) {
                                0 -> internalFile.getActualLines()
                                1 -> internalFile.getActualLines(args[0].getContent().toInt())
                                2 -> internalFile.getActualLines(
                                    args[0].getContent().toInt(),
                                    args[1].getContent().toInt(),
                                )
                                else -> listOf()
                            }

                            lines.forEach(::println)
                        }
                        else -> Unit
                    }
                } catch (e: IllegalArgumentException) {
                    println(e.message)
                }
            }
        }

        return false
    }

    @Throws(IllegalArgumentException::class)
    private fun argCheck(amt: Int, vararg allowed: Int) {
        if (amt !in allowed) {
            throw IllegalArgumentException("Not an allowed number of arguments: $amt")
        }
    }

    private val isWindows: Boolean by lazy {
        System.getProperty("os.name").toLowerCase().startsWith("windows")
    }

    /**
     * Exec handler
     *
     * Executes an external command, inheriting from the current instance's runtime.
     *
     * @param exCommand Command to execute
     * @return Return code of the program
     */
    private fun execHandler(exCommand: String): Int {
        println("Running [ $exCommand ]...")

        val proc = Runtime.getRuntime().exec(
            if (isWindows) "CMD /C $exCommand" else "sh -c exCommand"
        )

        cmdExecutor.submit {
            val inpStrm = proc.inputStream
            BufferedReader(InputStreamReader(inpStrm)).lines().forEach(::println)
        }

        return proc.waitFor()
    }

    /**
     * Save handler
     *
     * Handles the saving of files.
     */
    private fun saveHandler() {
        if (path != null) {
            FileHandler.saveFileFromStream(internalFile.unloadLines(), path!!)
        } else {
            var path: String?
            do {
                println("Input a filepath to save this file to:")
                path = input.readLine()

                if (path == null) {
                    println("\nNot a path.\n")
                }
            } while (path == null)
            println()

            this.path = path
            FileHandler.saveFileFromStream(internalFile.unloadLines(), path)
        }

        hasSaved = true
    }
}
