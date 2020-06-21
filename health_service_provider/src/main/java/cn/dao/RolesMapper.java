package cn.dao;

import cn.domain.Role;

import java.util.Set;

public interface RolesMapper {
    Set<Role> findByUserId(int id);
}
