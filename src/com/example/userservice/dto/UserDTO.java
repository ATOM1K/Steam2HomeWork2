package com.example.userservice.dto;

import java.time.LocalDateTime;

/*
 * DTO (Data Transfer Object) для передачи данных пользователя между слоями приложения
 * Используется для отделения логики работы с БД от представления данных
 */
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private Integer age;
    private LocalDateTime createdAt;

    /*
     * Конструктор по умолчанию (без параметров)
     * Необходим для сериализации и фреймворков
     */
    public UserDTO() {}

    /*
     * Полный конструктор для создания DTO с заданными параметрами
     * @param id уникальный идентификатор пользователя
     * @param name имя пользователя
     * @param email адрес электронной почты пользователя
     * @param age возраст пользователя
     * @param createdAt дата и время создания записи
     */
    public UserDTO(Long id, String name, String email, Integer age, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.createdAt = createdAt;
    }

    /*
     * Геттер для ID пользователя
     * @return уникальный идентификатор пользователя или null, если не задан
     */
    public Long getId() {
        return id;
    }

    /*
     * Сеттер для ID пользователя
     * @param id уникальный идентификатор пользователя
     */
    public void setId(Long id) {
        this.id = id;
    }

    /*
     * Геттер для имени пользователя
     * @return имя пользователя или null, если не задано
     */
    public String getName() {
        return name;
    }

    /*
     * Сеттер для имени пользователя
     * @param name имя пользователя
     */
    public void setName(String name) {
        this.name = name;
    }

    /*
     * Геттер для email пользователя
     * @return адрес электронной почты или null, если не задан
     */
    public String getEmail() {
        return email;
    }

    /*
     * Сеттер для email пользователя
     * @param email адрес электронной почты
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /*
     * Геттер для возраста пользователя
     * @return возраст пользователя или null, если не задан
     */
    public Integer getAge() {
        return age;
    }

    /*
     * Сеттер для возраста пользователя
     * @param age возраст пользователя
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /*
     * Геттер для даты создания записи
     * @return дата и время создания пользователя или null, если не заданы
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /*
     * Сеттер для даты создания записи
     * @param createdAt дата и время создания пользователя
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /*
     * Переопределённый метод toString() для удобного вывода информации о пользователе
     * @return строковое представление объекта UserDTO
     */
    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", name='" + name + "'" +
                ", email='" + email + "'" +
                ", age=" + age +
                ", createdAt=" + createdAt +
                "}";
    }
}