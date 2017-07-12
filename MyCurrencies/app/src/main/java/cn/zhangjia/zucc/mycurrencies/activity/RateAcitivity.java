package cn.zhangjia.zucc.mycurrencies.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.zhangjia.zucc.mycurrencies.R;
import cn.zhangjia.zucc.mycurrencies.database.DBManager;
import cn.zhangjia.zucc.mycurrencies.module.ExchangeRate;
import cn.zhangjia.zucc.mycurrencies.module.ExchangeRecord;
import cn.zhangjia.zucc.mycurrencies.util.DateUtil;

/**
 * Created by Mia on 2017/7/11.
 */

public class RateAcitivity extends Activity {
    private DBManager database;

    private List<ExchangeRate> exchangeRates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_rate);
        database = new DBManager(this);
        final TextView textView = findViewById(R.id.rateText);
        try {
            exchangeRates = database.queryAllExchangeRate();
            String rateString = "";
            for (ExchangeRate exchangeRate : exchangeRates) {
                rateString +=  "                  " + exchangeRate.getRate() + "                                      "  + DateUtil.dateToString(exchangeRate.getTime(),"yy-MM-dd-hh") + "\n"+ "\n";
            }
            textView.setText(rateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
