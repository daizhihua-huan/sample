package com.daizhihua.sample.dao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface TestMapper {


    @Select("SELECT * FROM test")
    List< Map<String,Object>> findByState();

}
