package com.app.moneytransfer.service;

import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.app.moneytransfer.util.HibernateUtil;
import com.app.moneytransfer.dao.UserDAOImpl;
import com.app.moneytransfer.model.User;


@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserService implements IUserService {

    private final UserDAOImpl userDAO = new UserDAOImpl(HibernateUtil.getSessionFactory());

	/**
	 * Find by userName
	 * @param userName
	 * @return
	 */
    @GET
    @Path("/{userName}")
    public User getUserByName(@PathParam("userName") String userName) {
        User user = userDAO.getUserByName(userName);
        if (user == null) {
            throw new WebApplicationException("User Not Found", Response.Status.NOT_FOUND);
        }
        return user;
    }
    
    /**
	 * Find by all
	 * @return
	 */
    @GET
    @Path("/all")
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }
    
    /**
     * Create User
     * @param user
     * @return
     */
    @POST
    @Path("/create")
    public User createUser(User user) {
        System.out.println("Querying by userName "+user.getUserName());
        if (userDAO.getUserByName(user.getUserName()) != null) {
            throw new WebApplicationException("User name already exist", Response.Status.BAD_REQUEST);
        }
        userDAO.save(user);
        return userDAO.findById(user.getUserId(), User.class);
    }
    
    /**
     * Find by User Id
     * @param userId
     * @param user
     * @return
     */
    @PUT
    @Path("/{userId}")
    public User updateUser(@PathParam("userId") long userId, User user) {
        return userDAO.update(user);
    }
    
    /**
     * Delete by User Id
     * @param userId
     * @return
     */
    @DELETE
    @Path("/{userId}")
    public Response deleteUser(@PathParam("userId") long userId) {
        try {
            userDAO.delete(userDAO.findById(userId, User.class));
            return Response.status(Response.Status.OK).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
