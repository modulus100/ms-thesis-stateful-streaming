package ms.thesis.coroutines.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Configuration


@Configuration
@ConfigurationPropertiesScan("ms.thesis.coroutines.config")
class GeneratorConfig {

    @ConfigurationProperties(prefix = "app")
    data class Props(
        val executionTimeSec: String,
        val numOfJobs: String,
        val period: String,
        val partitions: String,
        val topicReplicationFactor: String,
        val showGeneratedRecords: String
    ) {
        override fun toString(): String =
            """
                Kafka partitions: $partitions
                Job period (ms): $period
                Number of jobs: $numOfJobs
                Execution time (s): $executionTimeSec
                Topic replication factor: $topicReplicationFactor
                Show records: $showGeneratedRecords
            """.trimIndent()
    }
}
