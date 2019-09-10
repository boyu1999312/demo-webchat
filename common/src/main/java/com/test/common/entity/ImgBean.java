package com.test.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@TableName("c_img")
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ImgBean {

    private Long cId;
    private String cName; //图片名称
    private Integer cUid; //发送人
    private String cPath; //图片路径
    private Date cCreatedTime;
    private Date cUpdatedTime;
    private Integer cType; //1-普通图，2-闪图
    private String cCheckUid; //记录看过的人
    private Integer cVersion; //记录版本，改动增加1

}

   

