package com.daizhihua.sample.entity;


//import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@TableName(value = "dataentity")
public class DataEntity  {


    private String id ;

    @TableField(value = "samplecode")
    private String samplecode;

    @TableField(value = "xx")
    private String xx;

    private String yy;

//    private String latitude;

    private String asa;

    private String cd;

    @TableField(value = "cu_hj",exist = true)
    private String cuHj;

    private String cr;

    private String hg;

    private String ni;

    private String pb;
    @TableField(value = "zn_hj")
    private String znHj;

    private String ph;

    //环境综合分级
    private String environmental;


    private String n;

    private String p;

    private String k;
    @TableField(value = "ca_o")
    private String cao;
    @TableField(value = "mg_o")
    private String mgo;

    private String s;

    //有机质
    private String organic;
    @TableField(value = "fe2_o3")
    private String fe2o3;

    private String c0;

    private String v;

    private String ge;

    private String b;

    private String mo;

    private String mn;


    @TableField(value = "cu_yf")
    private String cuYf;
    @TableField(value = "zn_yf")
    private String znYf;

    private String cl;
    @TableField(value = "si_o2 ")
    private String sio2;

    private String se;

    private String i;

    private String f;

    //养分综合
    private String nutrient;
    //综合分级
    private String comprehensive;


    //区
    private String district;
    //县
    private String street;
    //
    private String community;
    //天气
    private String weather;

    //监测点类型
    private String type;



    //位置
    private String site;

    //深度
    private String depth;

    private String color;

    //质地
    private String characters;
    //污染
    private String contaminate;
    //土壤类型
    private String solitype;
    //地形地貌
    private String landform;
    //土地利用
    private String utilization;
    //农作物与植被
    private String crop;
    //高程
    private String elevation;
    //监测年度
    private String years;

    private String latitude;

    private String longitude;

    private String environment;

    private String if_gather;

    private String date;

    private String picter;


//    private String k2_o;




















    @TableField(exist = false)
    private List<String> lnglat;
    @TableField(exist = false)
    private int style;



}
