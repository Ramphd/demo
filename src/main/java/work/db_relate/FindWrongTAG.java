package work.db_relate;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by hzliyue1 on 2017/03/29,15:47.
 * 孙芬芬的程序
 */
public class FindWrongTAG {
    
    private static BufferedWriter writer = null;
    private static double contentIValue = 0;
    private static double contentJValue = 0;
    private static double modelIValue = 0;
    private static double modelJValue = 0;
    private static long processNum = 0;
    private static Connection newsConn;
    private static Connection recsysConn;
    
    private static void mainFinc() {
       
        String docId = null;
        try {
            String todayString = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
            String fileName = "tag_" + todayString;
            Path p = Paths.get("D:\\beatZa\\2017-file-trans\\poitagstat\\" + fileName);
            //            BufferedReader reader = Files.newBufferedReader(p);
            //            String parentPath = p.getParent().toString();
            //            String outFileName = p.getFileName().toString() + ".out";
            //            Path out = Paths.get(parentPath + File.separator + outFileName);
            writer = Files.newBufferedWriter(p, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    
            newsConn = Config.getNewsConnection();
            recsysConn = Config.getRecsysConnection();
            for (String s : Config.getDistinctDocIdSet(newsConn)) {
                docId = s;
                //            String docId = "0001_2242013";
                String newsSql = Config.getNewsSql(docId,"M");
                Map<String, Map<String, Double>> retMap = Config.getNestMap(newsSql, newsConn,"tag");
                String recsysSql = Config.getRecsysSql(docId);
                Map<String, String> tagPoiMap = Config.getTagPoiMap(recsysSql, recsysConn);
                
//                if (retMap.containsKey("poi") && retMap.get("poi").size() > 1) {
//                    Map<String, Double> contentMap = retMap.get("poi");
//                    Map<String, Double> poiWordMap = getPoiWordMap(tagPoiMap.get("poiWord"));
//                    detail("POI", docId, contentMap, poiWordMap);
//                    //                    System.out.println(contentList);
//                }
                if (retMap.containsKey("tag") && retMap.get("tag").size() > 1) {
                    Map<String, Double> contentMap = retMap.get("tag");
                    Map<String, Double> tagWordMap = getTagWordMap(tagPoiMap.get("tagWord"));
                    detail("TAG", docId, contentMap, tagWordMap);
                }
                //                System.out.println(retMap);
                processNum ++;
                if(processNum % 1000 == 0){
                    System.out.println(processNum);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(docId);
        }finally {
            if (newsConn != null) {
                try {
                    newsConn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (recsysConn != null) {
                try {
                    recsysConn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
        System.out.println("done!");
    }
    
    private static void detail(String kind, String docId, Map<String, Double> contentMap,
                               Map<String, Double> tagWordMap) throws IOException {
        
        List<String> contentList = new ArrayList<>(contentMap.keySet());
        
        if (tagWordMap != null) {
            L:
            for (int i = 0; i < contentList.size(); i++) {
                for (int j = i + 1; j < contentList.size(); j++) {
                    String contentI = contentList.get(i);
                    String contentJ = contentList.get(j);
                    if (isReachCondition(contentMap, tagWordMap, contentI, contentJ)) {
                        String outRet = docId + "\t" + kind + "\t" + contentI + "\t" + contentIValue + "\t" + modelIValue + "\t"+ contentJ + "\t" + contentJValue + "\t" + modelJValue;
                        
                        //                        System.out.println(outRet);
                        writer.write(outRet);
                        writer.newLine();
                        writer.flush();
                        break L;
                    }
                }
            }
        }
    }
    
    private static Map<String, Double> getTagWordMap(String tagWord) {
        
        if (StringUtils.isNotBlank(tagWord) && tagWord.length() > 4 && JSONObject.parseArray(tagWord).size() > 0) {
            Map<String, Double> scoreMap = new HashMap<>();
            for (Object object : JSONObject.parseArray(tagWord)) {
                scoreMap.put(((JSONObject) object).getString("fullPath"),
                             Double.parseDouble(((JSONObject) object).getString("score")));
            }
            return scoreMap;
        }
        return null;
    }
    
    private static boolean isReachCondition(Map<String, Double> contentMap, Map<String, Double> poiWordMap, String i,
                                            String j) {
        
        try {
            contentIValue = contentMap.get(i);
            contentJValue = contentMap.get(j);
            
            double a = contentIValue - contentJValue;
            
            for (Map.Entry<String, Double> entry : poiWordMap.entrySet()) {
                if (entry.getKey().contains(i)) {
                    modelIValue = entry.getValue();
                }
                if (entry.getKey().contains(j)) {
                    modelJValue = entry.getValue();
                }
            }
            double b = modelIValue - modelJValue;
            
            double c = a - Config.RATIO_GAP;
            double d = a + Config.RATIO_GAP;
            return (c > 0 && c * b <= 0) || (d < 0 && d * b <= 0);
        } catch (Exception e) {
            System.out.println(i + " " + j);
        }
        return false;
    }
    
    public static void main(String[] args) {
        
        mainFinc();
    }
}

