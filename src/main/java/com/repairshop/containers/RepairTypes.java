package com.repairshop.containers;

import com.repairshop.dao.RepairTypeDAO;
import com.repairshop.model.RepairType;

import java.util.List;

public class RepairTypes {
    private static RepairTypes instance;
    private List<RepairType> repairTypesCache;
    private final RepairTypeDAO repairTypeDAO = new RepairTypeDAO();

    private RepairTypes() {}

    public static synchronized RepairTypes getInstance() {
        if (instance == null) {
            instance = new RepairTypes();
        }
        return instance;
    }

    private void loadCache() {
        if (repairTypesCache == null) {
            try {
                repairTypesCache = repairTypeDAO.readAll();
            } catch (Exception e) {
                System.err.println("Ошибка загрузки типов ремонта в кэш");
                e.printStackTrace();
            }
        }
    }

    public List<RepairType> readAll() {
        loadCache();
        return repairTypesCache;
    }

    public RepairType readById(int id) {
        loadCache();
        for (RepairType type : repairTypesCache) {
            if (type.getId() == id) {
                return type;
            }
        }
        return null;
    }
}
