package com.test.user.service;


import com.test.common.util.Result;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface UserService {


    Result login(Map<String, String> map, HttpServletResponse response);
}
