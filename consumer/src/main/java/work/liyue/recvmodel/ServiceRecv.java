package work.liyue.recvmodel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import work.api.SayHello;
import work.liyue.Client;

/**
 * Created by hzliyue1 on 2016/8/18 ,1:48.
 */




@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=Client.class)
public class ServiceRecv {

    @Autowired
    private  SayHello sayHello;

    public  SayHello getSayHello() {
        return sayHello;
    }

    @Test
    public void say(){
        System.out.println(sayHello.sayHello("adadasd"));
    }


}
