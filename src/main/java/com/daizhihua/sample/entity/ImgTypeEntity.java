package com.daizhihua.sample.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "imagetype")
public class ImgTypeEntity {

    @TableId(type = IdType.AUTO)
    private int id;

    @TableField(value = "name")
    private String name;

    @TableField(value = "path")
    private String path;

    @TableField(value = "year")
    private String year;

    @TableField(value = "createTime")
    private String createTime;

    @TableField(value = "status")
    private String status;

    @TableField(value = "type")
    private String type;

    @TableField(value = "pid")
    private String pid;

    @TableField(value = "imgType")
    private String imgType;
}
