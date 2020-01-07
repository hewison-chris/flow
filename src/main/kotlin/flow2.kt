import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.Instant.now

fun runner2() = runBlocking {
    val start = now().toEpochMilli()
    log("1) collect from stream")
    launch {
        streamingInts(20)
            .onEach {
                log("onEach:$it")
            }
            .buffer(5).collect {
                doWork(500)
                log("worked $it")
            }
        timed(start, "flow is exhausted")
    }
    log("2) End of main instruction block (will wait here as in runBlocking statement block)")
}

fun main() = runner2().also { log("End of main") }

