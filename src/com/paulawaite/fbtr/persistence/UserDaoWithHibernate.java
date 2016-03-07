package com.paulawaite.fbtr.persistence;

import com.paulawaite.fbtr.entity.User;
import com.paulawaite.fbtr.entity.UsersRoles;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by paulawaite on 2/2/16.
 */
public class UserDaoWithHibernate implements UserDao {

    private final Logger log = Logger.getLogger(this.getClass());

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<User>();
        Session session = SessionFactoryProvider.getSessionFactory().openSession();
        session.createCriteria(User.class).list();
        return users;
    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public void deleteUser(User user) {

    }

    @Override
    public int addUser(User user) {
        Session session = SessionFactoryProvider.getSessionFactory().openSession();
        Transaction tx = null;
        Integer userId = null;
        try {
            tx = session.beginTransaction();
            userId = (Integer) session.save(user);
            // for now, give any new user a role of user
            session.save(createUserRole(user));
            tx.commit();
            log.info("Added user: " + user + " with id of: " + userId);
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
           log.error(e);
        } finally {
            session.close();
        }
        return userId;
    }

    private UsersRoles createUserRole(User user) {

        UsersRoles usersRoles = new UsersRoles();
        usersRoles.setEmailAddress(user.getEmailAddress());
        usersRoles.setRole("user");
        return usersRoles;
    }
}
