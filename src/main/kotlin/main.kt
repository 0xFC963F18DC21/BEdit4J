import net.nergi.bedit4j.BEdit
import net.nergi.bedit4j.parser.CommandParser
import java.io.BufferedReader
import java.io.InputStreamReader

// Do not close this reader unless the program has terminated
val input: BufferedReader by lazy {
    BufferedReader(InputStreamReader(System.`in`))
}

fun main(args: Array<String>) {
    val path = if (args.size > 0) args[0] else ""
    val gaps = if (args.size > 1) args[1].toInt() else 10

    println("Welcome to BEdit4J v0.0.1")
    if (path != "") {
        println("Opening $path")
    }

    val editor = BEdit(if (path == "") null else path, gaps)
    do {
        var cmd: String?
        do {
            print("> ")
            cmd = input.readLine()
        } while (cmd == null)

        val parsed = CommandParser.tokenise(cmd)
        if (editor.acceptCommand(parsed)) {
            break
        }
    } while (true)

    input.close()
    BEdit.cmdExecutor.shutdown()
}
