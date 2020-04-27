package ru.otus.chernovsa.hibernate.dao;


import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.chernovsa.core.dao.UserDao;
import ru.otus.chernovsa.core.dao.UserDaoException;
import ru.otus.chernovsa.core.model.User;
import ru.otus.chernovsa.core.sessionmanager.SessionManager;
import ru.otus.chernovsa.hibernate.sessionmanager.DatabaseSessionHibernate;
import ru.otus.chernovsa.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.Optional;

public class UserDaoHibernate implements UserDao {
  private static Logger logger = LoggerFactory.getLogger(UserDaoHibernate.class);

  private final SessionManagerHibernate sessionManager;

  public UserDaoHibernate(SessionManagerHibernate sessionManager) {
    this.sessionManager = sessionManager;
  }


  @Override
  public Optional<User> findById(long id) {
    DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
    try {
      return Optional.ofNullable(currentSession.getHibernateSession().find(User.class, id));
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    return Optional.empty();
  }


  @Override
  public long saveUser(User user) {
    DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
    try {
      Session hibernateSession = currentSession.getHibernateSession();
      if (user.getId() > 0) {
        hibernateSession.merge(user);
      } else {
        hibernateSession.persist(user);
      }
      return user.getId();
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new UserDaoException(e);
    }
  }

  @Override
  public long updateUser(User object) {
    return 0;
  }

  @Override
  public SessionManager getSessionManager() {
    return sessionManager;
  }
}
