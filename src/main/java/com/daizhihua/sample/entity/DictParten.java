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

    private String dataIndex;

    private String dataCode;

    private String title;

    private String sortNo;

    private String status;

    private String dataDesc;

    private String updateTime;

    @TableField(exist = false)
    private boolean spread;

    @TableField(exist = false)
    private List<Dict> children;
}
