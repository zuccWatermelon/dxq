package cn.zhangjia.zucc.mycurrencies.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import cn.zhangjia.zucc.mycurrencies.R;
import cn.zhangjia.zucc.mycurrencies.database.DBManager;
import cn.zhangjia.zucc.mycurrencies.model.ExchangeRate;
import cn.zhangjia.zucc.mycurrencies.util.DateUtil;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by Mia on 2017/7/11.
 */

public class RateAcitivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        DBManager database = new DBManager(this);
        try {
            LineChartView rateDayView = findViewById(R.id.chart);
            List<Line> rateLineList = new ArrayList<>();
            List<PointValue> rateValueList = new ArrayList<>();
            List<AxisValue> rateAxisValueList = new ArrayList<>();
            Line rateLine = new Line(rateValueList).setCubic(true).setHasLabels(true).setHasLines(true).setHasPoints(false);


            List<ExchangeRate> exchangeRates = database.queryAllExchangeRate();
            for (int i = 0; i < exchangeRates.size(); i++) {
                rateValueList.add(new PointValue(i, (float) exchangeRates.get(i).getRate()));
                rateAxisValueList.add(new AxisValue(i).setLabel(DateUtil.dateToString(exchangeRates.get(i).getTime(),"yyyy-MM-dd")));
            }

            rateLineList.add(rateLine);

            LineChartData rateData = new LineChartData();

            Axis axisX = new Axis();
            axisX.setHasLines(true);
            axisX.setHasTiltedLabels(true);
            axisX.setTextColor(Color.BLACK);
            axisX.setName("Time");
            axisX.setMaxLabelChars(6);
            axisX.setValues(rateAxisValueList);
            rateData.setAxisXBottom(axisX);

            Axis axisY = new Axis();
            axisY.setHasLines(true);
            axisY.setHasTiltedLabels(true);
            axisY.setTextColor(Color.BLACK);
            axisY.setName("Rate");
            axisY.setMaxLabelChars(6);
            rateData.setAxisYLeft(axisY);

            rateData.setLines(rateLineList);

            rateDayView.setLineChartData(rateData);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}
