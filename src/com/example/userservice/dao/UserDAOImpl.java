package com.example.userservice.dao;

import com.example.userservice.entity.User;
import org.hibernate.Session;
import java.util.List;
import java.util.Optional;

/*
 * Реализация DAO для работы с сущностью User
 */
public class UserDAOImpl implements UserDAO {

    @Override
    public Long save(User user, Session session) {
        return (Long) session.save(user);
    }

    @Override
    public User findById(Long id, Session session) {
        return session.get(User.class, id);
    }

    @Override
    public List<User> findAll(Session session) {
        return session.createQuery("FROM User", User.class).list();
    }

    @Override
    public void update(User user, Session session) {
        session.update(user);
    }

    @Override
    public void delete(Long id, Session session) {
        User user = findById(id, session);
        if (user != null) {
            session.delete(user);
        }
    }
}