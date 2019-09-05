package com.test.common;

import com.test.common.util.JsonMapper;
import com.test.common.util.Result;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// @RunWith(SpringRunner.class)
// @SpringBootTest
public class CommonApplicationTests {



    @Test
    public void contextLoads() {

        Map<String, Object> map = new HashMap<>();
        map.put("uid", 1);
        map.put("time", new Date().getTime());
        map.put("sid", 1);

        String json = JsonMapper.toJson(map);
        System.out.println(json);
        Map<String, Object> map1 = JsonMapper.toObject(json, Map.class);
        System.out.println(map1);

        String str = "{\"uid\":1,\"time\":1567057886984,\"sid\":1}";
        HashMap hashMap = JsonMapper.toObject(str, HashMap.class);
        System.out.println(hashMap);
    }

}
