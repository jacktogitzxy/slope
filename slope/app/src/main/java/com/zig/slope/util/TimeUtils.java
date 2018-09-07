package com.zig.slope.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by 17120 on 2018/9/3.
 */

public  class TimeUtils {
    public static String transleteTime(String time){
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy K:m:s a", Locale.ENGLISH);
        Date d = null;
        try {
            d = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat sdf2 = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");
        return  sdf2.format(d);
    }

}
