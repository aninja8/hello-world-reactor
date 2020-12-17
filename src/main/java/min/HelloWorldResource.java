package min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;


@RestController
public class HelloWorldResource {

    @Autowired
    Helper helper;

    @GetMapping("/hello")
    public Mono<String> sayHello() {

        //just ack and process in the background

        Mono.fromRunnable(() -> helper.doSomethingAsync())
                .subscribeOn(Schedulers.boundedElastic())  // delegate to proper thread to not block main flow
                .subscribe();

        return Mono.just("Hello World");

    }
}
