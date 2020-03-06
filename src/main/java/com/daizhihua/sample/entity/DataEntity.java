package com.daizhihua.sample.entity;


//import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "dataentity")
public class DataEntity {


    private String id ;

    private String code;

    private String longitude;

    private String latitude;

    private String cu;

    private String pb;

    private String zn;

    private String co;

    private String cr;

    private String cd;

    private String v;

    private String mn;

    private String mo;

    private String p;

    private String asp;

    private String hg;

    private String se;

    private String f;

    private String i;

    private String b;

    private String cl;

    private String c;

    private String s;

    private String n;

    private String cec;

    private String ph;

    private String siO2;

    private String caO;

    private String fe2O3;

    private String k2O;

    private String mgO;

    private String sb;

    private String district;

    private String street;

    private String community;

    private String weather;

    private String number;

    private String type;

    private String localtion;

    private String depth;

    private String sampleCode;

    private String gpsCode;

    private String color;

    private String characters;

    private String contaminate;

    private String solitype;

    private String landform;

    private String utilization;

    private String crop;

    private String elevation;

    private Date years;

    private String name;

    private String xx;

    private String yy;

    private String environment;

    private String ifGather;

    private String diagram;

    private String notepaper;

    private String samplingPeople;

    private String auditor;

    private Date date;

    private String location;

}
