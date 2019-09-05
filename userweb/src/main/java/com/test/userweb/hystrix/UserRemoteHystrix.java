package com.test.userweb.hystrix;

import com.test.common.entity.UserBean;
import com.test.common.util.Result;
import com.test.userweb.service.UserRemote;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserRemoteHystrix implements UserRemote {


    @Override
    public List<UserBean> findAll() {

        List<UserBean> list = new ArrayList<>();
        list.add(new UserBean().setUAge(18).setUId(1L).setUNickname("小红"));
        return list;
    }

    @Override
    public Result login(String account, String password, HttpServletResponse response) {
        return null;
    }
}
