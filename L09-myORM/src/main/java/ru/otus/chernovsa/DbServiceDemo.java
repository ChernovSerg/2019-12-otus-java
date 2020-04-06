package ru.otus.chernovsa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.chernovsa.core.dao.JdbcMapper;
import ru.otus.chernovsa.core.dao.ObjectDao;
import ru.otus.chernovsa.core.model.Account;
import ru.otus.chernovsa.core.model.User;
import ru.otus.chernovsa.core.service.DBServiceObject;
import ru.otus.chernovsa.core.service.DbServiceObjectImpl;
import ru.otus.chernovsa.h2.DataSourceH2;
import ru.otus.chernovsa.jdbc.DbExecutor;
import ru.otus.chernovsa.jdbc.dao.JdbcMapperImpl;
import ru.otus.chernovsa.jdbc.dao.ObjectDaoJdbc;
import ru.otus.chernovsa.jdbc.sessionmanager.SessionManagerJdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

/**
 * @author sergey
 * created on 03.02.19.
 */
public class DbServiceDemo {
    private static Logger logger = LoggerFactory.getLogger(DbServiceDemo.class);

    public static void main(String[] args) throws Exception {
        DataSource dataSource = new DataSourceH2();
        DbServiceDemo demo = new DbServiceDemo();
        demo.createTable(dataSource);

        SessionManagerJdbc sessionManager = new SessionManagerJdbc(dataSource);
        DbExecutor<User> dbExecutorForUser = new DbExecutor<>();
        JdbcMapper jdbcMapper = new JdbcMapperImpl();

        //User
        System.out.println();
        ObjectDao<User> userDao = new ObjectDaoJdbc<>(sessionManager, dbExecutorForUser, jdbcMapper, User.class);
        DBServiceObject dbServiceUser = new DbServiceObjectImpl(userDao);
        User userIn = new User(0, 38, "Sergey");
        System.out.println("New Object: " + userIn);
        long id = dbServiceUser.saveObject(userIn);
        Optional<User> userOut = dbServiceUser.getObject(id, User.class);
        userOut.ifPresentOrElse(
                crUser -> logger.info("Object from DB: {}", crUser),
                () -> logger.info("user was not created")
        );

        //Account
        System.out.println();
        DbExecutor<Account> dbExecutorForAcc = new DbExecutor<>();
        ObjectDao<Account> accountDao = new ObjectDaoJdbc<>(sessionManager, dbExecutorForAcc, jdbcMapper, Account.class);
        DBServiceObject dbServiceAccount = new DbServiceObjectImpl(accountDao);
        Account accIn = new Account(0, "Debit", 45.321);
        System.out.println("New Object: " + accIn);
        long idAcc = dbServiceAccount.saveObject(accIn);
        Optional<Account> accOut = dbServiceAccount.getObject(idAcc, Account.class);
        accOut.ifPresentOrElse(
                account -> logger.info("Object from DB: {}", account),
                () -> logger.info("account was not created")
        );

    }

    private void createTable(DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pst = connection.prepareStatement(
                     "create table user(idnt bigint(20) NOT NULL auto_increment, name varchar(255), age int(3));"
                             + "create table account(no bigint(20) NOT NULL auto_increment, type varchar(255), rest number);")
        ) {
            pst.executeUpdate();
        }
        System.out.println("table created");
    }
}
