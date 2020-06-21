package cn.service.impl;

import cn.dao.PermissionsMapper;
import cn.dao.RolesMapper;
import cn.dao.UserMappers;
import cn.domain.Permission;
import cn.domain.Role;
import cn.domain.User;
import cn.service.UserService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMappers userMapper;
    @Autowired
    private RolesMapper rolesMapper;
    @Autowired
    private PermissionsMapper permissionsMapper;
    @Override
    public User findByUsername(String username) {
        //根据用户名查询用户信息
        User user = userMapper.findByUsername(username);
        //获取用户id
        Integer userId = user.getId();
        //根据用户id查询角色
        Set<Role> roles = rolesMapper.findByUserId(userId);
        //遍历角色信息
        for (Role role : roles) {
            //获取角色id，根据角色id进行权限的查询
            Integer roleId = role.getId();
            Set<Permission> permissions = this.permissionsMapper.findByRolesId(roleId);
            //把查询到的权限存储到role当中
            role.setPermissions(permissions);
        }
        user.setRoles(roles);
        return user;
    }
}
