package com.rawlead.github.service.impl;

import com.rawlead.github.entity.Role;
import com.rawlead.github.repository.RoleRepository;
import com.rawlead.github.service.RoleService;
import com.rawlead.github.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getRole(String roleName) {
        return roleRepository.findByName(roleName);
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role saveRole(String roleName) {
        return roleRepository.save(new Role(roleName));
    }

    @Override
    public List<Role> listAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public boolean deleteRole(Long roleId) {
        Role role = roleRepository.findOne(roleId);
        if (role == null)
            return false;
        role.getUsers().forEach(user -> user.getRoles().remove(role));
        roleRepository.delete(role);
        return true;
    }
}
