package cn.zhangjia.zucc.mycurrencies.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(formatType);
        return sdf.parse(strTime);
    }

    public static String dateToString(Date date, String formatType) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatType);
        return sdf.format(date);
    }
}
