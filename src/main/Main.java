package main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * @author: Thorn
 * @Date: 2020/8/19 00:04
 * @Description:
 */
public class Main extends AirTicket{
    private static Logger logger = LogManager.getLogger(Main.class);
    public static void main(String[] args) {
        ArrayList<Map> list = new ArrayList<>();
        //解析配置文件
        Ticket ticket = ParseConfiguration("configuration.xml");
        for (int i = 0; i < ticket.getDates().length; i++) {
            //访问并解析网页
           list.add(ParHtml(GetHtml(ticket.getKey(), ticket.getFromcitycode(), ticket.getTocitycode(), ticket.getDates()[i], ticket.getFromcityname(), ticket.getTocityname())));
        }
        String Message = "| 日期 | 价格 |\n" +
                "| :------ | ------: |\n";

        for (Map value:list
             ) {
            Iterator it = value.entrySet().iterator();
            while (it.hasNext())
            {
                Map.Entry entry = (Map.Entry) it.next();
                Message = Message + "| "+entry.getKey()+"&nbsp; &nbsp;"+" | "+entry.getValue()+" |  \n";
            }
        }

        SendMessage(ticket.getKey(),"今日"+ticket.getFromcityname()+"-"+ ticket.getTocityname()+"机票价格",Message);
        logger.info("program run successful!");
    }
}
