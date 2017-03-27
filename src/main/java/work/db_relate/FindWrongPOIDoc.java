package work.db_relate;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.util.Date;
import java.util.Set;

/**
 * Created by hzliyue1 on 2017/03/22,14:37.
 */
public class FindWrongPOIDoc {
    
    private static void getWrongPOIDoc()  {
        
        Connection newsConn = Config.getNewsConnection();
        Connection recsysConn = Config.getRecsysConnection();
        Set<String> poiRetSet = Config.poiResultString(newsConn);
        String todayString = DateFormatUtils.format(new Date(),"yyyy-MM-dd");
        String fileName = "poi_" + todayString;
        Path out = Paths.get("D:\\beatZa\\2017-file-trans\\poitagstat\\"+fileName);
        String lineInfo = "";
        try {
            BufferedWriter writer = Files
                    .newBufferedWriter(out, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            long lineCount = 0;
            for (String s : poiRetSet) {
                lineInfo = s;
                String docId = StringUtils.substringBefore(s, "\t");
                String titleAndUrl = Config.getTitleAndUrl(docId, recsysConn);
                String ret = s + "\t" + titleAndUrl;
                writer.write(ret);
                writer.newLine();
                writer.flush();
                lineCount++;
                if (lineCount % 1000 == 0) {
                    System.out.println(lineCount);
                }
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(lineInfo);
        }
    }
    
    public static void main(String[] args)  {
        
        getWrongPOIDoc();
//        System.out.println(DateFormatUtils.format(new Date(),"yyyy-MM-dd"));
    }
}
