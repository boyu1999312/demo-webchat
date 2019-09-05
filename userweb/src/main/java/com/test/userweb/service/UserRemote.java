package com.test.userweb.service;

import com.test.common.entity.UserBean;
import com.test.common.util.Result;
import com.test.userweb.hystrix.UserRemoteHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
@FeignClient(value = "user-producer", fallback = UserRemoteHystrix.class)
public interface UserRemote {

    @GetMapping("/user/findAll")
    List<UserBean> findAll();

    @PostMapping("/connect/login")
    public Result login(@RequestParam("account") String account
            ,@RequestParam("password") String password
            , HttpServletResponse response);

}
