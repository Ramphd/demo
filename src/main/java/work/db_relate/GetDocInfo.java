package work.db_relate;

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

/**
 * Created by hzliyue1 on 2017/03/15,19:30.
 */
public class GetDocInfo {
    private static String getCategoryQuery(String sql, Connection conn) {
        
        Statement st = null;
        String ret = "null";
        try {
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                ret = rs.getString("category");
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
    public static void main(String[] args) {
        
        Connection conn;
        String url = "jdbc:mysql://localhost:4331/recsys?useUnicode=true&characterEncoding=UTF8&zeroDateTimeBehavior=convertToNull&useSSL=false";
        String uid = "recsys_read";
        String pwd = "XYjBPSNkPdJ3";
        String line = null;
        try {
            Path p = Paths.get("D:\\beatZa\\2017-file-trans\\0317\\hejun");
            BufferedReader reader = Files.newBufferedReader(p);
            String parentPath = p.getParent().toString();
            String outFileName = p.getFileName().toString() + ".out";
            Path out = Paths.get(parentPath + File.separator + outFileName);
            BufferedWriter writer = Files
                    .newBufferedWriter(out, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            
            conn = StatxUtils.getMysqlConnection(url, uid, pwd);
            while ((line = reader.readLine()) != null) {
                String items[] = line.split("\t");
                String docId = items[0];
                String sql = "SELECT category FROM recsys.article_attr where docid = '"+ docId+"'";
                
                String ret = getCategoryQuery(sql, conn);
                writer.write(line + "\t" + ret);
                writer.newLine();
                writer.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(line);
        }
        
        System.out.println("done!");
    }
}
