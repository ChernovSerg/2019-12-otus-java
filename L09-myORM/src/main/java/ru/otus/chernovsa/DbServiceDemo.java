package ru.otus.chernovsa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.chernovsa.core.dao.AccountDao;
import ru.otus.chernovsa.core.dao.UserDao;
import ru.otus.chernovsa.core.model.Account;
import ru.otus.chernovsa.core.model.User;
import ru.otus.chernovsa.core.service.DbServiceAccount;
import ru.otus.chernovsa.core.service.DbServiceAccountImpl;
import ru.otus.chernovsa.core.service.DbServiceUser;
import ru.otus.chernovsa.core.service.DbServiceUserImpl;
import ru.otus.chernovsa.h2.DataSourceH2;
import ru.otus.chernovsa.jdbc.DbExecutor;
import ru.otus.chernovsa.jdbc.dao.AccountDaoJdbc;
import ru.otus.chernovsa.jdbc.dao.ObjectDaoJdbc;
import ru.otus.chernovsa.jdbc.dao.UserDaoJdbc;
import ru.otus.chernovsa.jdbc.sessionmanager.SessionManagerJdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class DbServiceDemo {
    private static Logger logger = LoggerFactory.getLogger(DbServiceDemo.class);

    public static void main(String[] args) throws Exception {
        DataSource dataSource = new DataSourceH2();
        DbServiceDemo demo = new DbServiceDemo();
        demo.createTable(dataSource);

        SessionManagerJdbc sessionManager = new SessionManagerJdbc(dataSource);

        //User
        DbExecutor<User> dbExecutorForUser = new DbExecutor<>();
        System.out.println();
        UserDao userDao =  new UserDaoJdbc(new ObjectDaoJdbc<>(sessionManager, dbExecutorForUser));
        DbServiceUser dbServiceUser = new DbServiceUserImpl(userDao);
        User userIn = new User(0, 38, "Sergey");
        System.out.println("New Object: " + userIn);
        long id = dbServiceUser.saveUser(userIn);
        long id2 = dbServiceUser.saveUser(new User(0, 5, "Vova"));
        Optional<User> userOut = dbServiceUser.getUser(id);
        userOut.ifPresentOrElse(
                crUser -> logger.info("User from DB: {}", crUser),
                () -> logger.info("user was not created")
        );
        Optional<User> userOut2 = dbServiceUser.getUser(id2);
        userOut2.ifPresentOrElse(
                crUser -> logger.info("User from DB: {}", crUser),
                () -> logger.info("user was not created")
        );

        //Account
        System.out.println();
        DbExecutor<Account> dbExecutorForAcc = new DbExecutor<>();
        AccountDao accountDao = new AccountDaoJdbc(new ObjectDaoJdbc<>(sessionManager, dbExecutorForAcc));
        DbServiceAccount dbServiceAccount = new DbServiceAccountImpl(accountDao);
        Account accIn = new Account(0, "Debit", 45.321);
        System.out.println("New Object: " + accIn);
        long idAcc = dbServiceAccount.saveAccount(accIn);
        long idAcc2 = dbServiceAccount.saveAccount(new Account(0, "Credit", 100.00));
        Optional<Account> accOut = dbServiceAccount.getAccount(idAcc);
        accOut.ifPresentOrElse(
                account -> logger.info("Account from DB: {}", account),
                () -> logger.info("account was not created")
        );
        Optional<Account> accOut2 = dbServiceAccount.getAccount(idAcc2);
        accOut2.ifPresentOrElse(
                account -> logger.info("Account from DB: {}", account),
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
