package main;

/**
 * @author: Thorn
 * @Date: 2020/8/19 15:22
 * @Description:
 */
public class Ticket {
    private String fromcitycode,tocitycode,fromcityname,tocityname,key;
    private String[] dates;

    public Ticket(String fromcitycode, String tocitycode, String fromcityname, String tocityname, String key, String[] dates) {
        this.fromcitycode = fromcitycode;
        this.tocitycode = tocitycode;
        this.fromcityname = fromcityname;
        this.tocityname = tocityname;
        this.key = key;
        this.dates = dates;
    }

    public String getFromcitycode() {
        return fromcitycode;
    }

    public void setFromcitycode(String fromcitycode) {
        this.fromcitycode = fromcitycode;
    }

    public String getTocitycode() {
        return tocitycode;
    }

    public void setTocitycode(String tocitycode) {
        this.tocitycode = tocitycode;
    }

    public String getFromcityname() {
        return fromcityname;
    }

    public void setFromcityname(String fromcityname) {
        this.fromcityname = fromcityname;
    }

    public String getTocityname() {
        return tocityname;
    }

    public void setTocityname(String tocityname) {
        this.tocityname = tocityname;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String[] getDates() {
        return dates;
    }

    public void setDates(String[] dates) {
        this.dates = dates;
    }
}
