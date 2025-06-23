package com.repairshop.containers;

import com.repairshop.dao.RoleDAO;
import com.repairshop.model.Role;

import java.util.List;

public class Roles {
    private static Roles instance;
    private List<Role> rolesCache;
    private final RoleDAO roleDAO = new RoleDAO();

    private Roles() {}

    public static synchronized Roles getInstance() {
        if (instance == null) {
            instance = new Roles();
        }
        return instance;
    }

    private void loadCache() {
        if (rolesCache == null) {
            try {
                rolesCache = roleDAO.readAll();
            } catch (Exception e) {
                System.err.println("Ошибка загрузки ролей в кэш");
                e.printStackTrace();
            }
        }
    }

    public Role readByName(String name) {
        loadCache();
        for (Role role : rolesCache) {
            if (role.getRoleName().equals(name)) {
                return role;
            }
        }
        return null;
    }
}
