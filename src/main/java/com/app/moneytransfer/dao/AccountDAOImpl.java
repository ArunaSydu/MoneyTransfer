package com.app.moneytransfer.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.app.moneytransfer.model.Account;

public class AccountDAOImpl extends AbstractHibernateDao implements IAccountDao {

    private static Logger log = Logger.getLogger(AccountDAOImpl.class);

    public AccountDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        entityClass = Account.class;
    }

    /**
     * Get all accounts.
     */
    @SuppressWarnings("unchecked")
    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<Account>();
        try {
            Transaction trans = getCurrentSession().beginTransaction();
            Criteria criteria = getCurrentSession().createCriteria(Account.class);
            accounts = criteria.list();
            trans.commit();
        } catch (HibernateException he) {
            log.error("getAllAccounts: Error: " + he);
        }
        return accounts;
    }
}
