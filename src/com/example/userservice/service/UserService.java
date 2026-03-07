package com.example.userservice.service;
import com.example.userservice.dao.UserDAO;
import com.example.userservice.dto.UserDTO;
import com.example.userservice.dto.UserMapper;
import com.example.userservice.entity.User;
import com.example.userservice.exception.DatabaseException;
import com.example.userservice.exception.ValidationException;
import com.example.userservice.config.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
 * Сервис для бизнес‑логики работы с пользователями
 * Использует DAO для взаимодействия с базой данных
 */
public class UserService {
    private static final Logger logger = LogManager.getLogger(UserService.class);
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /*
     * Создаёт нового пользователя
     * @param userDTO DTO с данными нового пользователя
     * @return DTO созданного пользователя
     * @throws ValidationException если данные невалидны
     * @throws DatabaseException если произошла ошибка базы данных
     */
    public UserDTO createUser(UserDTO userDTO) {
        validateUserDTO(userDTO);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                User user = UserMapper.toEntity(userDTO);
                Long id = userDAO.save(user, session);
                user.setId(id);
                transaction.commit();
                logger.info("Создан пользователь с ID: {}", id);
                return UserMapper.toDTO(user);
            } catch (Exception e) {
                transaction.rollback();
                logger.error("Ошибка при создании пользователя", e);
                throw new DatabaseException("Ошибка при создании пользователя", e);
            }
        }
    }

    /*
     * Получает пользователя по ID
     * @param id ID пользователя
     * @return Optional с DTO пользователя или пустой, если не найден
     */
    public Optional<UserDTO> getUserById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = userDAO.findById(id, session);
            return Optional.ofNullable(UserMapper.toDTO(user));
        } catch (Exception e) {
            logger.error("Ошибка при получении пользователя с ID: {}", id, e);
            throw new DatabaseException("Ошибка при получении пользователя", e);
        }
    }

    /*
     * Получает всех пользователей
     * @return список DTO всех пользователей
     */
    public List<UserDTO> getAllUsers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<User> users = userDAO.findAll(session);
            return users.stream()
                    .map(UserMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Ошибка при получении списка пользователей", e);
            throw new DatabaseException("Ошибка при получении списка пользователей", e);
        }
    }

    /*
     * Обновляет существующего пользователя
     * @param userDTO DTO с обновлёнными данными пользователя
     * @return DTO обновлённого пользователя
     * @throws ValidationException если данные невалидны
     * @throws DatabaseException если произошла ошибка базы данных
     */
    public UserDTO updateUser(UserDTO userDTO) {
        validateUserDTO(userDTO);
        if (userDTO.getId() == null) {
            throw new ValidationException("ID пользователя не может быть null при обновлении");
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                User user = UserMapper.toEntity(userDTO);
                userDAO.update(user, session);
                transaction.commit();
                logger.info("Обновлён пользователь с ID: {}", userDTO.getId());
                return userDTO;
            } catch (Exception e) {
                transaction.rollback();
                logger.error("Ошибка при обновлении пользователя с ID: {}", userDTO.getId(), e);
                throw new DatabaseException("Ошибка при обновлении пользователя", e);
            }
        }
    }

    /*
     * Удаляет пользователя по ID
     * @param id ID пользователя для удаления
     * @throws DatabaseException если произошла ошибка базы данных
     */
    public void deleteUser(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                userDAO.delete(id, session);
                transaction.commit();
                logger.info("Удален пользователь с ID: {}", id);
            } catch (Exception e) {
                transaction.rollback();
                logger.error("Ошибка при удалении пользователя с ID: {}", id, e);
                throw new DatabaseException("Ошибка при удалении пользователя", e);
            }
        }
    }

    /*
     * Валидирует данные пользователя в DTO
     * @param userDTO DTO для валидации
     * @throws ValidationException если данные не проходят валидацию
     */
    private void validateUserDTO(UserDTO userDTO) {
        if (userDTO == null) {
            throw new ValidationException("Данные пользователя не могут быть null");
        }
        if (userDTO.getName() == null || userDTO.getName().trim().isEmpty()) {
            throw new ValidationException("Имя пользователя не может быть пустым");
        }
        if (userDTO.getEmail() == null || !userDTO.getEmail().contains("@")) {
            throw new ValidationException("Email должен быть корректным и не пустым");
        }
        if (userDTO.getAge() != null && userDTO.getAge() < 0) {
            throw new ValidationException("Возраст не может быть отрицательным");
        }
    }
}