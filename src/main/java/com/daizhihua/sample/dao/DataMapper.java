package com.daizhihua.sample.dao;


//import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daizhihua.sample.entity.DataEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

//@Mapper
//@Repository("dataMapper")

public interface DataMapper  extends BaseMapper<DataEntity> {


    @Select("select * from soucedata")
    List<DataEntity> getAll();



    /*@Insert({
            "<script>",
            "insert into tb_area(id, name, pid,level) values ",
            "<foreach collection='areaLists' item='item' index='index' separator=','>",
            "(#{item.id}, #{item.name}, #{item.pid}, #{item.level} )",
            "</foreach>",
            "</script>"
    })
    int insertDataEntity(@Param(value = "dataEntityList") List<DataEntity> dataEntityList);*/


}
