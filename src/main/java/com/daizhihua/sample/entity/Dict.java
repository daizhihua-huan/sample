package com.daizhihua.sample.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

@TableName("dict")
@Data
public class Dict {

    private String id;

    private String pid;

    private String dataIndex;


    private String dataCode;


    private String title;

    private String sortNo;

    private String status;

    private String dataDesc;

    private String updateTime;

    private String checkArr;


}
