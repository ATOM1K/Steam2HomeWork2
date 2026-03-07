package com.example.userservice.config;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * Утилитный класс для создания и получения SessionFactory Hibernate
 * Обеспечивает централизованный доступ к сессии Hibernate и корректное закрытие ресурсов
 */
public class HibernateUtil {
    private static final Logger logger = LogManager.getLogger(HibernateUtil.class);
    private static SessionFactory sessionFactory;

    /*
     * Статический блок инициализации для создания SessionFactory
     * Выполняется при первом обращении к классу
     */
    static {
        try {
            // Создаём реестр сервисов с настройками из hibernate.cfg.xml
            final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure() // автоматически читает hibernate.cfg.xml из classpath
                    .build();

            // Создаём SessionFactory на основе метаданных
            sessionFactory = new MetadataSources(registry)
                    .buildMetadata()
                    .buildSessionFactory();

            logger.info("Hibernate SessionFactory успешно инициализирован");
        } catch (Exception e) {
            // В случае ошибки — логируем и закрываем реестр
            logger.error("Ошибка инициализации Hibernate SessionFactory", e);

            // Попытка закрыть реестр сервисов при ошибке
            if (sessionFactory != null) {
                sessionFactory.close();
            }

            // Перебрасываем ошибку как фатальную для инициализации класса
            throw new ExceptionInInitializerError(e);
        }
    }

    /*
     * Возвращает экземпляр SessionFactory
     * @return SessionFactory для работы с Hibernate
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            throw new IllegalStateException("SessionFactory не инициализирован!");
        }
        return sessionFactory;
    }

    /*
     * Закрывает SessionFactory при завершении работы приложения
     * Должен быть вызван в блоке finally или при shutdown hook
     */
    public static void shutdown() {
        if (sessionFactory != null) {
            try {
                sessionFactory.close();
                logger.info("Hibernate SessionFactory корректно закрыт");
            } catch (Exception e) {
                logger.error("Ошибка при закрытии SessionFactory", e);
            }
        }
    }
}
