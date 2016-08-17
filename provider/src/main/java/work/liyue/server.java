package work.liyue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

import java.io.IOException;

/**
 * Created by hzliyue1 on 2016/8/18 ,0:46.
 */
@SpringBootApplication
@ImportResource("classpath:dubbo-provider.xml")
public class server {
    public static void main(String[] args) {
        SpringApplication.run(server.class, args);
//        try {
//            System.in.read();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
