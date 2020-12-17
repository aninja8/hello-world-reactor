package min;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.inject.Inject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;


@Component
public class Helper {
    private final WebClient webClient;
    List<Integer> nums;
    Logger logger = Logger.getLogger(String.valueOf(Helper.class));
    @Inject
    Helper() throws Exception {
        this.webClient = makeWebClient();
        nums = IntStream.rangeClosed(1, 1000)
                .boxed().collect(Collectors.toList());
    }

    @Inject
    @Qualifier("service-executor")
    private ExecutorService serviceOpsExecutor;

    public void doSomethingAsync(){

        Flux.fromIterable(nums) // Mocking DB operation to retrieve a list based on the incoming request
                .publishOn(Schedulers.fromExecutor(serviceOpsExecutor))
                .flatMap(i->{
                   return callService(); // Mock service call
                })
                .subscribe();

    }

    private Mono<String> callService() {
        return
                webClient
                        .get()
                        .retrieve()
                        .bodyToMono(String.class)
                        .map(str-> {
                            logger.info("Received: "+str);
                            return str;
                        });

    }

    private static WebClient makeWebClient() throws Exception {
        String baseURL = "https://reqres.in/api/users/2";
        SslContext sslContext= SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();

        ConnectionProvider connectionProvider = ConnectionProvider.builder("test")
                .maxConnections(30)
                .maxIdleTime(Duration.ofSeconds(3600))
                .pendingAcquireTimeout(Duration.ofMillis(4500))
                .pendingAcquireMaxCount(-1)
                .fifo()
                .build();


        HttpClient httpClient = HttpClient.create(connectionProvider).secure(t -> t.sslContext(sslContext));
        return WebClient
                .builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(baseURL)
                .build();
    }

}

