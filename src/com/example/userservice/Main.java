package com.example.userservice;

import com.example.userservice.config.HibernateUtil;
import com.example.userservice.dao.UserDAOImpl;
import com.example.userservice.service.UserService;
import com.example.userservice.ui.ConsoleUI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * Главный класс приложения — точка входа
 */
public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            // Инициализация компонентов приложения
            UserDAOImpl userDAO = new UserDAOImpl();
            UserService userService = new UserService(userDAO);
            ConsoleUI consoleUI = new ConsoleUI(userService);

            logger.info("Приложение User Service запущено");
            System.out.println("Добро пожаловать в User Service!");

            // Запуск консольного интерфейса
            consoleUI.run();

        } catch (Exception e) {
            logger.error("Критическая ошибка при запуске приложения", e);
            System.err.println("Произошла критическая ошибка при запуске приложения: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Закрытие Hibernate SessionFactory при завершении работы
            try {
                HibernateUtil.shutdown();
                logger.info("Hibernate SessionFactory корректно закрыт");
            } catch (Exception e) {
                logger.error("Ошибка при закрытии Hibernate SessionFactory", e);
            }
        }
    }
}