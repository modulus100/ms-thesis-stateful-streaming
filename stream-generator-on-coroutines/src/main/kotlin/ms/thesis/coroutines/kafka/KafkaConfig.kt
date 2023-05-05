package ms.thesis.coroutines.kafka

import ms.thesis.coroutines.config.GeneratorConfig
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import reactor.kafka.sender.SenderOptions


@Configuration
class KafkaConfig(
    private val config: GeneratorConfig.Props
) {

    @Bean
    fun topic() = NewTopic(INPUT_TOPIC, config.partitions.toInt(), config.topicReplicationFactor.toShort())

    @Bean
    fun reactiveKafkaProducerTemplate(properties: KafkaProperties): ReactiveKafkaProducerTemplate<String?, ByteArray> {
        val props = properties.buildProducerProperties()
        return ReactiveKafkaProducerTemplate(SenderOptions.create(props))
    }

    companion object {
        const val INPUT_TOPIC = "inbound-stream"
    }
}