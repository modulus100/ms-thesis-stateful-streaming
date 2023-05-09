package ms.thesis.log.state.config;

import com.dynatrace.hash4j.hashing.Hashing;
import ms.thesis.common.matcher.HashBasedMatchingRule;
import ms.thesis.common.matcher.MatcherService;
import ms.thesis.common.matcher.SimpleMatcherService;
import ms.thesis.common.record.ByteArrayRecord;
import ms.thesis.log.state.consumer.CountingConsumer;
import ms.thesis.log.state.consumer.State;
import ms.thesis.log.state.consumer.StatefulConsumer;
import ms.thesis.log.state.record.DynatraceRecordSerde;
import ms.thesis.log.state.record.StateSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.util.Map;
import java.util.SplittableRandom;

@Configuration
@EnableKafka
@EnableKafkaStreams
public class PipelineConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(PipelineConfig.class);

    public static final String IN_TOPIC = "input";
    public static final String OUT_TOPIC = "output";


    @Bean
    public StatefulConsumer statefulConsumer() {
        return new CountingConsumer("counter");
    }

    @Bean
    public MatcherService matcherService() {
        final long seed = new SplittableRandom().nextLong();
        long ruleCounter = 0;
        final MatcherService matcherService = new SimpleMatcherService();
        var selectivities = Map.ofEntries(
                Map.entry(0.01, 100),
                Map.entry(0.001, 1000)
        );

        for (Map.Entry<Double, Integer> entry : selectivities.entrySet()) {
            final int numRules = entry.getValue();
            final double selectivity = entry.getKey();
            for (int i = 0; i < numRules; i++) {
                matcherService.addMatchingRule(
                        "consumer_" + ruleCounter,
                        new HashBasedMatchingRule(
                                Hashing.komihash4_3()
                                        .hashStream()
                                        .putLong(seed)
                                        .putLong(ruleCounter)
                                        .getAsLong(),
                                selectivity
                        ));
                ruleCounter++;
            }
        }

        return matcherService;
    }

    @Bean
    public KStream<String, Long> aggregationPipeline(
            StreamsBuilder kStreamBuilder,
            MatcherService matcherService,
            StatefulConsumer consumer) {
        final VarHandle longVarHandle = MethodHandles.byteArrayViewVarHandle(long[].class, ByteOrder.LITTLE_ENDIAN);
        KStream<String, Long> stream = kStreamBuilder.stream(IN_TOPIC, Consumed.with(Serdes.String(), Serdes.ByteArray()))
            .map((k, v) -> new KeyValue<>(k, new ByteArrayRecord(v)))
            .flatMap((k, v) -> matcherService.match(v).map(con -> KeyValue.pair(con.getKey(), con.getValue())).toList())
            .groupByKey(Grouped.with(Serdes.String(), new DynatraceRecordSerde()))
            .aggregate(
                State::new,
                (key, value, aggregate) -> consumer.accept(value, aggregate),
                Materialized.with(Serdes.String(), new StateSerde()))
            .toStream()
            .mapValues(state -> (long) longVarHandle.get(state.getData(), 0));

        stream.to(OUT_TOPIC, Produced.with(Serdes.String(), Serdes.Long()));
        return stream;
    }
}
