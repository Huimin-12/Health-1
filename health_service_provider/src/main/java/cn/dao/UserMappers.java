package cn.dao;

import cn.domain.User;

public interface UserMappers {
    User findByUsername(String username);
}
