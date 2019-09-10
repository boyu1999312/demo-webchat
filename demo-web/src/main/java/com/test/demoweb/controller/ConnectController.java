package com.test.demoweb.controller;


import com.test.common.constant.LoginFinal;
import com.test.common.util.CookieUtil;
import com.test.common.util.Result;
import com.test.demoweb.service.UserRemote;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/connect")
public class ConnectController {

    @Autowired
    private UserRemote userRemote;
    @Autowired
    private StringRedisTemplate template;

    private Log log = LogFactory.getLog(ConnectController.class);

    @PostMapping("/login")
    public Result login(@RequestParam Map<String, String> map, HttpServletResponse response) {
        String account = map.get("account");
        String password = map.get("password");
        System.out.println("map:" + map);
        log.info("info ---> " + account + ":" + password);

        if(StringUtils.isEmpty(account) || StringUtils.isEmpty(password)){
            return Result.error("用户名或密码为空!");
        }

        Result result = userRemote.login(map);
        Map<String, String> cookieMap = (Map<String, String>) result.getData();
        String jsonHex = cookieMap.get("jsonHex");
        String time = cookieMap.get("time");

        Cookie cookie = new Cookie(LoginFinal.COOKIE_LOGIN_TOKEN, jsonHex);
        cookie.setMaxAge(Integer.valueOf(time));
        cookie.setPath("/");

        response.addCookie(cookie);
        return result;
    }

    @GetMapping("/loginout")
    public Result loginout(HttpServletRequest request, HttpServletResponse response){
        Cookie cookie = CookieUtil.getCookie(LoginFinal.COOKIE_LOGIN_TOKEN, request);
        if(cookie == null){
            return Result.error();
        }
        template.delete(cookie.getValue());
        cookie.setValue(null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return Result.ok();
    }

}
