package br.danton.grawards.controller;

import br.danton.grawards.dto.MinMaxTO;
import br.danton.grawards.model.Producer;
import br.danton.grawards.dto.ProducerTO;
import br.danton.grawards.service.ProducerService;
import br.danton.grawards.util.JsonUtil;
import java.io.IOException;
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
@Path("/producers")
public class ProducerController {

	@EJB
	private ProducerService producerService;

	@GET
	@Path("/{id}")
	@Produces(JsonUtil.APPLICATION_JSON)
	public Response find(@PathParam("id") Integer id) {
		Producer producer = producerService.find(id);
		return Response.ok(JsonUtil.GSON.toJson(producer)).build();
	}

	@GET
	@Path("")
	@Produces(JsonUtil.APPLICATION_JSON)
	public Response find(@QueryParam("name") @DefaultValue("") String name) {
		List<Producer> list = producerService.find(name);
		if (name.isEmpty()) {
			return Response.ok(JsonUtil.GSON.toJson(list)).build();
		} else {
			return Response.ok(JsonUtil.GSON.toJson(list.isEmpty() ? null : list.get(0))).build();
		}
	}

	@PUT
	@Path("")
	@Produces(JsonUtil.APPLICATION_JSON)
	public Response merge(Producer producer) {
		producer = producerService.merge(producer);
		return Response.ok(JsonUtil.GSON.toJson(producer)).build();
	}

	@DELETE
	@Path("/{id}")
	@Produces(JsonUtil.APPLICATION_JSON)
	public Response remove(@PathParam("id") Integer id) {
		producerService.remove(id);
		return Response.ok().build();
	}

	@GET
	@Path("/findByMinMaxIntervalBetweenAwards")
	@Produces(JsonUtil.APPLICATION_JSON)
	public Response findByMinMaxIntervalBetweenAwards() throws IOException {
		MinMaxTO<ProducerTO> minMaxTO = producerService.findByMinMaxIntervalBetweenAwards();
		return Response.ok(JsonUtil.GSON.toJson(minMaxTO)).build();
	}
}
