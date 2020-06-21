package cn.service;

import cn.domain.User;

public interface UserService {
    //根据用户名查询用户信息
    User findByUsername(String username);
}
