package work.db_relate;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import work.liyue.hadoop.Utils.StatxUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hzliyue1 on 2017/03/21,16:00.
 * 解决寻找有问题的docId，这种doc是在推荐过程上面，依据的POI或者MAIN_TAG的点击率与对文章建模生成的POI或者MAIN_TAG权值不符；
 */
public class FindWrongPoiTag {
    
    private static BufferedWriter writer = null;
    private static double contentIValue = 0;
    private static double contentJValue = 0;
    private static double modelIValue = 0;
    private static double modelJValue = 0;
    private static long processNum = 0;
    private static void mainFinc() {
        
        Connection newsConn;
        Connection recsysConn;
        String docId = null;
        try {
            Path p = Paths.get("D:\\beatZa\\2017-file-trans\\0321\\process_docId.txt");
            BufferedReader reader = Files.newBufferedReader(p);
            String parentPath = p.getParent().toString();
            String outFileName = p.getFileName().toString() + ".out";
            Path out = Paths.get(parentPath + File.separator + outFileName);
            writer = Files.newBufferedWriter(out, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            
            newsConn = StatxUtils.getMysqlConnection(Config.NEWS_URL, Config.NEWS_UID, Config.NEWS_PWD);
            recsysConn = StatxUtils.getMysqlConnection(Config.RECSYS_READ_URL, Config.RECSYS_UID, Config.RECSYS_PWD);
            while ((docId = reader.readLine()) != null) {
                //            String docId = "0001_2242013";
                String newsSql = Config.getNewsSql(docId);
                Map<String, Map<String, Double>> retMap = Config.getNestMap(newsSql, newsConn);
                String recsysSql = Config.getRecsysSql(docId);
                Map<String, String> tagPoiMap = Config.getTagPoiMap(recsysSql, recsysConn);
                
                if (retMap.containsKey("poi") && retMap.get("poi").size() > 1) {
                    Map<String, Double> contentMap = retMap.get("poi");
                    Map<String, Double> poiWordMap = getPoiWordMap(tagPoiMap.get("poiWord"));
                    detail("POI", docId, contentMap, poiWordMap);
                    //                    System.out.println(contentList);
                }
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
                        String outRet = docId + "\t" + kind + "\t" + contentI + "\t" + contentIValue + "\t" + modelIValue + "\t" + "\t" + contentJ + "\t" + contentJValue + "\t" + modelJValue;
                        
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
    
    private static Map<String, Double> getPoiWordMap(String poiWord) {
        
        if (StringUtils.isNotBlank(poiWord) && poiWord.length() > 2 && JSONObject.parseObject(poiWord).size() > 0) {
            JSONObject jsonObject = JSONObject.parseObject(poiWord);
            Map<String, Double> scoreMap = new HashMap<>();
            for (String poiKey : jsonObject.keySet()) {
                scoreMap.put(poiKey, jsonObject.getDouble(poiKey));
            }
            return scoreMap;
        }
        return null;
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
