package com.rawlead.github.controller;

import com.rawlead.github.entity.Role;
import com.rawlead.github.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RoleController {
    private RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping(value = "/api/roles")
    List<Role> getAllRoles() {
        return roleService.listAllRoles();
    }

    @GetMapping(value = "/api/roles/{roleName}")
    Role getRole(@PathVariable String roleName) {
        return roleService.getRole(roleName);
    }

    @PostMapping(value = "/api/roles")
    Role addRole(@RequestBody Role role) {
        return roleService.saveRole(role);
    }

    @DeleteMapping(value = "/api/roles/{roleId}")
    boolean deleteRole(@PathVariable Long roleId) {
        return roleService.deleteRole(roleId);
    }


}
