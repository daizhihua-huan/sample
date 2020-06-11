package com.daizhihua.sample.dao;


//import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daizhihua.sample.entity.DataEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

//@Mapper
//@Repository("dataMapper")

public interface DataMapper  extends BaseMapper<DataEntity> {

    @Select("select ${name} as name from dataentity where type='${type}' and years ='${year}' group by ${name}")
    List<String> getElement(@Param("name") String name,@Param("type")String type,@Param("year")String year);


    @Select("select * from soucedata")
    List<DataEntity> getAll();

    @Select("select district as name ,count(id) as y from dataentity where type ='${type}'and  years ='${year}' group by district")
    List<Map<String,Object>> getCount(@Param("type")String type,@Param("year")String year);


    @Select("select count(id) from dataentity where type='${type}' and years='${year}'")
    int getTypeCount(String type,String year);

    //养分综合
    @Select("select nutrient as name,count(id) as y from dataentity where type ='${type}'and  years ='${year}' group by nutrient")
    List<Map<String,Object>> getNutrientCount(@Param("type")String type,@Param("year")String year);

    @Select("select nutrient as name,count(id) as y from dataentity where type ='${type}'and district='${district}' and years ='${year}' group by nutrient")
    List<Map<String,Object>> getNutrientCountCity(@Param("type")String type,@Param("year")String year,@Param("district")String district);

    //环境指标
    @Select("select environmental as name,count(id) as y from dataentity where type='${type}' and years='${year}' group by environmental ")
    List<Map<String,Object>>getEnvironmentContainer(@Param("type")String type,@Param("year")String year);

    @Select("select comprehensive as name,count(id) as y from dataentity where type='${type}' and years='${year}' group by comprehensive")
    List<Map<String,Object>>getcomprehensive(@Param("type")String type,@Param("year")String year);


    @Select("select environmental as name,count(id) as y from dataentity where type='${type}' and years='${year}' and district='${district}' group by environmental ")
    List<Map<String,Object>>getEnvironmentContainerCity(@Param("type")String type,@Param("year")String year,@Param("district")String district);


    //土地利用类型
    @Select("select utilization as name,count(id) as y from dataentity where type='${type}' and years='${year}' group by utilization")
    List<Map<String,Object>>getLandTypeContainer(@Param("type")String type,@Param("year")String year);


    @Select("select utilization as name,count(id) as y from dataentity where type='${type}' and years='${year}' and district='${district}' group by utilization")
    List<Map<String,Object>>getLandTypeContainerCity(@Param("type")String type,@Param("year")String year,@Param("district")String district);

    @Select("select ${name} as name,count(id) as data from dataentity where type='${type}' and years='${year}' group by ${name} order by name desc")
    List<Map<String,Object>>getNameCount(@Param("name")String name,@Param("type")String type,@Param("year")String year);

    @Select("select count(id) as total,years from dataentity where type = '${type}' group by years order by total desc")
    List<Map<String,Object>>getTotalTypeForYears(@Param("type")String type);

    /*@Insert({
            "<script>",
            "insert into tb_area(id, name, pid,level) values ",
            "<foreach collection='areaLists' item='item' index='index' separator=','>",
            "(#{item.id}, #{item.name}, #{item.pid}, #{item.level} )",
            "</foreach>",cu
            "</script>"
    })
    int insertDataEntity(@Param(value = "dataEntityList") List<DataEntity> dataEntityList);*/


}
