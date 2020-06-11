package com.daizhihua.sample.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daizhihua.sample.entity.Dict;

public interface DicMapper extends BaseMapper<Dict> {


    int drope();
}
