package cn.zhangjia.zucc.mycurrencies.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import cn.zhangjia.zucc.mycurrencies.R;
import cn.zhangjia.zucc.mycurrencies.database.DBManager;
import cn.zhangjia.zucc.mycurrencies.module.ExchangeRecord;
import cn.zhangjia.zucc.mycurrencies.util.DateUtil;

/**
 * Created by Mia on 2017/7/10.
 */

public class RecordActivity extends Activity {
    private DBManager database;

    private List<ExchangeRecord> exchangeRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_record);
        database = new DBManager(this);

        final TextView textView = findViewById(R.id.historyText);
        try {
            exchangeRecords = database.queryAllExchangeRecord();
            String recordString = "";
            for (ExchangeRecord exchangeRecord : exchangeRecords) {
                recordString += "      " + exchangeRecord.getForeignName() + "           " + exchangeRecord.getForeignCurrency() + "            " + exchangeRecord.getHomeName() + "        " + exchangeRecord.getHomeCurrency() + "       "+ DateUtil.dateToString(exchangeRecord.getTime(),"yy/MM/dd/hh")+ "\n"+"\n";
            }
            textView.setText(recordString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final TextView selectedDate = findViewById(R.id.selectedDate);
        selectedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker arg0, int year, int month, int day) {
                        month++;
                        String dateString = year + "-" + (month < 10 ? "0" : "") + month + "-" + (day < 10 ? "0" : "") + day;
                        selectedDate.setText(dateString);      //将选择的日期显示到TextView中,因为之前获取month直接使用，所以不需要+1，这个地方需要显示，所以+1
                        try {
                            Date date = DateUtil.stringToDate(dateString, "yyyy-MM-dd");
                            String recordString = "";
                            for (ExchangeRecord exchangeRecord : exchangeRecords) {
                                if (exchangeRecord.getTime().compareTo(date) <= 0)
                                    recordString += exchangeRecord.getForeignName() + "\t" + exchangeRecord.getForeignCurrency() + "\t" + exchangeRecord.getHomeName() + "\t" + exchangeRecord.getHomeCurrency() + "\n";
                            }
                            textView.setText(recordString);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                };
                DatePickerDialog dialog = new DatePickerDialog(RecordActivity.this, 0, listener, 2017, 6, 10);//后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
                dialog.show();
            }
        });

    }

}