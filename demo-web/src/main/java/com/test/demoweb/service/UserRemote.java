package com.test.demoweb.service;

import com.test.common.util.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


@Component
@FeignClient("user-producer")
public interface UserRemote {

    @PostMapping("/connect/login")
    public Result login(@RequestParam Map<String, String> map);
}
