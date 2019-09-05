package com.test.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class JsonMapper {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String toJson(Object data){
        if(StringUtils.isEmpty(data)){
            return null;
        }
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T>T toObject(String json, Class<T> tClass){
        if(StringUtils.isEmpty(json) || StringUtils.isEmpty(tClass)){
            return null;
        }
        try {
            return mapper.readValue(json, tClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
