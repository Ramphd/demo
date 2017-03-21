package work.db_relate;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import work.liyue.hadoop.Utils.StatxUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

/**
 * Created by hzliyue1 on 2017/03/21,16:00.
 * 解决寻找有问题的docId，这种doc是在推荐过程上面，依据的POI或者MAIN_TAG的点击率与对文章建模生成的POI或者MAIN_TAG权值不符；
 */
public class FindWrongPoiTag {
    
    private static String getCategoryQuery(String sql, Connection conn) {
        
        Statement st = null;
        String ret = "null";
        try {
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                ret = rs.getString("kind_type") + " " + rs.getString("content");
                System.out.println(ret);
            }
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null) st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ret;
    }
    
    private static void mainFinc() {
        
        Connection conn;
        //        String url = "jdbc:mysql://localhost:4331/recsys?useUnicode=true&characterEncoding=UTF8&zeroDateTimeBehavior=convertToNull&useSSL=false";
        //        String uid = "recsys_read";
        //        String pwd = "XYjBPSNkPdJ3";
        String url = "jdbc:mysql://localhost:4333/news?useUnicode=true&characterEncoding=UTF8&zeroDateTimeBehavior=convertToNull&useSSL=false";
        String uid = "recsys";
        String pwd = "]^gSQrODZ";
        //        String line = null;
        try {
            //            Path p = Paths.get("D:\\beatZa\\2017-file-trans\\0317\\hejun");
            //            BufferedReader reader = Files.newBufferedReader(p);
            //            String parentPath = p.getParent().toString();
            //            String outFileName = p.getFileName().toString() + ".out";
            //            Path out = Paths.get(parentPath + File.separator + outFileName);
            //            BufferedWriter writer = Files
            //                    .newBufferedWriter(out, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            
            conn = StatxUtils.getMysqlConnection(url, uid, pwd);
            //            while ((line = reader.readLine()) != null) {
            //                String items[] = line.split("\t");
            //                String docId = items[0];
            String docId = "0001_2241507";
            String sql = "select kind_type,content,recom_num,click_num, (click_num / recom_num )* 100 as ratio from process_of_poi_tag_stat where doc_id = '"+docId+"' and recom_num > 500";
            
            String ret = getCategoryQuery(sql, conn);
            //                writer.write(line + "\t" + ret);
            //                writer.newLine();
            //                writer.flush();
            //            }
        } catch (Exception e) {
            e.printStackTrace();
            //            System.out.println(line);
        }
        
        System.out.println("done!");
    }
    
    public static void main(String[] args) {
        
        mainFinc();
        Set<String> s = new HashSet<>();
    
        List<String> l = new ArrayList<>(s);
        l.addAll(s);
    }
    
    
}
