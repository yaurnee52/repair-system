package com.repairshop.containers;

import com.repairshop.dao.MachineModelDAO;
import com.repairshop.model.MachineModel;

import java.sql.SQLException;
import java.util.List;

public class MachineModels {
    private static MachineModels instance;
    private List<MachineModel> machineModelsCache;
    private final MachineModelDAO machineModelDAO = new MachineModelDAO();

    private MachineModels() {}

    public static synchronized MachineModels getInstance() {
        if (instance == null) {
            instance = new MachineModels();
        }
        return instance;
    }

    private void loadCache() {
        if (machineModelsCache == null) {
            try {
                machineModelsCache = machineModelDAO.readAll();
            } catch (Exception e) {
                System.err.println("Ошибка загрузки моделей станков в кэш");
                e.printStackTrace();
            }
        }
    }

    public void refreshCache() {
        this.machineModelsCache = null;
    }

    public MachineModel readById(int id) {
        loadCache();
        for (MachineModel model : machineModelsCache) {
            if (model.getId() == id) {
                return model;
            }
        }
        return null;
    }

    public MachineModel findOrCreate(String brand, int year, String country) throws SQLException {
        MachineModel model = machineModelDAO.findOrCreate(brand, year, country);
        refreshCache();
        return model;
    }
}
