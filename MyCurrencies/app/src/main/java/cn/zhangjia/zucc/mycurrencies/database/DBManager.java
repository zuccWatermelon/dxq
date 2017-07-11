package cn.zhangjia.zucc.mycurrencies.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.zhangjia.zucc.mycurrencies.module.ExchangeRate;
import cn.zhangjia.zucc.mycurrencies.module.ExchangeRecord;
import cn.zhangjia.zucc.mycurrencies.util.DateUtil;

/**
 * Created by Mia on 2017/7/9.
 */

public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        helper = new DBHelper(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    public void addExchangeRate(ExchangeRate exchangeRate) {
        db.beginTransaction();  //开始事务
        try {
            db.execSQL("INSERT INTO ExchangeRate VALUES(null, ?, ?, ? , ?)", new Object[]{exchangeRate.getForeignName(), exchangeRate.getHomeName(), exchangeRate.getRate(), exchangeRate.getTime()});
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    public void addExchangeRecord(ExchangeRecord exchangeRecord) {
        db.beginTransaction();  //开始事务
        try {
            db.execSQL("INSERT INTO ExchangeRecord VALUES(null, ?, ?, ?, ?, ?)", new Object[]{exchangeRecord.getForeignName(), exchangeRecord.getForeignCurrency(), exchangeRecord.getHomeName(), exchangeRecord.getHomeCurrency(), DateUtil.dateToString(exchangeRecord.getTime(), "yyyy-MM-dd")});
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    public List<ExchangeRecord> queryAllExchangeRecord() throws ParseException {
        Cursor cursor = db.rawQuery("SELECT * FROM ExchangeRecord", null);
        List<ExchangeRecord> exchangeRecords = new ArrayList<>();
        while (cursor.moveToNext()) {
            ExchangeRecord exchangeRecord = new ExchangeRecord();
            exchangeRecord.setId(cursor.getInt(0));
            exchangeRecord.setForeignName(cursor.getString(1));
            exchangeRecord.setForeignCurrency(cursor.getDouble(2));
            exchangeRecord.setHomeName(cursor.getString(3));
            exchangeRecord.setHomeCurrency(cursor.getDouble(4));
            exchangeRecord.setTime(DateUtil.stringToDate(cursor.getString(5), "yyyy-MM-dd"));
            exchangeRecords.add(exchangeRecord);
        }
        cursor.close();
        return exchangeRecords;
    }

    public List<ExchangeRate> queryAllExchangeRate() throws ParseException {
        Cursor cursor = db.rawQuery("SELECT * FROM ExchangeRate", null);
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        while (cursor.moveToNext()) {
            ExchangeRate exchangeRate = new ExchangeRate();
            exchangeRate.setId(cursor.getInt(0));
            exchangeRate.setForeignName(cursor.getString(1));
            exchangeRate.setHomeName(cursor.getString(2));
            exchangeRate.setRate(cursor.getDouble(3));
            exchangeRate.setTime(DateUtil.stringToDate(cursor.getString(4), "yyyy-MM-dd"));
            exchangeRates.add(exchangeRate);
        }
        cursor.close();
        return exchangeRates;
    }

    /**
     * close database
     */
    public void closeDB() {
        db.close();
    }
}