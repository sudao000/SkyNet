package com.hxy.skynet.until;


import com.hxy.skynet.bean.Statebean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Sudao on 2016/11/7.
 * 全局变量类
 */
public class Globals {
    public static String machineName="sclyj01001";
    public static String machineAdata;
    public static String machineDdata;
    public static List<Statebean> list_sb=new ArrayList<>();
    public static String nowState;
    public static int dataid=0;
    public static String[] getTime() {
        String[] a = new String[2];
        Calendar c = Calendar.getInstance();
        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH) + 1);
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String mins = String.valueOf(c.get(Calendar.MINUTE));
        String sec = String.valueOf(c.get(Calendar.SECOND));
        a[0] = hour + ":" + mins + ":" + sec;
        a[1] = year + "-" + month + "-" + day;
        return a;
    }
    public static String getDate() {
        String a;
        Calendar c = Calendar.getInstance();
        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH) + 1);
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        a = year + "-" + month + "-" + day;
        return a;
    }
}
