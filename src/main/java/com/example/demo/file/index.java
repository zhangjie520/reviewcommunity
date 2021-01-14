package com.example.demo.file;


import javafx.scene.input.DataFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author: codeape
 * @Date: 2019/11/2 14:22
 * @Version: 1.0
 */
public class index {
    private int intExample = 1;
    private short shortExample = 1;
    private char charExample = 'a';
    private long longExample = 1L;
    private double doubleExample = 1;
    private String stringExample = "this is a example";
    private boolean booleanExample = true;

    private long longEx = intExample;


    //    DateFormat.parse(String s)
    public static void main(String[] args) {
        Date nowDate = new Date();
        // dataformat
        DataFormat dataFormat = new DataFormat();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-mm");
        System.out.println(nowDate.getYear()+"年"+nowDate.getMonth()+"月"+nowDate.getDay()+"日");
        // aabb
        for (int i = 0000; i < 10000; i++) {
            int qianWei = i / 1000;
            int baiWei = i / 100 % 10;
            int shiWei = i / 10 % 10;
            int geWei = i % 10;
            if (qianWei == baiWei && shiWei == geWei && qianWei != shiWei) {
                System.out.println(i);
            }
        }
    }

}
