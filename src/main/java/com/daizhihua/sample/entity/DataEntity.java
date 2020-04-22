package com.daizhihua.sample.entity;


//import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@TableName(value = "dataentity")
public class DataEntity  {


    private String id ;

    private String samplecode;

    private String longitude;

    private String latitude;

    private String cu;

    private String pb;

    private String zn;

    private String ni;

    private String cr;

    private String cd;

    private String asa;

    private String hg;

    private String environmental;

    private String mn;

    private String mo;

    private String p;

    private String se;

    private String f;

    private String i;

    private String b;

    private String cl;

    private String s;

    private String n;

    private String ca_o;

    private String fe2_o3;

    private String k2_o;

    private String mg_o;

    private String c0;

    private String v;

    private String ge;

    //养分综合
    private String nutrient;
    //综合分级
    private String comprehensive;

    private String ph;

    //区
    private String district;
    //县
    private String street;

    private String community;

    private String weather;

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

    private String years;

    private String xx;

    private String yy;

    private String environment;

    private String if_gather;

    private Date date;

    private String picter;


    @TableField(exist = false)
    private List<String> lnglat;
    @TableField(exist = false)
    private int style;



}
