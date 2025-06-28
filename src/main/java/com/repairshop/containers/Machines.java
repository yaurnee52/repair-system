package com.repairshop.containers;

import com.repairshop.dao.MachineDAO;
import com.repairshop.model.Machine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Machines {
    private static Machines instance;
    private List<Machine> machinesCache;
    private final MachineDAO machineDAO = new MachineDAO();

    private Machines() {}

    public static synchronized Machines getInstance() {
        if (instance == null) {
            instance = new Machines();
        }
        return instance;
    }

    private void loadCache() {
        if (machinesCache == null) {
            try {
                machinesCache = machineDAO.readAll();
            } catch (Exception e) {
                System.err.println("Ошибка загрузки станков в кэш");
                e.printStackTrace();
                machinesCache = Collections.emptyList();
            }
        }
    }

    public void refreshCache() {
        this.machinesCache = null;
    }

    public Machine readById(int id) {
        loadCache();
        for (Machine machine : machinesCache) {
            if (machine.getId() == id) {
                return machine;
            }
        }
        return null;
    }

    public List<Machine> readByClientId(int clientId) {
        loadCache();
        List<Machine> result = new ArrayList<>();
        for (Machine machine : machinesCache) {
            if (machine.getClientId() == clientId) {
                result.add(machine);
            }
        }
        return result;
    }
} 