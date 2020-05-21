package com.daizhihua.sample.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@TableName("dict")
@Data
//@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class Dict {

    private String id;

    private String pid;
    @TableField(value = "data_index")
    private String dataIndex;

    @TableField(value = "data_code")
    private String dataCode;


    private String title;
    @TableField(value = "sort_no")
    private Integer sortNo;

    private String status;

    private String data_desc;
    @TableField(value = "update_time")
    private String updateTime;
    @TableField(value = "check_arr")
    private String checkArr;



}
