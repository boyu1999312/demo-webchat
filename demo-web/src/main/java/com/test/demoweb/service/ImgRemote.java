package com.test.demoweb.service;

import com.test.common.entity.ImgBean;
import com.test.common.vo.MsgVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@Component
@FeignClient(value = "img-server")
public interface ImgRemote {

    @PostMapping(value = "/img/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    MsgVo uploadImg(@RequestParam("map") String map, @RequestPart("file") MultipartFile file);



}
