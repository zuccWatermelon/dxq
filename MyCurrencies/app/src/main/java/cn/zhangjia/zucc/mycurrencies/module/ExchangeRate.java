package cn.zhangjia.zucc.mycurrencies.module;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Mia on 2017/7/9.
 */

public class ExchangeRate {
    private Date time;
    private int id;
    private String foreignName;
    private String homeName;
    private double rate;

    public Date getTime(){
        return time;
    }

    public void setTime(Date time){
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getForeignName() {
        return foreignName;
    }

    public void setForeignName(String foreignName) {
        this.foreignName = foreignName;
    }

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
