package ms.thesis.coroutines.scheduler

import kotlinx.coroutines.*
import ms.thesis.common.record.RandomRecordGenerator
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.CoroutineContext

class PeriodicJobScheduler(
    private val counter: AtomicInteger,
    private val sendRecord: (generator: RandomRecordGenerator) -> Unit,
    private val generator: RandomRecordGenerator,
    private val period: Long
) : CoroutineScope {
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job

    fun run() {
        launch {
            while (isActive) {
                counter.incrementAndGet()
                sendRecord(generator)
                delay(period)
            }
        }
    }

    fun stop() {
        job.cancel()
    }
}