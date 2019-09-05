package com.test.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 传递消息实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class MsgVo {

    private Integer id;
    private String msg;
    private String nickName;
    /**
     * 消息类型 1是心跳信息 2是普通消息
     */
    private Integer type;
    /**
     * 发送时间
     */
    private Date time;
}
