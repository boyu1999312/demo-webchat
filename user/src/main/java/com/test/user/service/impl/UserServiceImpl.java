package com.test.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.test.common.constant.LoginFinal;
import com.test.common.entity.UserBean;
import com.test.common.util.JsonMapper;
import com.test.common.util.Result;
import com.test.user.repo.UserMapper;
import com.test.user.service.UserService;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service
@RestController
@RequestMapping(value = "/connect", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate template;
    private Log log = LogFactory.getLog(UserServiceImpl.class);


    @Override
    @PostMapping("/login")
    public Result login(@RequestParam Map<String, String> map, HttpServletResponse response) {
        String account = map.get("account");
        String password = map.get("password");

        UserBean userBean = userMapper.selectOne(new QueryWrapper<UserBean>(
                new UserBean().setUAccount(account)
                        .setUPassword(password)));
        map.clear();
        if(userBean == null) {
            return Result.error("账户或密码错误!");
        }
        String json = JsonMapper.toJson(new UserBean()
                .setUNickname(
                        userBean.getUNickname())
                .setUAge(
                        userBean.getUAge())
                .setUId(
                        userBean.getUId()));

        String md5Json = LoginFinal.COOKIE_LOGIN_TOKEN + json + System.currentTimeMillis();
        String jsonHex = DigestUtils.md5DigestAsHex(md5Json.getBytes());
        int time = 3600 * 24 * 30;

        log.info("hex: " + jsonHex
                   + "userJson: " + json
                    + "time: " + time);

        map.put("jsonHex", jsonHex);
        map.put("time", time+"");
        template.opsForValue().set(jsonHex, json, time, TimeUnit.SECONDS);
        return Result.ok(map);
    }
}
