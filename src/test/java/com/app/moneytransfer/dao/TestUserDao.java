package com.app.moneytransfer.dao;

import com.app.moneytransfer.model.User;
import com.app.moneytransfer.util.HibernateUtil;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Test class for User
 * @author Aruna
 */
public class TestUserDao {

	private static UserDAOImpl userDAO;

	
	@BeforeClass
	public static void setup() {
		userDAO = new UserDAOImpl(HibernateUtil.getSessionFactory());
	}

	@Before
	@Test
	public void testCreateUser() {
		User savedUser = new User("Jax-Rs","jaxRs@Test.com");
		userDAO.save(savedUser);
		User userAfterInsert = userDAO.findById(savedUser.getUserId(), User.class);
		assertThat(userAfterInsert.getUserName(), is(equalTo("Jax-Rs")));
		assertThat(userAfterInsert.getEmailAddress(), is(equalTo("jaxRs@Test.com")));
	}

	@Test	
	public void testGetAllUsers() {
		List<User> allUsers = userDAO.getAllUsers();
		assertThat(allUsers, not(empty()));
	}

	@Test
	public void testGetUserById() {
		User retrievedUser = userDAO.findById(2L, User.class);
		assertTrue(retrievedUser.getUserName().equals("Jax-Rs"));
	}

	
	@Test
	public void testGetNonExistingUserById() {
	    User retrievedUser = userDAO.findById(10L, User.class);
        assertThat(retrievedUser, is(nullValue()));
	}

	@Test
	public void testGetNonExistingUserByName() {
        User retrievedUser = userDAO.getUserByName("failed");
        assertThat(retrievedUser, is(nullValue()));
	}

	@Test
	public void testUpdateUser() {
		User updatedUser = new User("Jax-Rs", "emailUpdated@gmail.com");
		updatedUser.setUserId(1L);
        updatedUser = userDAO.update(updatedUser);
        userDAO.update(updatedUser);
        User userAfterUpdate = userDAO.findById(updatedUser.getUserId(), User.class);
        assertThat(userAfterUpdate.getEmailAddress(), is(equalTo("emailUpdated@gmail.com")));
	}

	@Test
	public void testDeleteUser() {
        User deletedUser = userDAO.findById(1L, User.class);
		userDAO.delete(deletedUser);
        deletedUser = userDAO.findById(1L, User.class);
        assertThat(deletedUser, is(nullValue()));
	}

}
