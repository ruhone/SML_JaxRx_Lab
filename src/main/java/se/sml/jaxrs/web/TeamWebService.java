package se.sml.jaxrs.web;

import static se.sml.jaxrs.ContextLoader.getBean;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.Consumes;
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
import se.sml.sdj.service.TeamService;

@Path("/teams")
@Produces(MediaType.APPLICATION_XML)
@Consumes(MediaType.APPLICATION_XML)
public final class TeamWebService {

	@Context
	private UriInfo uriInfo;
 
	// Create
	@POST
	public Response addUser(TeamWeb teamWeb) {

		Team team = new Team(teamWeb.getName(), teamWeb.getStatus());
		
		getBean(TeamService.class).save(team);
		
		URI location = uriInfo.getAbsolutePathBuilder().path(getClass(), "getTeamByTeamNameWeb").build(teamWeb.getName());

		return Response.created(location).build();
	}
	
	@GET
	@Path("/teamName/{teamName}")
	public TeamWeb getTeamByTeamNameWeb(@PathParam("teamName") String teamName) {

		Team team = getBean(TeamService.class).findByName(teamName);

		if (team == null) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}

		return new TeamWeb(team.getName(), team.getStatus());
	}
	
	@GET
	@Path("/all")
	public Response getAllTeams() {

		List<Team> result = new ArrayList<>();
		result = getBean(TeamService.class).findAll();

		if (result == null) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}

		result.forEach(e -> System.out.println(e.getName()));

		GenericEntity<List<Team>> entity = new GenericEntity<List<Team>>(result) {};
		return Response.ok(entity).build();
		
		/*	
		 */
	}
	
	// Update
	@PUT
	@Path("/teamName/{teamName}")
	public Response updateUser(@PathParam("teamName") String teamName, TeamWeb teamWeb) {
			
//		Team team = getBean(TeamName.class).findByUserNumber(userNumber.toString()); ?????
		Team team = getBean(TeamService.class).findByName(teamName);
			
		if (team == null) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
			
		team.setName(teamWeb.getName()).setStatus(teamWeb.getStatus());
			
		getBean(TeamService.class).save(team);
			
		return Response.noContent().build();
	}

}
