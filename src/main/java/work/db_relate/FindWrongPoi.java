package work.db_relate;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
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
import java.sql.SQLException;
import java.util.*;

/**
 * Created by hzliyue1 on 2017/03/21,16:00.
 * 马俊霞的程序
 */
public class FindWrongPoi {
    
    private static BufferedWriter writer = null;
    private static double contentIValue = 0;
    private static double contentJValue = 0;
    //    private static double modelIValue = 0;
    //    private static double modelJValue = 0;
    private static long processNum = 0;
    private static Connection newsConn;
    private static Connection recsysConn;
    
    private static void mainFinc() {
        
        
        String docId = null;
        try {
            String todayString = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
            String fileName = "poi_" + todayString;
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
                String newsSql = Config.getNewsSql(docId, "P");
                Map<String, Map<String, Double>> retMap = Config.getNestMap(newsSql, newsConn, "poi");
                //                String recsysSql = Config.getRecsysSql(docId);
                //                Map<String, String> tagPoiTitleMap = Config.getTagPoiMap(recsysSql, recsysConn);
                
                if (retMap.containsKey("poi") && retMap.get("poi").size() > 1) {
                    Map<String, Double> contentMap = retMap.get("poi");
                    //                    Map<String, Double> poiWordMap = getPoiWordMap(tagPoiTitleMap.get("poiWord"));
                    detail("POI", docId, contentMap, null);
                    //                    System.out.println(contentList);
                }
                //                System.out.println(retMap);
                processNum++;
                if (processNum % 1000 == 0) {
                    System.out.println(processNum);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(docId);
        } finally {
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
        
        //        if (tagWordMap != null) {
//        Set<String> retSet = new HashSet<>();
//        int flag = 0;
        L:
        for (int i = 0; i < contentList.size(); i++) {
            for (int j = i + 1; j < contentList.size(); j++) {
                String contentI = contentList.get(i);
                String contentJ = contentList.get(j);
                if (isReachCondition(contentMap, null, contentI, contentJ)) {
                    String titleAndUrl = Config.getTitleAndUrl(docId, recsysConn);
                    String outRet = docId + "\t" + kind + "\t" + contentI + "\t" + contentIValue + "\t" + contentJ + "\t" + contentJValue + "\t" + titleAndUrl;
        
                    //                        System.out.println(outRet);
                    writer.write(outRet);
                    writer.newLine();
                    writer.flush();
                    break L;
                }
//                contentIValue = contentMap.get(contentI);
//                contentJValue = contentMap.get(contentJ);
//                if (contentIValue > 8.0 || contentJValue > 8.0) {
//                    flag = 1;
//                    break L;
//                }
//                String ret1  = contentI + "\t" + contentIValue;
//                String ret2 = contentJ + "\t" + contentJValue;
//                retSet.add(ret1);
//                retSet.add(ret2);
            }
        }
//        if (flag == 0) {
//            StringBuilder sb = new StringBuilder();
//            for (String s : retSet) {
//                sb.append(s).append("\t");
//            }
//            String titleAndUrl = Config.getTitleAndUrl(docId, recsysConn);
//            String outRet = docId + "\t" + titleAndUrl + "\t" + sb.toString();
//
//            //                        System.out.println(outRet);
//            writer.write(outRet);
//            writer.newLine();
//            writer.flush();
//        }
        //        }
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
    
    private static boolean isReachCondition(Map<String, Double> contentMap, Map<String, Double> poiWordMap, String i,
                                            String j) {
        
        try {
            contentIValue = contentMap.get(i);
            contentJValue = contentMap.get(j);
            
            //            double a = contentIValue - contentJValue;
            //
            //            for (Map.Entry<String, Double> entry : poiWordMap.entrySet()) {
            //                if (entry.getKey().contains(i)) {
            //                    modelIValue = entry.getValue();
            //                }
            //                if (entry.getKey().contains(j)) {
            //                    modelJValue = entry.getValue();
            //                }
            //            }
            //            double b = modelIValue - modelJValue;
            //
            //            double c = a - Config.RATIO_GAP;
            //            double d = a + Config.RATIO_GAP;
            //            return (c > 0 && c * b <= 0) || (d < 0 && d * b <= 0);
            return (contentIValue >= 12.0 && contentJValue <= 5.0) || (contentIValue <= 5.0 && contentJValue >= 12.0);
        } catch (Exception e) {
            System.out.println(i + " " + j);
        }
        return false;
    }
    
    public static void main(String[] args) {
        
        mainFinc();
        //        System.out.println(Config.getDistinctDocIdSet(Config.getNewsConnection()).size());
    }
    
    
}
