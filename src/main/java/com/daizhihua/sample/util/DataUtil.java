package com.daizhihua.sample.util;

import com.daizhihua.sample.entity.DataEntity;

import java.util.ArrayList;
import java.util.List;

public class DataUtil {

    public static List<DataEntity> getDataList(List<DataEntity> listData){
        for (DataEntity dataEntity : listData) {
            String longitude = dataEntity.getXx();
            String latitude = dataEntity.getYy();
            List<String> list = new ArrayList<>();
            list.add(longitude);
            list.add(latitude);
            dataEntity.setLnglat(list);
//                dataEntity.setStyle(0);
        }
        return listData;
    }
}
