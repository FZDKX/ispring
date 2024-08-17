package com.fzdkx.spring.test.service;

import com.fzdkx.spring.context.annotation.Autowired;
import com.fzdkx.spring.context.annotation.Component;
import com.fzdkx.spring.context.annotation.PropertiesSource;
import com.fzdkx.spring.context.annotation.Value;
import com.fzdkx.spring.test.xhyl.Person;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 发着呆看星
 * @create 2024/8/13
 */
@Slf4j
@Component("userService")
@PropertiesSource("jdbc.properties")
public class UserServiceImpl implements UserService {

    @Value("${username}")
    private String username;

    @Value("${password}")
    private String password;

    @Autowired
    private Person person;

    @Override
    public String queryUserInfo() {
        log.debug("目标方法调用：正在查询用户信息......");
        return "zs";
    }

    @Override
    public void register(String userName) {
        log.debug("目标方法调用：正在注册用户信息：{}", userName);
    }
}
