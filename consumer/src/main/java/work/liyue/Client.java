package work.liyue;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Created by hzliyue1 on 2016/8/18 ,0:37.
 */
@SpringBootApplication
public class Client {
    @Reference(interfaceName = "sayHello")
    public static void main(String[] args) {
        ConfigurableApplicationContext run =   SpringApplication.run(Client.class, args);
        ServiceRecv bean = run.getBean(ServiceRecv.class);
        System.out.println(bean.sayHello.sayHello("here it is"));
    }
}
