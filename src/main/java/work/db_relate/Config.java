package work.db_relate;

import work.liyue.hadoop.Utils.StatxUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by hzliyue1 on 2017/03/21,16:48.
 */
public class Config {
    
    //news_db_config
    private static final String NEWS_URL = "jdbc:mysql://localhost:4333/news?useUnicode=true&characterEncoding=UTF8&zeroDateTimeBehavior=convertToNull&useSSL=false";
    private static final String NEWS_UID = "recsys";
    private static final String NEWS_PWD = "]^gSQrODZ";
    //recsys_read_db_config
    private static final String RECSYS_READ_URL = "jdbc:mysql://localhost:4331/recsys?useUnicode=true&characterEncoding=UTF8&zeroDateTimeBehavior=convertToNull&useSSL=false";
    private static final String RECSYS_UID = "recsys_read";
    private static final String RECSYS_PWD = "XYjBPSNkPdJ3";
    
    static final double RATIO_GAP = 2.0;
    
    /**
     * 获取type_kind content 和点击率的嵌套map
     *
     * @param sql  输入的获取数据的sql语句
     * @param conn 获取的sql连接对象
     * @return map
     */
    static Map<String, Map<String, Double>> getNestMap(String sql, Connection conn) {
        
        Statement st = null;
        Map<String, Map<String, Double>> nestMap = new HashMap<>();
        try {
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                String content = rs.getString("content");
                double d = Double.parseDouble(rs.getString("ratio"));
                    if (! nestMap.containsKey("poi")) {
                        nestMap.put("poi", new HashMap<>());
                    }
                    nestMap.get("poi").put(content, d);
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
        return nestMap;
    }
    
    static Map<String, String> getTagPoiMap(String sql, Connection conn) {
        
        Statement st = null;
        Map<String, String> nestMap = new HashMap<>();
        try {
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                String tagWord = rs.getString("tagword");
                String poiWord = rs.getString("interest_word");
                String title = rs.getString("title");
                nestMap.put("tagWord", tagWord);
                nestMap.put("poiWord", poiWord);
                nestMap.put("title", title);
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
        return nestMap;
    }
    
    static String getNewsSql(String docId) {
        
        return "select content,recom_num,click_num, (click_num / recom_num )* 100 as ratio from" + " process_of_poi_tag_stat " + "where doc_id = '" + docId + "' and recom_num > 500 and kind_type like 'P%'";
    }
    
    static String getRecsysSql(String docId) {
        
        return "select tagword,interest_word,title from article_attr where docid = '" + docId + "';";
    }
    
    static Set<String> getDistinctDocIdSet(Connection conn) {
        
        Statement st = null;
        Set<String> docSet = new HashSet<>();
        String sql = "select distinct(doc_id) from process_of_poi_tag_stat where recom_num > 500";
        try {
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                String docId = rs.getString("doc_id");
                docSet.add(docId);
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
        return docSet;
    }
    
    static Connection getNewsConnection() {
        
        return StatxUtils.getMysqlConnection(NEWS_URL, NEWS_UID, NEWS_PWD);
    }
    
    static Connection getRecsysConnection() {
        
        return StatxUtils.getMysqlConnection(RECSYS_READ_URL, RECSYS_UID, RECSYS_PWD);
    }
    
    static Set<String> poiResultString(Connection newsConn) {
        //
        Statement st = null;
        Set<String> retSet = new HashSet<>();
        String sql = "select * from (select doc_id,kind_type,content,recom_num,click_num,click_num /recom_num * 100 as ctr from process_of_poi_tag_stat where recom_num > 500 and kind_type like 'P%') B where ctr < 5.0";
        try {
            st = newsConn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                String docId = rs.getString("doc_id");
                String kind = rs.getString("kind_type");
                String content = rs.getString("content");
                String recomCount = rs.getString("recom_num");
                String clkCount = rs.getString("click_num");
                String ctr = rs.getString("ctr");
                String ret = docId + "\t" + kind + "\t" + content + "\t" + recomCount + "\t" + clkCount + "\t" + ctr;
                retSet.add(ret);
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
        return retSet;
    }
    
    static String getTitleAndUrl(String docId , Connection recsysConn) {
        
        Statement st = null;
        StringBuilder ret = new StringBuilder("\t\t");
        String queryTitleSql = "select title from article_attr where docid = '" + docId + "'";
        String queryUrlSql = "select doc_url from recomArticle where docID = '" + docId + "'";
        try {
            st = recsysConn.createStatement();
            ResultSet rs = st.executeQuery(queryTitleSql);
            while (rs.next()) {
                String title = rs.getString("title");
                ret = new StringBuilder(title + "\t");
            }
            
            rs = st.executeQuery(queryUrlSql);
            while (rs.next()) {
                String url = rs.getString("doc_url");
                ret.append(url);
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
        return ret.toString();
    }
}
