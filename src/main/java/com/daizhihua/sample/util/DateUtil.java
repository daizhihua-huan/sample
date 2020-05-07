package com.daizhihua.sample.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String getNewDate(){
        SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");
        Date date = new Date();
        return smf.format(date);
    }
}
