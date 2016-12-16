package work.liyue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import work.api.SayHello;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by hzliyue1 on 2016/8/18 ,0:37.
 */
@RunWith(SpringJUnit4ClassRunner.class)
//@ImportResource("classpath:dubbo-comsumer.xml")
@SpringBootApplication
public class Client {

//    @Autowired
//    SayHello sayHello;
    @Autowired
    SubClass subClass;

    
    @Test
    public void say(){
//        String[] args = {""};
//        ConfigurableApplicationContext run =   SpringApplication.run(Client.class, args);
//        System.out.println(sayHello.sayHello("adadasd"));
        subClass.sss();
    }
    
    
    
    
//    public static void main(String[] args) {
//        ConfigurableApplicationContext run =  SpringApplication.run(Client.class, args);
////
////        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:dubbo-comsumer.xml");
////        SayHello sayHello = (SayHello)context.getBean("sayHello");
////
////        System.out.println(sayHello.sayHello("asdasd"));
//    }
}
