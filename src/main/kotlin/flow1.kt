import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.Instant.now

fun streamingInts(max: Long = 10) = (1..max).asFlow()

suspend fun doWork(ms: Long = 200) = delay(ms).also { log("working hard for $ms milliseconds") }

fun log(message: Any = "") = println("[${Thread.currentThread().name}] ${now()}: $message")

fun timed(start: Long, message: Any = "") =
    println("[${Thread.currentThread().name}] ${now()}: $message after ${now().toEpochMilli() - start} millis!")

fun runner1() = runBlocking {
    val start = now().toEpochMilli()
    log("1) tick every second")
    launch {
        repeat(5) {
            log("I'm not blocked as I tick every second")
            doWork(1000)
        }
        timed(start,"Done with this ticking!")
    }
    log("2) collect from stream off main thread")
    launch {
        streamingInts().collect {
            doWork()
            log(it)
        }
        timed(start, "flow is exhausted")
    }
    log("3) End of main instruction block (will wait here as in runBlocking statement block)")
}

fun main() = runner1().also { log("End of main") }

