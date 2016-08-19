package work.liyue.service;

import work.api.SayHello;
import com.alibaba.dubbo.config.annotation.Service;
/**
 * Created by hzliyue1 on 2016/8/18 ,0:30.
 */
@Service()
public class SayHelloImpl implements SayHello {

    @Override
    public String sayHello(String sth) {
        return sth + "  haha , from service";
    }
}
