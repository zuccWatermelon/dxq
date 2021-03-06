package cn.zhangjia.zucc.mycurrencies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mia on 2017/7/9.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ExchangeRate.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        //CursorFactory设置为null,使用默认值
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //数据库第一次被创建时onCreate会被调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS ExchangeRate" +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT , foreignName VARCHAR(10), homeName VARCHAR(10), rate REAL , time VARCHAR(20))");

        db.execSQL("CREATE TABLE IF NOT EXISTS ExchangeRecord" +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, foreignName VARCHAR(10), foreignCurrency REAL, homeName VARCHAR(10), homeCurrency REAL,time VARCHAR(20))");
    }

    //如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists ExchangeRate");
        db.execSQL("drop table if exists ExchangeRecord");
        onCreate(db);
    }
}