package cn.zhangjia.zucc.mycurrencies.module;

/**
 * Created by Mia on 2017/7/9.
 */

public class ExchangeRecord {
    private int id;
    private String foreignName;
    private double foreignCurrency;
    private String homeName;
    private double homeCurrency;

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

    public double getForeignCurrency() {
        return foreignCurrency;
    }

    public void setForeignCurrency(double foreignCurrency) {
        this.foreignCurrency = foreignCurrency;
    }

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public double getHomeCurrency() {
        return homeCurrency;
    }

    public void setHomeCurrency(double homeCurrency) {
        this.homeCurrency = homeCurrency;
    }
}
