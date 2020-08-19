package main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * @author: Thorn
 * @Date: 2020/8/19 00:04
 * @Description:
 */
public class Main extends AirTicket{
    private static Logger logger = LogManager.getLogger(Main.class);
    public static void main(String[] args) {
        try {
            //获取xml的path
            //解析xml
            Document document = Jsoup.parse(new File("configuration.xml"),"utf-8");
            //获取机票信息
            //1.获取当前位置城市代码
            String fromcitycode = document.getElementsByTag("fromcitycode").text();
            //2.获取目标位置城市代码
            String tocitycode = document.getElementsByTag("tocitycode").text();
            //3.获取出行日期
            String date = document.getElementsByTag("date").text();
            //4.获取当前位置的中文名
            String fromcityname = document.getElementsByTag("fromadd").text();
            //5.获取目标位置的中文名
            String tocityname = document.getElementsByTag("toadd").text();
            //6.获取Server酱的key
            String key = document.getElementsByTag("key").text();

            //访问并解析网页
            ArrayList<String> value = ParHtml(GetHtml(key, fromcitycode, tocitycode, date, fromcityname, tocityname));
            String Message = "| 日期 | 价格 |\n" +
                    "| :------ | ------: |\n" +
                    "| "+value.get(0)+"&nbsp; &nbsp;"+" | "+value.get(1)+" |  ";
            SendMessage(key,"今日"+fromcityname+"-"+tocityname+"机票价格",Message);
            logger.info("program run successful!");
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
        }

    }
}
