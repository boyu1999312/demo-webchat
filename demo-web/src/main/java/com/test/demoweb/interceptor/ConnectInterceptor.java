package com.test.demoweb.interceptor;

import com.test.common.constant.LoginFinal;
import com.test.common.entity.UserBean;
import com.test.common.util.CookieUtil;
import com.test.common.util.JsonMapper;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ConnectInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate template;

    private Log log = LogFactory.getLog(ConnectInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String contextPath = request.getServletPath();
        log.info("url ---> " + contextPath);
        Cookie cookie = CookieUtil.getCookie(LoginFinal.COOKIE_LOGIN_TOKEN, request);
        if("/".equals(contextPath)){
            if(cookie == null){
                response.sendRedirect("/html/login");
                return false;
            }
            String hex = cookie.getValue();
            String json = template.opsForValue().get(hex);
            UserBean userBean = JsonMapper.toObject(json, UserBean.class);
            if(userBean == null){
                cookie.setValue(null);
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
                response.sendRedirect("/html/login");
                return false;
            }
            request.setAttribute("userInfo", userBean);
            return true;
        }
        return true;
    }

}
