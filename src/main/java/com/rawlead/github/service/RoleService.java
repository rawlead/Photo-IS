package com.rawlead.github.service;

import com.rawlead.github.entity.Role;

import java.util.List;

public interface RoleService {
    Role getRole(String roleName);
    Role saveRole(Role role);
    Role saveRole(String roleName);
    List<Role> listAllRoles();
    boolean deleteRole(Long roleId);
}
