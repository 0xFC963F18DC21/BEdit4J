import net.nergi.bedit4j.BFileTests
import net.nergi.bedit4j.FileHandlerTests
import net.nergi.bedit4j.parser.CommandParserTests
import org.junit.platform.engine.discovery.DiscoverySelectors.selectClass
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder
import org.junit.platform.launcher.core.LauncherFactory
import org.junit.platform.launcher.listeners.SummaryGeneratingListener

object TestSuiteRunner {
    @JvmStatic
    fun main(args: Array<String>) {
        // Make request to test the given classes
        val request = LauncherDiscoveryRequestBuilder.request()
            .selectors(
                selectClass(CommandParserTests::class.java),
                selectClass(BFileTests::class.java),
                selectClass(FileHandlerTests::class.java),
            )
            .build()

        // Make a launcher, add a result listener and execute the request
        val launcher = LauncherFactory.create()
        val listener = SummaryGeneratingListener()

        launcher.registerTestExecutionListeners(listener)
        launcher.execute(request)

        // Collect the results and display them
        val summary = listener.summary

        val successCount = summary.testsSucceededCount
        val testCount = summary.testsFoundCount

        println("Tests passed: $successCount / $testCount")
        summary.failures.forEach { println(it) }
    }
}
