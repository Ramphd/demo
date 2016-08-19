package work.liyue.recvmodel;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;
import work.api.SayHello;

/**
 * Created by hzliyue1 on 2016/8/18 ,1:48.
 */
@Component
public class ServiceRecv {

    @Reference()
    private static SayHello sayHello;

    public static SayHello getSayHello() {
        return sayHello;
    }
}
