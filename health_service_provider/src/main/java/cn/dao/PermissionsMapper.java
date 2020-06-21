package cn.dao;

import cn.domain.Permission;

import java.util.Set;

public interface PermissionsMapper {
    Set<Permission> findByRolesId(int id);
}
