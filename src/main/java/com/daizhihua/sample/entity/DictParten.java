package com.daizhihua.sample.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@Slf4j
@TableName("dict")
public class DictParten {

    private String id;

    private String pid;
    @TableField(value = "data_index")
    private String dataIndex;
    @TableField(value = "data_code")
    private String dataCode;

    private String title;
    @TableField(value = "sort_no")
    private String sortNo;

    private String status;
    @TableField(value = "data_desc")
    private String dataDesc;
    @TableField(value = "update_time")
    private String updateTime;

    @TableField(exist = false)
    private boolean spread;

    @TableField(exist = false)
    private List<Dict> children;
}
