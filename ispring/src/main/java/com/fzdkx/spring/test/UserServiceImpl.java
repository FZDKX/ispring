package com.fzdkx.spring.test;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 发着呆看星
 * @create 2024/8/13
 */
@Slf4j
public class UserServiceImpl implements UserService {
    @Override
    public void queryUserInfo() {
        log.debug("正在查询用户信息......");
    }

    @Override
    public void register(String userName) {
        log.debug("正在注册用户信息：{}", userName);
    }
}
