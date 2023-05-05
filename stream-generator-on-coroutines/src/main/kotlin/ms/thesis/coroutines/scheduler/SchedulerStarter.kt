package ms.thesis.coroutines.scheduler

import io.github.oshai.KotlinLogging
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import ms.thesis.common.record.RandomRecordGenerator
import ms.thesis.coroutines.config.GeneratorConfig
import ms.thesis.coroutines.kafka.KafkaConfig
import ms.thesis.coroutines.kafka.KafkaConfig.Companion.INPUT_TOPIC
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

private val logger = KotlinLogging.logger {}

@Configuration
class SchedulerStarter {

    @Bean
    fun startGenerator(
        context: ApplicationContext,
        kafkaProducer: ReactiveKafkaProducerTemplate<String?, ByteArray>,
        config: GeneratorConfig.Props,
        kafkaConfig: KafkaConfig
    ) = CommandLineRunner {
        runBlocking {
            logger.info("""
                Running the generator with the following parameters:
                $config
            """.trimIndent())

            val counter = AtomicInteger(0)
            val numOfJobs = config.numOfJobs.toInt()
            val period = config.period.toLong()
            val executionTime = config.executionTimeSec.toLong()
            val seed = SplittableRandom().nextLong() //0x2e3fac4f58fc98b4L

            val sendNewRecord: (generator: RandomRecordGenerator) -> Unit = {
                val newRecord = it.get()
                kafkaProducer.send(INPUT_TOPIC, newRecord.data()).subscribe()
                if (config.showGeneratedRecords.toBooleanStrict()) {
                    logger.info("Record: $newRecord")
                }
            }

            val schedulers = List(numOfJobs) { sourceId -> PeriodicJobScheduler(
                    counter,
                    sendNewRecord,
                    RandomRecordGenerator(seed + sourceId),
                    period
            ) }

            schedulers.forEach { it.run() }
            delay(TimeUnit.SECONDS.toMillis(executionTime))
            schedulers.forEach { it.stop() }

            logger.info("Records count: ${counter.get()}")
            SpringApplication.exit(context)
        }
    }
}