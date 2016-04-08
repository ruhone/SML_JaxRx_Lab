package se.sml.jaxrs.web;

import static se.sml.jaxrs.ContextLoader.getBean;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import se.sml.jaxrs.model.TeamWeb;
import se.sml.sdj.model.Team;
import se.sml.sdj.model.User;
import se.sml.sdj.service.TeamService;
import se.sml.sdj.service.UserService;

@Path("/teams")
@Produces(MediaType.APPLICATION_XML)
@Consumes(MediaType.APPLICATION_XML)
public final class TeamWebService {

	@Context
	private UriInfo uriInfo;
 
	// Create a Team
	@POST
	public Response addTeam(TeamWeb teamWeb) {
		Team team = new Team(teamWeb.getName(), teamWeb.getStatus());
		getBean(TeamService.class).save(team);
		URI location = uriInfo.getAbsolutePathBuilder().path(getClass(), "getTeamByTeamNameWeb").build(teamWeb.getName());
		return Response.created(location).build();
	}
	
	// Get a Team by Team Name
	@GET
	@Path("/teamName/{teamName}")
	public TeamWeb getTeamByTeamNameWeb(@PathParam("teamName") String teamName) {
		Team team = getBean(TeamService.class).findByName(teamName);
		if (team == null) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		return new TeamWeb(team.getName(), team.getStatus()); 
	}
	
	// Get All Teams
	@GET
	@Path("/all")
	public Response getAllTeams() {
		Collection<Team> teamResult = new ArrayList<>();
		teamResult = getBean(TeamService.class).findAll();
		if (teamResult == null) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		Collection<TeamWeb> teamWebResult = new ArrayList<>();
		teamResult.forEach(tr -> teamWebResult.add(new TeamWeb(tr.getName(), tr.getStatus())));
		GenericEntity<Collection<TeamWeb>> entity = new GenericEntity<Collection<TeamWeb>>(teamWebResult) {};
		return Response.ok(entity).build();

	}
	
	// Update a Team
	@PUT
	@Path("/teamName/{teamName}")
	public Response updateUser(@PathParam("teamName") String teamName, TeamWeb teamWeb) {
		Team team = getBean(TeamService.class).findByName(teamName);
		if (team == null) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		team.setName(teamWeb.getName()).setStatus(teamWeb.getStatus());
		getBean(TeamService.class).save(team);
		return Response.noContent().build();
	}

	// Add a User to a Team
	@PUT
	@Path("addToteam/{teamName}/{userNumber}")
	public Response updateUser(@PathParam("teamName") String teamName, @PathParam("userNumber") String userNumber) {
		User user = getBean(UserService.class).findByUserNumber(userNumber);
		if (user == null) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		getBean(TeamService.class).addUser(teamName, user);			
		return Response.noContent().build();
	}
	
	// Delete a Team [update status to "Inactive"]
	@DELETE
	@Path("/teamName/{teamName}")
	public Response deleteTeam(@PathParam("teamName") String teamName) {
		Team team = getBean(TeamService.class).findByName(teamName);
		if (team == null) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		team.setStatus("Inactive");
		getBean(TeamService.class).save(team);
		return Response.noContent().build();
	}
}
