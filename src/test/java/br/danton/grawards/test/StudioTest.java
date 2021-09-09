package br.danton.grawards.test;

import br.danton.grawards.util.TesteUtil;
import br.danton.grawards.controller.StudioController;
import br.danton.grawards.model.Studio;
import br.danton.grawards.util.JsonUtil;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Danton Estevam Pinto <dantonepinto@gmail.com>
 */
public class StudioTest {

	private final StudioController studioController;

	public StudioTest() {
		studioController = TesteUtil.getSimulardorEjb().get(StudioController.class);
	}

	@Test
	public void testFindId() {
		Integer id = 1;
		Studio studio = toEntity(studioController.find(id));
		assertEquals(id, studio != null ? studio.getId() : null);
	}

	@Test
	public void testFindIdInexistente() {
		Integer id = Integer.MAX_VALUE;
		Studio studio = toEntity(studioController.find(id));
		assertNotEquals(id, studio != null ? studio.getId() : null);
	}

	@Test
	public void testFindName() {
		String name = "MGM";
		Studio studio = toEntity(studioController.find(name));
		assertEquals(name, studio != null ? studio.getName() : null);
	}

	@Test
	public void testFindNameInexistente() {
		String name = "<INEXISTENTE>";
		Studio studio = toEntity(studioController.find(name));
		assertEquals(null, studio);
	}

	@Test
	public void testFindAll() {
		String name = "";
		List<Studio> studios = toEntities(studioController.find(name));
		assertNotEquals(0, studios.size());
	}

	@Test
	public void testMerge() {
		String name;
		Studio studio = new Studio();

		name = "Studio 1";
		studio.setName(name);
		studio = toEntity(studioController.merge(studio));
		assertNotEquals(null, studio.getId());
		assertEquals(name, studio.getName());

		name = "Studio 2";
		studio.setName(name);
		studio = toEntity(studioController.merge(studio));
		assertEquals(name, studio.getName());
	}

	@Test
	public void testRemove() {
		Studio studio1 = new Studio();
		studio1.setName("Studio 1");
		studio1 = toEntity(studioController.merge(studio1));

		studioController.remove(studio1.getId());

		Studio studio2 = toEntity(studioController.find(studio1.getId()));
		assertNotEquals(studio1, studio2);
	}

	@Test
	public void testFindByMinMaxIntervalBetweenAwards() throws IOException {
		Response response = studioController.findByMinMaxIntervalBetweenAwards();

		assertEquals(""
				+ "{\n"
				+ "  \"min\": [\n"
				+ "    {\n"
				+ "      \"studio\": \"Warner Bros.\",\n"
				+ "      \"interval\": 1,\n"
				+ "      \"previousWin\": 1999,\n"
				+ "      \"followingWin\": 2000\n"
				+ "    },\n"
				+ "    {\n"
				+ "      \"studio\": \"Paramount Pictures\",\n"
				+ "      \"interval\": 1,\n"
				+ "      \"previousWin\": 2008,\n"
				+ "      \"followingWin\": 2009\n"
				+ "    },\n"
				+ "    {\n"
				+ "      \"studio\": \"Paramount Pictures\",\n"
				+ "      \"interval\": 1,\n"
				+ "      \"previousWin\": 2009,\n"
				+ "      \"followingWin\": 2010\n"
				+ "    },\n"
				+ "    {\n"
				+ "      \"studio\": \"Columbia Pictures\",\n"
				+ "      \"interval\": 1,\n"
				+ "      \"previousWin\": 2017,\n"
				+ "      \"followingWin\": 2018\n"
				+ "    }\n"
				+ "  ],\n"
				+ "  \"max\": [\n"
				+ "    {\n"
				+ "      \"studio\": \"Paramount Pictures\",\n"
				+ "      \"interval\": 15,\n"
				+ "      \"previousWin\": 1993,\n"
				+ "      \"followingWin\": 2008\n"
				+ "    }\n"
				+ "  ]\n"
				+ "}", (String) response.getEntity());
	}

	public static List<Studio> toEntities(Response response) {
		return JsonUtil.GSON.fromJson((String) response.getEntity(), new TypeToken<ArrayList<Studio>>() {
		}.getType());
	}

	public static Studio toEntity(Response response) {
		return JsonUtil.GSON.fromJson((String) response.getEntity(), Studio.class);
	}
}
