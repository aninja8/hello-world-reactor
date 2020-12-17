package tests;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.function.Try;
import org.springframework.web.reactive.function.client.WebClient;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;


public class FooTest {
    @Test
    public void testSayHello() {

        ExecutorService executorService = Executors.newFixedThreadPool(1000);

        List<Integer> list = IntStream.rangeClosed(1, 1000)
                .boxed().collect(Collectors.toList());

        List<? extends Future<?>> futures = list
                .stream()
                .map(i -> executorService.submit(() -> call()))
                .collect(Collectors.toList());

        futures
                .stream()
                .forEach(future -> {
                    try {
                        future.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                });
    }

    private void call() {
        String baseUrl = "http://localhost:8080/hello" ;
          WebClient.create(baseUrl)
                  .get()
                  .retrieve()
                  .bodyToMono(String.class)
                  .log()
                  .subscribe();

    }
}
