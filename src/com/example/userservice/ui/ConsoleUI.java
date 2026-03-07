package com.example.userservice.ui;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;
import java.util.Scanner;

/*
 * Консольный пользовательский интерфейс для взаимодействия с приложением
 */
public class ConsoleUI {
    private static final Logger logger = LogManager.getLogger(ConsoleUI.class);
    private final UserService userService;
    private final Scanner scanner;

    public ConsoleUI(UserService userService) {
        this.userService = userService;
        this.scanner = new Scanner(System.in);
    }

    /*
     * Запускает консольное меню приложения
     */
    public void run() {
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    createUser();
                    break;
                case 2:
                    getUser();
                    break;
                case 3:
                    getAllUsers();
                    break;
                case 4:
                    updateUser();
                    break;
                case 5:
                    deleteUser();
                    break;
                case 0:
                    running = false;
                    System.out.println("Выход из приложения...");
                    break;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n=== User Service ===");
        System.out.println("1. Создать пользователя");
        System.out.println("2. Получить пользователя по ID");
        System.out.println("3. Получить всех пользователей");
        System.out.println("4. Обновить пользователя");
        System.out.println("5. Удалить пользователя");
        System.out.println("0. Выход");
        System.out.print("Выберите действие: ");
    }

    private int getUserChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void createUser() {
        System.out.print("Имя: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Возраст: ");
        Integer age = Integer.parseInt(scanner.nextLine());

        UserDTO userDTO = new UserDTO(null, name, email, age, null);
        UserDTO createdUser = userService.createUser(userDTO);
        System.out.println("Пользователь создан с ID: " + createdUser.getId());
    }

    private void getUser() {
        System.out.print("ID пользователя: ");
        Long id = Long.parseLong(scanner.nextLine());
        userService.getUserById(id)
                .ifPresentOrElse(
                        user -> System.out.println("Найден пользователь: " + user),
                        () -> System.out.println("Пользователь не найден")
                );
    }

    private void getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("Пользователи не найдены");
        } else {
            users.forEach(System.out::println);
        }
    }

    private void updateUser() {
        System.out.print("ID пользователя: ");
        Long id = Long.parseLong(scanner.nextLine());
        System.out.print("Новое имя: ");
        String name = scanner.nextLine();
        System.out.print("Новый email: ");
        String email = scanner.nextLine();
        System.out.print("Новый возраст: ");
        Integer age = Integer.parseInt(scanner.nextLine());

        UserDTO userDTO = new UserDTO(id, name, email, age, null);
        UserDTO updatedUser = userService.updateUser(userDTO);
        System.out.println("Пользователь обновлён: " + updatedUser);
    }

    private void deleteUser() {
        System.out.print("ID пользователя для удаления: ");
        Long id = Long.parseLong(scanner.nextLine());
        userService.deleteUser(id);
        System.out.println("Пользователь с ID " + id + " удалён");
    }
}