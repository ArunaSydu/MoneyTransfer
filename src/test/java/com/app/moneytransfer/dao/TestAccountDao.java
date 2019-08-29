package com.app.moneytransfer.dao;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.app.moneytransfer.model.Account;
import com.app.moneytransfer.util.HibernateUtil;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test AccountDao
 * @author Aruna
 */
public class TestAccountDao {

	private static AccountDAOImpl accountDAO;

	private static final BigDecimal BALANCE = new BigDecimal(99.99).setScale(2, RoundingMode.HALF_EVEN);

	@BeforeClass
	public static void setup() {
		accountDAO = new AccountDAOImpl(HibernateUtil.getSessionFactory());
	}

    @Before
    @Test
    public void testCreateAccount() {       
        Account account = new Account("testingAccount1", BALANCE, "CNY");
        accountDAO.save(account);
        Account afterCreation = accountDAO.findById(account.getAccountId(), Account.class);
        assertThat(afterCreation.getUserName(), is(equalTo("testingAccount1")));
        assertThat(afterCreation.getCurrencyCode(), is(equalTo("CNY")));
        assertThat(afterCreation.getBalance(), is(equalTo(BALANCE)));
    }

    @Test
    public void testGetAllAccounts() {
        List<Account> allAccounts = accountDAO.getAllAccounts();
        assertThat(allAccounts, not(empty()));
    }

    @Test
    public void testGetAccountById() {
            Account account = accountDAO.findById(1L, Account.class);
            assertThat(account.getBalance(), is(equalTo(BALANCE)));
        }
    }


