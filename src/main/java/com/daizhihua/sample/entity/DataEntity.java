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

    @TableId(value = "samplecode")
    private String samplecode;

    @TableField(value = "xx")
    private String xx;

    private String yy;

//    private String latitude;
    //4
    private String asa;
    //5
    private String cd;
    //6
    @TableField(value = "cu_hj")
    private String cu_hj;
    //7
    private String cr;
    //8
    private String hg;
    //9
    private String ni;
    //10
    private String pb;
    //11
    @TableField(value = "zn_hj")
    private String zn_hj;
    //12
    private String ph;

    //环境综合分级 13
    private String environmental;

    //14
    private String n;
    //15
    private String p;
    //16
    private String k;
    //17
    @TableField(value = "ca_o")
    private String ca_o;
    //18
    @TableField(value = "mg_o")
    private String mg_o;
    //19
    private String s;

    //有机质 20
    private String organic;
    //21
    @TableField(value = "fe2_o3")
    private String fe2_o3;
    //22
    private String c0;
    //23
    private String v;
    //24
    private String ge;
    //25
    private String b;
    //26
    private String mo;

    //27

    private String mn;

    //28
    @TableField(value = "cu_yf")
    private String cu_yf;
    //29
    @TableField(value = "zn_yf")
    private String zn_yf;
    //30
    private String cl;
    //31
    @TableField(value = "si_o2 ")
    private String si_o2;
    //32
    private String se;
    //33
    private String i;
    //34
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
