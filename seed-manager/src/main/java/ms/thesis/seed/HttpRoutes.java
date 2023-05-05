package ms.thesis.seed;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.concurrent.ConcurrentLinkedQueue;

import static ms.thesis.seed.Seed.seedSetOne;
import static ms.thesis.seed.Seed.seedSetTwo;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class HttpRoutes {
    private final ConcurrentLinkedQueue<Long> consumerQueue = new ConcurrentLinkedQueue<>(seedSetOne);
    private final ConcurrentLinkedQueue<Long> producerQueue = new ConcurrentLinkedQueue<>(seedSetTwo);

    record SeedResponse(Long seed) {}

    private SeedResponse gimmeSeed(Long seed) {
        if (seed == null) {
            throw new RuntimeException("Lack of consumer seeds");
        }

        return new SeedResponse(seed);
    }

    @Bean
    RouterFunction<ServerResponse> consumerSeed() {
        return route(GET("/consumer"),
                req -> ok().body(
                        Mono.just(gimmeSeed(consumerQueue.poll())), SeedResponse.class));
    }

    @Bean
    RouterFunction<ServerResponse> producerSeed() {
        return route(GET("/producer"),
                req -> ok().body(
                        Mono.just(gimmeSeed(producerQueue.poll())), SeedResponse.class));
    }
}
