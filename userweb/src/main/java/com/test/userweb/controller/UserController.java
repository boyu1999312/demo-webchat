package com.test.userweb.controller;


import com.test.common.entity.UserBean;
import com.test.common.util.Result;
import com.test.userweb.service.UserRemote;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserController {

    @Autowired
    private UserRemote userRemote;

    private Log log = LogFactory.getLog(UserController.class);

    @GetMapping("/findAll")
    public List<UserBean> findAll(){

        return userRemote.findAll();
    }

    @PostMapping("/login")
    public Result login(String account, String password, HttpServletResponse response){

        log.info("info ---> " + account + " : " + password);

        if(StringUtils.isEmpty(account) || StringUtils.isEmpty(password)){
            return Result.error("用户名或密码为空!");
        }
        return userRemote.login(account, password, response);
    }

}
