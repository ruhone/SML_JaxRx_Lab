package se.sml.jaxrs.web;

import static se.sml.jaxrs.ContextLoader.getBean;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.springframework.lang.UsesSunHttpServer;

import se.sml.jaxrs.model.UserWeb;
import se.sml.sdj.model.User;
import se.sml.sdj.service.UserService;

@Path("/users")
@Produces(MediaType.APPLICATION_XML)
@Consumes(MediaType.APPLICATION_XML)
public final class UserWebService {

	@Context
	private UriInfo uriInfo;
	 
	 	// Create
	@POST
	public Response addUser(UserWeb userWeb) {

		User user = new User(userWeb.getUsername(), userWeb.getFirstName(), userWeb.getLastName(), userWeb.getUserNumber(), userWeb.getStatus());
		
		getBean(UserService.class).save(user);
		
		URI location = uriInfo.getAbsolutePathBuilder().path(getClass(), "getUserByUserNumberWeb").build(userWeb.getUserNumber());

		return Response.created(location).build();
	}
	
	
	
	// Read
	@GET
	@Path("/userNumber/{userNumber}")
	public UserWeb getUserByUserNumberWeb(@PathParam("userNumber") String userNumber) {

		User user = getBean(UserService.class).findByUserNumber(userNumber);

		if (user == null) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}

		return new UserWeb(user.getUsername(), user.getFirstName(), user.getLastName(), user.getUserNumber(), user.getStatus());
	}
	
	@GET
	@Path("/firstName/{firstName}")
	public Collection<UserWeb> getUserByFirstNameWeb(@PathParam("firstName") String firstName) {

		Collection<User> user = getBean(UserService.class).findByFirstName(firstName);

		if (user == null) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		
		Collection<UserWeb> userWeb = new ArrayList<UserWeb>();
		
		user.forEach(u -> userWeb.add(new UserWeb(u.getUsername(), u.getFirstName(), u.getLastName(), u.getUserNumber(), u.getStatus())));

		return userWeb;
	}
	
	@GET
	@Path("/lastName/{lastName}")
	public Collection<UserWeb> getUserByLastNameWeb(@PathParam("lastName") String lastName) {

		Collection<User> user = getBean(UserService.class).findByLastName(lastName);

		if (user == null) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		
		Collection<UserWeb> userWeb = new ArrayList<UserWeb>();
		
		user.forEach(u -> userWeb.add(new UserWeb(u.getUsername(), u.getFirstName(), u.getLastName(), u.getUserNumber(), u.getStatus())));

		return userWeb;
	}
	
	@GET
	@Path("/username/{username}")
	public UserWeb getUserByUsernameWeb(@PathParam("username") String username) {

		User user = getBean(UserService.class).findByUsername(username);

		if (user == null) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}

		return new UserWeb(user.getUsername(), user.getFirstName(), user.getLastName(), user.getUserNumber(), user.getStatus());
	}

	@GET
	@Path("/team/{team}")
	public Collection<UserWeb> getUserByTeamWeb(@PathParam("team") String team) {

		Collection<User> user = getBean(UserService.class).findUsersByTeam(team);

		if (user == null) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		
		Collection<UserWeb> userWeb = new ArrayList<UserWeb>();
		
		user.forEach(u -> userWeb.add(new UserWeb(u.getUsername(), u.getFirstName(), u.getLastName(), u.getUserNumber(), u.getStatus())));

		return userWeb;
	}
	
	// Update
	@PUT
	@Path("/userNumber/{userNumber}")
	public Response updateUser(@PathParam("userNumber") String userNumber, UserWeb userWeb) {
			
		User user = getBean(UserService.class).findByUserNumber(userNumber.toString());
			
		if (user == null) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
			
		user.setUsername(userWeb.getUsername()).setFirstName(userWeb.getFirstName()).setLastName(userWeb.getLastName()).setUserNumber(userWeb.getUserNumber()).setStatus(userWeb.getStatus());
			
		getBean(UserService.class).save(user);
			
		return Response.noContent().build();
	}
	
}

// localhost:8080/jax-rs-jpa/users/1  --> GET
// if you create a user Id from here it does not matter (affect) while saving into DB as DB follows the 
//model object that you have in your jpa-data project which is auto incremental. It will however create a header location but
// you wont be able to GET user info with that id. you need to check the DB for correct generated ID