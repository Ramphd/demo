package work.liyue.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by hzliyue1 on 2016/8/19 000019,18:33.
 */
@Configuration
@ImportResource("classpath:dubbo-provider.xml")
public class ServerConfiguration {
}
