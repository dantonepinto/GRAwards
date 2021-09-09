package br.danton.grawards.test;

import br.danton.grawards.util.TesteUtil;
import br.danton.grawards.controller.ProducerController;
import br.danton.grawards.model.Producer;
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
public class ProducerTest {

	private final ProducerController producerController;

	public ProducerTest() {
		producerController = TesteUtil.getSimulardorEjb().get(ProducerController.class);
	}

	@Test
	public void testFindId() {
		Integer id = 1;
		Producer producer = toEntity(producerController.find(id));
		assertEquals(id, producer != null ? producer.getId() : null);
	}

	@Test
	public void testFindIdInexistente() {
		Integer id = Integer.MAX_VALUE;
		Producer producer = toEntity(producerController.find(id));
		assertNotEquals(id, producer != null ? producer.getId() : null);
	}

	@Test
	public void testFindName() {
		String name = "Allan Carr";
		Producer producer = toEntity(producerController.find(name));
		assertEquals(name, producer != null ? producer.getName() : null);
	}

	@Test
	public void testFindNameInexistente() {
		String name = "<INEXISTENTE>";
		Producer producer = toEntity(producerController.find(name));
		assertEquals(null, producer);
	}

	@Test
	public void testFindAll() {
		String name = "";
		List<Producer> producer = toEntities(producerController.find(name));
		assertNotEquals(0, producer.size());
	}

	@Test
	public void testMerge() {
		String name;
		Producer producer = new Producer();

		name = "Producer 1";
		producer.setName(name);
		producer = toEntity(producerController.merge(producer));
		assertNotEquals(null, producer.getId());
		assertEquals(name, producer.getName());

		name = "Producer 2";
		producer.setName(name);
		producer = toEntity(producerController.merge(producer));
		assertEquals(name, producer.getName());
	}

	@Test
	public void testRemove() {
		Producer producer1 = new Producer();
		producer1.setName("Producer 1");
		producer1 = toEntity(producerController.merge(producer1));

		producerController.remove(producer1.getId());

		Producer producer2 = toEntity(producerController.find(producer1.getId()));
		assertNotEquals(producer1, producer2);
	}

	@Test
	public void testFindByMinMaxIntervalBetweenAwards() throws IOException {
		Response response = producerController.findByMinMaxIntervalBetweenAwards();

		assertEquals(""
				+ "{\n"
				+ "  \"min\": [\n"
				+ "    {\n"
				+ "      \"producer\": \"Joel Silver\",\n"
				+ "      \"interval\": 1,\n"
				+ "      \"previousWin\": 1990,\n"
				+ "      \"followingWin\": 1991\n"
				+ "    }\n"
				+ "  ],\n"
				+ "  \"max\": [\n"
				+ "    {\n"
				+ "      \"producer\": \"Matthew Vaughn\",\n"
				+ "      \"interval\": 13,\n"
				+ "      \"previousWin\": 2002,\n"
				+ "      \"followingWin\": 2015\n"
				+ "    }\n"
				+ "  ]\n"
				+ "}", (String) response.getEntity());
	}

	public static List<Producer> toEntities(Response response) {
		return JsonUtil.GSON.fromJson((String) response.getEntity(), new TypeToken<ArrayList<Producer>>() {
		}.getType());
	}

	public static Producer toEntity(Response response) {
		return JsonUtil.GSON.fromJson((String) response.getEntity(), Producer.class);
	}
}
