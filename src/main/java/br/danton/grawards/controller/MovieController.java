package br.danton.grawards.controller;

import br.danton.grawards.model.Movie;
import br.danton.grawards.service.MovieService;
import br.danton.grawards.util.JsonUtil;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 *
 * @author Danton Estevam Pinto <dantonepinto@gmail.com>
 */
@Path("/movies")
public class MovieController {

	@EJB
	private MovieService movieService;

	@GET
	@Path("/{id}")
	@Produces(JsonUtil.APPLICATION_JSON)
	public Response find(@PathParam("id") Integer id) {
		Movie movie = movieService.find(id);
		return Response.ok(JsonUtil.GSON.toJson(movie)).build();
	}

	@GET
	@Path("")
	@Produces(JsonUtil.APPLICATION_JSON)
	public Response find(@QueryParam("title") @DefaultValue("") String title) {
		List<Movie> list = movieService.find(title);
		if (title.isEmpty()) {
			return Response.ok(JsonUtil.GSON.toJson(list)).build();
		} else {
			return Response.ok(JsonUtil.GSON.toJson(list.isEmpty() ? null : list.get(0))).build();
		}
	}

	@PUT
	@Path("")
	@Produces(JsonUtil.APPLICATION_JSON)
	public Response merge(Movie movie) {
		movie = movieService.merge(movie);
		return Response.ok(JsonUtil.GSON.toJson(movie)).build();
	}

	@DELETE
	@Path("/{id}")
	@Produces(JsonUtil.APPLICATION_JSON)
	public Response remove(@PathParam("id") Integer id) {
		movieService.remove(id);
		return Response.ok().build();
	}
}
