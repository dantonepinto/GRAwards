package br.danton.grawards.service;

import br.danton.grawards.dao.MovieDao;
import br.danton.grawards.dao.ProducerDao;
import br.danton.grawards.dao.StudioDao;
import br.danton.grawards.model.Movie;
import br.danton.grawards.model.Producer;
import br.danton.grawards.model.Studio;
import br.danton.grawards.util.JpaUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InvalidObjectException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Pattern;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;

/**
 *
 * @author Danton Estevam Pinto <dantonepinto@gmail.com>
 */
@Stateless
public class MovieService {

	public static final int BUFFER_SIZE = 128 * 1024;
	public static final Charset CSV_CHARSET = Charset.forName("UTF-8");

	@EJB
	private MovieDao movieDao;
	@EJB
	private ProducerDao producerDao;
	@EJB
	private StudioDao studioDao;
	@EJB
	private EntityManager entityManager;

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Movie find(Integer id) {
		return movieDao.find(id);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Movie> find(String title) {
		return movieDao.find(title);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Movie merge(Movie movie) {
		return JpaUtil.executeTransaction(entityManager, () -> {
			movie.setProducers(new ArrayList<>(movie.getProducers()));
			movie.setStudios(new ArrayList<>(movie.getStudios()));
			return movieDao.merge(movie);
		});
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remove(Integer id) {
		JpaUtil.executeTransaction(entityManager, () -> movieDao.delete(id));
	}

	/**
	 * Importa todos os filmes do arquivo CSV para a base de dados.<br>
	 * Limpa a base de dados antes de iniciar a importação.
	 *
	 * @param arquivoInputStream
	 * @throws java.io.IOException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void importCsv(InputStream arquivoInputStream) throws IOException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(arquivoInputStream, CSV_CHARSET), BUFFER_SIZE)) {
			readHeader(reader);

			JpaUtil.executeTransaction(entityManager, () -> {
				movieDao.deleteAll();
				producerDao.deleteAll();
				studioDao.deleteAll();

				entityManager.clear();
				parseMovies(reader, movieDao::persist);
			});
		}
	}

	/**
	 * Lê, confere e descarta o cabeçalho contido na primeira linha do arquivo.
	 */
	private void readHeader(BufferedReader reader) throws IOException {
		String header = reader.readLine();
		if (!"year;title;studios;producers;winner".equals(header)) {
			throw new InvalidObjectException("Invalid header: " + header);
		}
	}

	/**
	 * Para cada linha do arquivo CSV, cria um filme e chama o consumer com o filme criado.
	 */
	private void parseMovies(BufferedReader reader, Consumer<Movie> movieConsumer) {
		Map<String, Studio> mapStudioByName = new HashMap<>();
		Map<String, Producer> mapProducerByName = new HashMap<>();

		reader.lines().forEach((line) -> {
			String[] cells = line.split(";", -1);
			Movie movie = new Movie();
			movie.setYear(Integer.parseInt(cells[0]));
			movie.setTitle(cells[1]);
			movie.setStudios(parseNamesToEntities(cells[2], mapStudioByName, Studio::new));
			movie.setProducers(parseNamesToEntities(cells[3], mapProducerByName, Producer::new));
			movie.setWinner(cells.length > 4 && "yes".equals(cells[4]));
			movieConsumer.accept(movie);
		});
	}

	/**
	 * Extrair da melhor forma possível cada um dos nomes contidos na celula, convertendo cada nome para uma única entidade correspondente.
	 */
	private <T> List<T> parseNamesToEntities(String names, Map<String, T> mapEntityByName, Function<String, T> functionCreateNewEntityWithName) {
		return parseNames(names)
				.stream()
				.map(name -> {
					String nameUpperCase = name.toUpperCase();
					T entidade = mapEntityByName.get(nameUpperCase);
					if (entidade == null) {
						entidade = functionCreateNewEntityWithName.apply(name);
						mapEntityByName.put(nameUpperCase, entidade);
					}
					return entidade;
				})
				.toList();
	}

	private static final Pattern SEPARATOR_PATTERN = Pattern.compile("([, ]+and +| *, *)+"); //Transforma "A, B,C ,C, and D,and E ,and F and G." em ["A", "B", "C", "D", "E", "F", "G"].

	/**
	 * Extrair da melhor forma possível cada um dos nomes contidos na celula.
	 */
	private Set<String> parseNames(String names) {
		return new HashSet<>(Arrays.asList(SEPARATOR_PATTERN.split(names.trim())));
	}
}
