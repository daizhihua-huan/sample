package com.daizhihua.sample.dao;


//import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daizhihua.sample.entity.DataEntity;
import com.daizhihua.sample.entity.RegionData;

//@Mapper
//@Repository("dataMapper")

public interface RegionDataMapper extends BaseMapper<RegionData> {






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
