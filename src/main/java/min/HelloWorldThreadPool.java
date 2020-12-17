package min;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class HelloWorldThreadPool {
    @Bean(name = "service-executor")
    public ThreadPoolExecutor getServiceOpsExecutor() {
        return (ThreadPoolExecutor) Executors.newFixedThreadPool(30);
    }

}
