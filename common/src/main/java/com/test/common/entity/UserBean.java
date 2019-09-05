package com.test.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@TableName("c_user")
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserBean {

    private Long uId;
    private String uAccount;
    private String uPassword;
    private String uNickname;
    private Integer uAge;
}
