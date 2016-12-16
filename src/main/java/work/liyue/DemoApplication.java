package work.liyue;

import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.InvalidProtocolBufferException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import work.liyue.protobuf.Pro.HelloPB;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;

import javax.annotation.Resources;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@SpringBootApplication
//@ImportResource("classpath:conf.xml")
public class DemoApplication {
    
    private static final String PATTERN = "#t[a-c]#";
    
    private static boolean haveInvalidString(String... param) {
        
        for (String s : param) {
            if (s.contains("{") || s.contains("}") || s.contains("[") || s.contains("]")) {
                return true;
            }
        }
        return false;
    }
    
    public static void main(String[] args) throws InvalidProtocolBufferException {
        
        //        HelloPB helloPB = HelloPB.getDefaultInstance();
        //        HelloPB.Builder builder = helloPB.toBuilder();
        //        HelloPB helloPB1 = helloPB.toBuilder().setI(1).build();
        //        byte[] b = helloPB1.toByteArray();
        ////        System.out.println(HelloPB.parseFrom(b));
        //        Set s = new HashSet();
        //        s.add(1);
        //        s.iterator();
        //        List<?> l = new ArrayList<>();
        //        l.addAll(s);
        //
        //        ABC abc = new ABC();
        //        abc.printI();
        
        //        String[] path = {"conf.xml"};
        //        ClassPathXmlApplicationContext context =  new ClassPathXmlApplicationContext(path);
        //        context.close();
        //        double[] d = new double[]{1.0,2.0,3.0};
        //        Percentile percentile = new Percentile(50);
        //        percentile.setData(d);
        //        long a = 3;
        //        long c = 5;
        //        double b = a * 1.0 /c;
    
        long a = 114865;
        double b = a * 1.0;
        String s = null;
        double radix = 1;
        System.out.println(Double.parseDouble("9.0E-4") + 1);
        
        
    }
}
