import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.Instant.now

val start = now().toEpochMilli()

fun CoroutineScope.producer(max: Int = 20) = produce(capacity = 4) {
    var x = 1
    while (x <= max) {
        send(x).also { log("sending $x") }
        delay(10)
        x++
    }
}

fun CoroutineScope.consumer(id: Int, channel: ReceiveChannel<Int>) = launch {
    for (msg in channel) {
        doWork(500)
        log("consumer $id processed $msg")
    }
    timed(start, "consumer is done")
}

fun runner3() = runBlocking {
    log("1) receive from producer")
    val receiveChannel = producer()
    repeat(2) {
        consumer(it, receiveChannel)
    }
    log("2) End of main instruction block (will wait here as in runBlocking statement block)")
}

fun main() = runner3().also { timed(start, "End of main") }
