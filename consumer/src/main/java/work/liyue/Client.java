package work.liyue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;
import work.liyue.recvmodel.ServiceRecv;

import static work.liyue.recvmodel.ServiceRecv.getSayHello;

/**
 * Created by hzliyue1 on 2016/8/18 ,0:37.
 */
@SpringBootApplication
@ImportResource("classpath:dubbo-comsumer.xml")
public class Client {
    private static ServiceRecv serviceRecv = new ServiceRecv();
    public static void main(String[] args) {
        ConfigurableApplicationContext run =   SpringApplication.run(Client.class, args);
//        ServiceRecv bean = run.getBean(ServiceRecv.class);
//
        System.out.println(getSayHello().sayHello("here i am "));
    }
}
