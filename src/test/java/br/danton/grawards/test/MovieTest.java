package br.danton.grawards.test;

import br.danton.grawards.util.TesteUtil;
import br.danton.grawards.controller.MovieController;
import br.danton.grawards.controller.ProducerController;
import br.danton.grawards.controller.StudioController;
import br.danton.grawards.model.Movie;
import br.danton.grawards.util.JsonUtil;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Danton Estevam Pinto <dantonepinto@gmail.com>
 */
public class MovieTest {

	private final MovieController movieController;
	private final ProducerController producerController;
	private final StudioController studioController;

	public MovieTest() {
		movieController = TesteUtil.getSimulardorEjb().get(MovieController.class);
		producerController = TesteUtil.getSimulardorEjb().get(ProducerController.class);
		studioController = TesteUtil.getSimulardorEjb().get(StudioController.class);
	}

	@Test
	public void testFindId() {
		Integer id = 1;
		Movie movie = toEntity(movieController.find(id));
		assertEquals(id, movie != null ? movie.getId() : null);
	}

	@Test
	public void testFindIdInexistente() {
		Integer id = Integer.MAX_VALUE;
		Movie movie = toEntity(movieController.find(id));
		assertNotEquals(id, movie != null ? movie.getId() : null);
	}

	@Test
	public void testFindTitle() {
		String title = "Cruising";
		Movie movie = toEntity(movieController.find(title));
		assertEquals(title, movie != null ? movie.getTitle() : null);
	}

	@Test
	public void testFindTitleInexistente() {
		String title = "<INEXISTENTE>";
		Movie movie = toEntity(movieController.find(title));
		assertEquals(null, movie);
	}

	@Test
	public void testFindAll() {
		String title = "";
		List<Movie> movies = toEntities(movieController.find(title));
		assertNotEquals(0, movies.size());
	}

	@Test
	public void testMerge() {
		Movie movie = new Movie();
		movie.setTitle("Movie 1");
		movie.setProducers(Arrays.asList(ProducerTest.toEntity(producerController.find(1))));
		movie.setStudios(Arrays.asList(StudioTest.toEntity(studioController.find(1))));
		movie.setYear(2000);
		movie.setWinner(false);
		movie = toEntity(movieController.merge(movie));

		movie.setTitle("Movie 2");
		movie.setProducers(Arrays.asList(ProducerTest.toEntity(producerController.find(2))));
		movie.setStudios(Arrays.asList(StudioTest.toEntity(studioController.find(2))));
		movie.setYear(movie.getYear() + 1);
		Movie movie2 = toEntity(movieController.merge(movie));

		assertEquals(JsonUtil.GSON.toJson(movie), JsonUtil.GSON.toJson(movie2));
	}

	@Test
	public void testRemove() {
		Integer id = 3;
		Movie movie1 = toEntity(movieController.find(id));
		movieController.remove(movie1.getId());
		Movie movie2 = toEntity(movieController.find(movie1.getId()));
		assertNotEquals(movie1, movie2);
	}

	public static List<Movie> toEntities(Response response) {
		return JsonUtil.GSON.fromJson((String) response.getEntity(), new TypeToken<ArrayList<Movie>>() {
		}.getType());
	}

	public static Movie toEntity(Response response) {
		return JsonUtil.GSON.fromJson((String) response.getEntity(), Movie.class);
	}
}
