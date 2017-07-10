package cn.zhangjia.zucc.mycurrencies.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import java.text.ParseException;
import java.util.List;

import cn.zhangjia.zucc.mycurrencies.R;
import cn.zhangjia.zucc.mycurrencies.database.DBManager;
import cn.zhangjia.zucc.mycurrencies.module.ExchangeRecord;

/**
 * Created by Mia on 2017/7/10.
 */

public class RecordActivity extends Activity{
    private DBManager database;
    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_record);
        database = new DBManager(this);
            String recordString = "";
        List<ExchangeRecord> exchangeRecords = null;
        try {
            exchangeRecords = database.queryAllExchangeRecord();
            for(ExchangeRecord exchangeRecord : exchangeRecords){
                recordString += exchangeRecord.getForeignName() + " " + exchangeRecord.getForeignCurrency() + " to " + exchangeRecord.getHomeName() + " " + exchangeRecord.getHomeCurrency() + "\n";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}