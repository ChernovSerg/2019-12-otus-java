package ru.otus.chernovsa.hibernate;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.chernovsa.core.dao.UserDao;
import ru.otus.chernovsa.core.model.AddressDataSet;
import ru.otus.chernovsa.core.model.PhoneDataSet;
import ru.otus.chernovsa.core.model.User;
import ru.otus.chernovsa.core.service.DbServiceUser;
import ru.otus.chernovsa.core.service.DbServiceUserImpl;
import ru.otus.chernovsa.hibernate.dao.UserDaoHibernate;
import ru.otus.chernovsa.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.Optional;

public class DbServiceDemo {
    private static Logger logger = LoggerFactory.getLogger(DbServiceDemo.class);

    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(
                "hibernate.cfg.xml", User.class, AddressDataSet.class, PhoneDataSet.class);

        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        UserDao userDao = new UserDaoHibernate(sessionManager);
        DbServiceUser dbServiceUser = new DbServiceUserImpl(userDao);


        User user1 = new User(0, "Вася", new AddressDataSet(0, "Lenina"));
        user1.getPhones().add(new PhoneDataSet(0, "8-903-555-44-22", user1));
        long id = dbServiceUser.saveUser(user1);
        Optional<User> mayBeCreatedUser = dbServiceUser.getUser(id);

        User user2 = new User(1L, "А! Нет. Это же совсем не Вася", new AddressDataSet(0, "Kosmonavtov"));
        user2.getPhones().add(new PhoneDataSet(0, "8-903-888-11-11", user2));
        id = dbServiceUser.saveUser(user2);
        Optional<User> mayBeUpdatedUser = dbServiceUser.getUser(id);

        outputUserOptional("Created user", mayBeCreatedUser);
        outputUserOptional("Updated user", mayBeUpdatedUser);
    }

    private static void outputUserOptional(String header, Optional<User> mayBeUser) {
        System.out.println("-----------------------------------------------------------");
        System.out.println(header);
        mayBeUser.ifPresentOrElse(System.out::println, () -> logger.info("User not found"));
    }
}
