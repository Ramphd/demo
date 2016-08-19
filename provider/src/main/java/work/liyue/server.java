package work.liyue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by hzliyue1 on 2016/8/18 ,0:46.
 */
@SpringBootApplication
public class server {
    public static void main(String[] args) {
        SpringApplication.run(server.class, args);
        com.alibaba.dubbo.container.Main.main(args);
    }
}
