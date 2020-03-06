package com.daizhihua.sample.controller;


import com.daizhihua.sample.core.Resut;
import com.daizhihua.sample.dao.DataMapper;
import com.daizhihua.sample.dao.TestMapper;
import com.daizhihua.sample.entity.DataEntity;
import com.daizhihua.sample.util.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@Slf4j
public class FileController {

    @Autowired
    TestMapper testMapper;

    @Autowired
    private DataMapper dataMapper;

    @RequestMapping(value = "/sendFile",method = RequestMethod.POST)
    @ResponseBody
//    @Transactional(readOnly = false)
//    @org.springframework.transaction.annotation.Transactional(propagation = Propagation.REQUIRED)
    public Resut sendFile(@RequestParam(name = "file") MultipartFile multipartFile){
        log.info(multipartFile.getName());
        log.info(multipartFile.getOriginalFilename());
        log.info(multipartFile.getContentType());
        String name = multipartFile.getOriginalFilename();
        if(name.indexOf(".")==-1){
            return Resut.error("上传文件类型错误");
        }

        try {
            List<DataEntity> dataEntities = ExcelUtils.readExcelToEntity(DataEntity.class, multipartFile.getInputStream(), name);
            System.out.println(dataEntities);
            for (DataEntity dataEntity : dataEntities) {
                if(dataEntity.getId()==null){
                    continue;
                }
                String longitude = dataEntity.getLongitude();
                String latitude = dataEntity.getLatitude();
                dataEntity.setLocation(longitude+","+latitude);
                //dataEntity.setDataId(0L);
                System.out.println(dataEntity);
                dataMapper.insert(dataEntity);
//                dataMapper.insert(dataEntity);
            }
//            dataMapper.saveAll(dataEntities);
            log.info("数据是"+dataEntities);
//            dataMapper.saveAll(dataEntities);
            //ExcleUtile.analisy(name.substring(name.indexOf(".")+1),multipartFile.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return Resut.error("文件模板错误");
        } catch (Exception e) {
            e.printStackTrace();
            return Resut.error("文件模板错误");
        }
//        multipartFile.getInputStream()

        return Resut.ok();

    }

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    @ResponseBody
    public Resut test(){
//        DataEntity dataEntity = new DataEntity();
//        dataEntity.setId("123");
//        dataMapper.save(dataEntity);
        //testMapper.findByState()
        return Resut.ok(testMapper.findByState());
    }


}
