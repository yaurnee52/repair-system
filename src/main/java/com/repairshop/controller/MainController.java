package com.repairshop.controller;

import com.repairshop.dao.*;
import com.repairshop.model.*;

import java.sql.SQLException;
import java.util.List;

/**
 * Простой контроллер для проверки работы DAO и моделей через консоль.
 */
public class MainController {
    public static void main(String[] args) {
        try {
            // Проверка работы с клиентами
            ClientDAO clientDAO = new ClientDAO();
            System.out.println("=== Клиенты ===");
            List<Client> clients = clientDAO.readAll();
            for (Client c : clients) {
                System.out.println(c);
            }

            // Проверка работы с моделями станков
            MachineModelDAO modelDAO = new MachineModelDAO();
            System.out.println("\n=== Модели станков ===");
            List<MachineModel> models = modelDAO.readAll();
            for (MachineModel m : models) {
                System.out.println(m);
            }

            // Проверка работы с машинами
            MachineDAO machineDAO = new MachineDAO();
            System.out.println("\n=== Станки ===");
            List<Machine> machines = machineDAO.readAll();
            for (Machine mach : machines) {
                System.out.println(mach);
            }

            // Проверка работы с типами ремонта
            RepairTypeDAO repairTypeDAO = new RepairTypeDAO();
            System.out.println("\n=== Типы ремонта ===");
            List<RepairType> repairTypes = repairTypeDAO.readAll();
            for (RepairType rt : repairTypes) {
                System.out.println(rt);
            }

            // Проверка работы с ремонтами
            RepairDAO repairDAO = new RepairDAO();
            System.out.println("\n=== Ремонты ===");
            List<Repair> repairs = repairDAO.readAll();
            for (Repair r : repairs) {
                System.out.println(r);
            }

        } catch (SQLException e) {
            System.err.println("Ошибка при работе с базой данных: " + e.getMessage());
            e.printStackTrace();
        }
    }
}