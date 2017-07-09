package cn.zhangjia.zucc.mycurrencies.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.zhangjia.zucc.mycurrencies.module.ExchangeRate;
import cn.zhangjia.zucc.mycurrencies.module.ExchangeRecord;

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
            db.execSQL("INSERT INTO ExchangeRate VALUES(null, ?, ?, ?)", new Object[]{exchangeRate.getForeignName(), exchangeRate.getHomeName(), exchangeRate.getRate()});
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    public void addExchangeRecord(ExchangeRecord exchangeRecord) {
        db.beginTransaction();  //开始事务
        try {
            db.execSQL("INSERT INTO ExchangeRecord VALUES(null, ?, ?, ?, ?)", new Object[]{exchangeRecord.getForeignName(), exchangeRecord.getForeignCurrency(), exchangeRecord.getHomeName(), exchangeRecord.getHomeCurrency()});
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    public List<ExchangeRecord> queryAllExchangeRecord() {
        Cursor cursor = db.rawQuery("SELECT * FROM ExchangeRecord", null);
        List<ExchangeRecord> exchangeRecords = new ArrayList<>();
        while (cursor.moveToNext()) {
            ExchangeRecord exchangeRecord = new ExchangeRecord();
            exchangeRecord.setId(cursor.getInt(0));
            exchangeRecord.setForeignName(cursor.getString(1));
            exchangeRecord.setForeignCurrency(cursor.getDouble(2));
            exchangeRecord.setHomeName(cursor.getString(3));
            exchangeRecord.setHomeCurrency(cursor.getDouble(4));
            exchangeRecords.add(exchangeRecord);
        }
        cursor.close();
        return exchangeRecords;
    }

    /**
     * close database
     */
    public void closeDB() {
        db.close();
    }
}