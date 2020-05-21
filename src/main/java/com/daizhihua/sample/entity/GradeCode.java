package com.daizhihua.sample.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@TableName(value = "gradecode")
public class GradeCode  implements Comparable<GradeCode>{

    private String id;

    private String name;

    private String url;

    @TableField(value = "index_code")
    private String indexCode;

    @Override
    public int compareTo(GradeCode o) {
        int i =  Integer.parseInt(this.indexCode)-Integer.parseInt(o.getIndexCode());
        return i;
    }



}
