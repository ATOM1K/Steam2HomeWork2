package com.example.userservice.dao;

import com.example.userservice.config.TestDatabaseConfig;
import com.example.userservice.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

/* Интеграционные тесты для UserDAO с использованием Testcontainers
 * Тестирует все CRUD-операции на реальной PostgreSQL в Docker-контейнере*/
class UserDAOTest {

    private static SessionFactory sessionFactory;
    private static UserDAO userDAO;

    @BeforeAll
    static void setUp() {
        // Настраиваем Hibernate для тестовой БД
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySetting("connection.driver_class", "org.postgresql.Driver")
                .applySetting("connection.url", TestDatabaseConfig.getJdbcUrl())
                .applySetting("connection.username", TestDatabaseConfig.getUsername())
                .applySetting("connection.password", TestDatabaseConfig.getPassword())
                .applySetting("dialect", "org.hibernate.dialect.PostgreSQLDialect")
                .applySetting("hbm2ddl.auto", "create-drop") // Создаёт схему перед тестами и удаляет после
                .applySetting("show_sql", "true")
                .build();

        sessionFactory = new MetadataSources(registry)
                .addAnnotatedClass(User.class)
                .buildMetadata()
                .buildSessionFactory();

        userDAO = new UserDAOImpl();
    }

    @AfterAll
    static void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    void testSaveAndFindById() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setAge(30);
        user.setCreatedAt(LocalDateTime.now());

        Long savedId;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            savedId = userDAO.save(user, session);
            transaction.commit();
        }

        assertThat(savedId).isNotNull();

        try (Session session = sessionFactory.openSession()) {
            User foundUser = userDAO.findById(savedId, session);

            assertThat(foundUser).isNotNull();
            assertThat(foundUser.getId()).isEqualTo(savedId);
            assertThat(foundUser.getName()).isEqualTo("Test User");
            assertThat(foundUser.getEmail()).isEqualTo("test@example.com");
            assertThat(foundUser.getAge()).isEqualTo(30);
        }
    }

    @Test
    void testFindAll() {
        // Создаём двух пользователей
        User user1 = createTestUser("User One", "one@example.com", 25);
        User user2 = createTestUser("User Two", "two@example.com", 35);

        saveUser(user1);
        saveUser(user2);

        try (Session session = sessionFactory.openSession()) {
            List<User> users = userDAO.findAll(session);

            assertThat(users).hasSize(2);
            assertThat(users)
                    .extracting("name")
                    .containsExactlyInAnyOrder("User One", "User Two");
        }
    }

    @Test
    void testUpdate() {
        User user = createTestUser("Old Name", "old@example.com", 20);
        Long userId = saveUser(user);

        try (Session session = sessionFactory.openSession()) {
            User foundUser = userDAO.findById(userId, session);
            foundUser.setName("Updated Name");
            foundUser.setAge(25);
            userDAO.update(foundUser, session);
        }

        try (Session session = sessionFactory.openSession()) {
            User updatedUser = userDAO.findById(userId, session);
            assertThat(updatedUser.getName()).isEqualTo("Updated Name");
            assertThat(updatedUser.getAge()).isEqualTo(25);
        }
    }

    @Test
    void testDelete() {
        User user = createTestUser("To Delete", "delete@example.com", 40);
        Long userId = saveUser(user);

        try (Session session = sessionFactory.openSession()) {
            userDAO.delete(userId, session);
        }

        try (Session session = sessionFactory.openSession()) {
            User deletedUser = userDAO.findById(userId, session);
            assertThat(deletedUser).isNull();
        }
    }

    /*Вспомогательный метод для создания тестового пользователя*/
    private User createTestUser(String name, String email, Integer age) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setAge(age);
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }

/* Вспомогательный метод для сохранения пользователя и возврата его ID*/
private Long saveUser(User user) {
    try (Session session = sessionFactory.openSession()) {
        var transaction = session.beginTransaction();
        Long id = userDAO.save(user, session);
        transaction.commit();
        return id;
    }
}
}