package com.app.moneytransfer.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.app.moneytransfer.model.User;


public class UserDAOImpl extends AbstractHibernateDao implements UserDao {
	
    private static Logger log = Logger.getLogger(UserDAOImpl.class);

    public UserDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        entityClass = User.class;
    }

    /**
     * Find all users
     */
    public List<User>  getAllUsers() {
        List<User> users = new ArrayList<User>();
        try {
            Transaction trans = getCurrentSession().beginTransaction();
            Criteria criteria = getCurrentSession().createCriteria(User.class);
            users = criteria.list();
            trans.commit();
        } catch (HibernateException he) {
            log.error("getAllUsers: Error: " + he);
        }
        return users;
    }
    
    /**
     * Find user by userName
     */
    public User getUserByName(String userName) {
        try {
            Transaction trans = getCurrentSession().beginTransaction();
            Criteria criteria = getCurrentSession().createCriteria(User.class);
            User retrievedUser = (User) criteria.add(Restrictions.eq("userName", userName)).uniqueResult();
            trans.commit();
            return retrievedUser;
        }
        catch (HibernateException he) {
            log.error("getUserByName: Error: " + he);
            throw he;
        }
    }

}
