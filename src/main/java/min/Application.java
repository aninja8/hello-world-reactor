package min;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import reactor.blockhound.BlockHound;


@SpringBootApplication
@ComponentScan({"min"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        //BlockHound.install();
    }
}
