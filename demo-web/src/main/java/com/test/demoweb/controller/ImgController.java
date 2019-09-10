package com.test.demoweb.controller;

import com.test.common.entity.ImgBean;
import com.test.common.util.JsonMapper;
import com.test.common.util.Result;
import com.test.common.vo.MsgVo;
import com.test.demoweb.service.ImgRemote;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/img")
public class ImgController {

    private Log log = LogFactory.getLog(ImgController.class);

    @Autowired
    private ImgRemote imgRemote;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result uploadImg(@RequestParam Map<String, String> map, @RequestParam("file") MultipartFile file){

        log.info("imgBean] : ----> " + map);
        log.info("file] : ---- > " + file);

        String path = ImgController.class.getClassLoader().getResource("static/images/").getPath();
        map.put("path", path);
        log.info("path] : ---- > " + path);

        MsgVo msgVo = imgRemote.uploadImg(JsonMapper.toJson(map), file);
        if(msgVo == null){
            return Result.error();
        }
        return Result.ok(msgVo);
    }
}
