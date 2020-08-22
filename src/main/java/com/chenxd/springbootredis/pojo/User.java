package com.chenxd.springbootredis.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author create by xiaodong.chen
 * @create 2020/08/20
 * @email xiaodong.chen@huixiaoer.com
 * @description
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private String name;
    private Date birthday;
    private Integer age;
    private List<String> hobby;
}
