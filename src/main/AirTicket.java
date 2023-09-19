package main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Thorn
 * @Date: 2020/8/17 22:49
 * @Description:每日监控低价机票
 */
public class AirTicket {
    private static Logger logger = LogManager.getLogger(AirTicket.class);
    /**
     * Description:〈通过get方式打开链接〉
     * @param httpurl ：要访问的链接
     * @return: java.net.HttpURLConnection
     */
    public static HttpURLConnection OpenUrl(String httpurl) {
        HttpURLConnection connection = null;
        try {
            //创建url对象
            URL url = new URL(httpurl);
            //打开连接
            connection = (HttpURLConnection) url.openConnection();
            //设置连接方式
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(6000);
            //开始连接
            connection.connect();
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
        }
        return connection;
    }
    /**
     * Description:〈查询机票价格〉
     * @param key：Server酱key
     * @param FromCityCode：当前位置代号(去同程搜索位置就会显示，例：HET，必须大写)
     * @param ToCityCode：目的位置代号
     * @param Date：日期
     * @param FromAdd：当前位置（简体中文全称）
     * @param ToAdd：想要去的地方
     * @return: java.lang.String
     */
    public static String GetHtml(String key, String FromCityCode, String ToCityCode, String Date, String FromAdd, String ToAdd){
        try {
            //特殊字符进行url编码
            FromAdd = URLEncoder.encode(FromAdd,"utf-8");
            ToAdd = URLEncoder.encode(ToAdd,"utf-8");
            String openurl = "https://www.ly.com/flights/itinerary/oneway/"+FromCityCode+"-"+ToCityCode+"?date="+Date+"&fromairport=&toairport=&from="+FromAdd+"&to="+ToAdd+"&childticket=0,0";

            HttpURLConnection connection = OpenUrl(openurl);
            //获取连接状态码
            int code = connection.getResponseCode();
            if (code>=200 || code<=300){
                logger.info("Connect Success!");
                //获取输入流
                InputStream in = connection.getInputStream();
                //读取输入流，使用bufferedReader提高效率
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                //定义存放html界面的内容
                String len = null;
                StringBuffer html = new StringBuffer();
                //循环获取html界面
                while ((len = bufferedReader.readLine()) != null){
                    html.append(len);
                }
                return html.toString();
            }
            else {
                SendMessage(key,"错误","访问错误，错误码："+code);
                logger.error("访问错误，错误码："+code);
            }
        } catch (MalformedURLException e) {
            logger.error(e.getMessage(),e);
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }
    /**
     * Description:〈使用Server酱微信提醒实时播报信息〉
     * @param Message ：文本信息
     * @return: void
     */
    public static void SendMessage(String key ,String Text,String Message){
        try {
            Text = URLEncoder.encode(Text,"utf-8");
            Message = URLEncoder.encode(Message,"utf-8");
            String url = "https://sc.ftqq.com/"+key+".send?text="+Text+"&desp="+Message;
            HttpURLConnection connection = OpenUrl(url);
            int code = connection.getResponseCode();
            if (code>=200 || code<=300)
            {
                logger.info("微信消息发送成功");
            }
            else {
                logger.info("访问错误，错误码："+code);
            }

        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(),e);
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
        }
    }
    /**
     * Description:〈使用jsoup解析html〉
     * @param html 要解析的界面
     * @return: java.util.ArrayList<java.lang.String>
     */
    public static Map<String, String> ParHtml(String html){
        Map<String,String> value = new HashMap<>();
        Document parse = Jsoup.parse(html);
        Elements selected = parse.getElementsByClass("selected");
        for (Element se:selected
        ) {
            Elements span = se.getElementsByTag("span");
            value.put(span.get(0).text(),span.get(1).text());
        }
        return value;
    }
    /**
     * Description:〈将日期切割〉
     * @param date ：获取的日期字符串
     * @return: java.lang.String[]
     */
    public static String[] SplitDate(String date){
        String[] allDate = date.split(",");
        return allDate;
    }
    public static Ticket ParseConfiguration(String filename){
        //获取xml的path
        //解析xml
        Document document = null;
        try {
            document = Jsoup.parse(new File(filename),"utf-8");
            //获取机票信息
            //1.获取当前位置城市代码
            String fromcitycode = document.getElementsByTag("fromcitycode").text();
            //2.获取目标位置城市代码
            String tocitycode = document.getElementsByTag("tocitycode").text();
            //3.获取出行日期
            String[] dates = SplitDate(document.getElementsByTag("date").text());
            //4.获取当前位置的中文名
            String fromcityname = document.getElementsByTag("fromadd").text();
            //5.获取目标位置的中文名
            String tocityname = document.getElementsByTag("toadd").text();
            //6.获取Server酱的key
            String key = document.getElementsByTag("key").text();

            return new Ticket(fromcitycode,tocitycode,fromcityname,tocityname,key,dates);
        } catch (IOException e) {
            logger.error("读取配置文件信息错误"+e.getMessage(),e);
        }
        return null;
    }

}
