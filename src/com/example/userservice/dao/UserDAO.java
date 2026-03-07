package com.example.userservice.dao;

import com.example.userservice.entity.User;
import org.hibernate.Session;
import java.util.List;
import java.util.Optional;

/*
 * Интерфейс DAO для работы с сущностью User
 */
public interface UserDAO {

    /*
     * Сохраняет нового пользователя в базу данных
     * @param user сущность User для сохранения
     * @param session Hibernate Session
     * @return ID сохранённого пользователя
     */
    Long save(User user, Session session);

    /*
     * Находит пользователя по ID
     * @param id ID пользователя
     * @param session Hibernate Session
     * @return найденный пользователь или null, если не найден
     */
    User findById(Long id, Session session);

    /*
     * Получает всех пользователей из базы данных
     * @param session Hibernate Session
     * @return список всех пользователей
     */
    List<User> findAll(Session session);

    /*
     * Обновляет существующего пользователя
     * @param user обновлённая сущность User
     * @param session Hibernate Session
     */
    void update(User user, Session session);

    /*
     * Удаляет пользователя по ID
     * @param id ID пользователя для удаления
     * @param session Hibernate Session
     */
    void delete(Long id, Session session);
}