package com.test.demoimg.service;

import com.test.common.constant.MsgFinal;
import com.test.common.entity.ImgBean;
import com.test.common.util.JsonMapper;
import com.test.common.vo.MsgVo;
import com.test.demoimg.repo.ImgMapper;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
@RestController
@RequestMapping("/img")
public class ImgService {

    @Autowired
    private ImgMapper imgMapper;

    private Log log = LogFactory.getLog(ImgService.class);

    @PostMapping("/upload")
    public MsgVo upload(@RequestParam("map") String strMap, MultipartFile file) throws IOException {

        ImgBean imgBean = new ImgBean();

        HashMap<String, String> map = JsonMapper.toObject(strMap, HashMap.class);

        Integer cUid = Integer.valueOf(map.get("cUid"));
        Integer cType = Integer.valueOf(map.get("cType"));
        Long cCreatedTime = Long.valueOf(map.get("cCreatedTime"));
        String nickName = map.get("nickName");
        String path = map.get("path");
        Date date = new Date(cCreatedTime);

        log.info("path] : ---- > " + map);
        log.info("file] : ---- > " + file);
        log.info("name] : ---- > " + file.getName());
        log.info("getOriginalFilename] : ---- > " + file.getOriginalFilename());
        log.info("getContentType] : ---- > " + file.getContentType());
        log.info("getInputStream] : ---- > " + file.getInputStream());
        log.info("isEmpty] : ---- > " + file.isEmpty());

        String today = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        String fileName = cUid + "_" + file.getOriginalFilename();
        String newPath = path + today + "/" + fileName;
        String substring = newPath.substring(newPath.indexOf("/images"));

        File localFile = new File(newPath);
        if(!localFile.getParentFile().exists()){
            localFile.mkdirs();
        }
        if(!localFile.exists()){

            file.transferTo(localFile);

            imgBean.setCUid(cUid);
            imgBean.setCType(cType);
            imgBean.setCName(fileName);
            imgBean.setCPath(substring);
            imgBean.setCCreatedTime(date);
            imgBean.setCUpdatedTime(date);

            imgMapper.insert(imgBean);
        }

        return new MsgVo()
                .setId(cUid)
                .setTime(cCreatedTime)
                .setMsg(substring)
                .setType(MsgFinal.IMG_TYPE)
                .setNickName(nickName);
    }
}
