package com.test.user.repo;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.test.common.entity.UserBean;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UserMapper extends BaseMapper<UserBean> {
}
