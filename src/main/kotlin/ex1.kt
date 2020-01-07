import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import java.time.Instant.now

fun streaming() = (1..5).asFlow()

suspend fun doWork() = delay(200).also { log("working hard") }

fun log(message: Any="") = println("${now()}: $message")

suspend fun main() {
    println("example1")
    streaming().collect {
        log(it)
        doWork()
    }
}
