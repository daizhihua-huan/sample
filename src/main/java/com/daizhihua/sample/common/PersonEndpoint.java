package com.daizhihua.sample.common;

import com.daizhihua.sample.core.Resut;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.AbstractEndpoint;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Endpoint(id="dataTest")
@Slf4j
public class PersonEndpoint {

    @ReadOperation
    public Resut checkStaus(){
        return Resut.ok("测试");
    }

}
