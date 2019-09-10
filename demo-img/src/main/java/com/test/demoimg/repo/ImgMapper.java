package com.test.demoimg.repo;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.test.common.entity.ImgBean;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface ImgMapper extends BaseMapper<ImgBean> {
}
