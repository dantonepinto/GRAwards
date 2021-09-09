package br.danton.grawards;

import br.danton.grawards.util.SimuladorEJB;
import br.danton.grawards.controller.MovieController;
import br.danton.grawards.controller.ProducerController;
import br.danton.grawards.controller.StudioController;
import br.danton.grawards.model.Movie;
import br.danton.grawards.model.Producer;
import br.danton.grawards.model.Studio;
import br.danton.grawards.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Danton Estevam Pinto <dantonepinto@gmail.com>
 */
public class AppHttpServer {

	public static void main(String[] args) throws IOException, URISyntaxException {
		int port = args.length > 0 && args[0].matches("\\d{1,5}") ? Integer.parseInt(args[0]) : 8080;

		SimuladorEJB simulardorEjb = new SimuladorEJB();

		//Simulando a inicialização da aplicação.
		//Lê o arquivo CSV que está nos resources e inserir os filmes na base de dados.
		AppStartup appStartup = simulardorEjb.get(AppStartup.class);
		appStartup.start();

		HttpServer server;
		try {
			server = HttpServer.create(new InetSocketAddress(port), 0);
		} catch (BindException ex) {
			throw ex;
		}
		server.createContext("/", new AppHttpHandler(simulardorEjb));
		server.setExecutor(null); // creates a default executor
		server.start();

		String host = "http://localhost:" + port;

		System.out.println("");
		System.out.println("Produtores:");
		System.out.println("\tGET " + host + "/producers/findByMinMaxIntervalBetweenAwards");
		System.out.println("\tGET " + host + "/producers");
		System.out.println("\tGET " + host + "/producers?name={name}");
		System.out.println("\tGET " + host + "/producers/{id}");
		System.out.println("\tPUT " + host + "/producers");
		System.out.println("\tDELETE " + host + "/producers/{id}");
		System.out.println("");
		System.out.println("Estúdios:");
		System.out.println("\tGET " + host + "/studios/findByMinMaxIntervalBetweenAwards");
		System.out.println("\tGET " + host + "/studios");
		System.out.println("\tGET " + host + "/studios?name={name}");
		System.out.println("\tGET " + host + "/studios/{id}");
		System.out.println("\tPUT " + host + "/studios");
		System.out.println("\tDELETE " + host + "/studios/{id}");
		System.out.println("");
		System.out.println("Filmes:");
		System.out.println("\tGET " + host + "/movies");
		System.out.println("\tGET " + host + "/movies?title={title}");
		System.out.println("\tGET " + host + "/movies/{id}");
		System.out.println("\tPUT " + host + "/movies");
		System.out.println("\tDELETE " + host + "/movies/{id}");

		//Abre a url no browser
		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
			Desktop.getDesktop().browse(new URI("http://localhost:" + port + "/producers/findByMinMaxIntervalBetweenAwards"));
		}
	}

	static class AppHttpHandler implements HttpHandler {

		private static final Charset UTF8 = Charset.forName("UTF-8");

		private final SimuladorEJB simulardorEjb;

		public AppHttpHandler(SimuladorEJB simulardorEjb) {
			this.simulardorEjb = simulardorEjb;
		}

		@Override
		public void handle(HttpExchange httpExchange) throws IOException {
			String method = httpExchange.getRequestMethod().toUpperCase();
			String uriPath = httpExchange.getRequestURI().getPath();
			String uriQuery = httpExchange.getRequestURI().getQuery();
			if (uriQuery == null) {
				uriQuery = "";
			}

			//Simulando JAX-RS
			if (uriPath.matches("/producers(/.*)?")) {
				ProducerController producerController = simulardorEjb.get(ProducerController.class);
				if ("GET".equals(method) && uriPath.equals("/producers/findByMinMaxIntervalBetweenAwards")) {
					send(httpExchange, producerController.findByMinMaxIntervalBetweenAwards());
				} else if ("GET".equals(method) && uriPath.matches("/producers/\\d+")) {
					Integer id = Integer.parseInt(uriPath.replaceAll("\\D", ""));
					send(httpExchange, producerController.find(id));
				} else if ("GET".equals(method) && uriPath.matches("/producers")) {
					String name = uriQuery.replaceFirst("^(.*[?&])?(name=([^&]+))?.*", "$3");
					send(httpExchange, producerController.find(name));
				} else if ("PUT".equals(method) && uriPath.matches("/producers")) {
					Producer producer = JsonUtil.GSON.fromJson(new InputStreamReader(httpExchange.getRequestBody(), UTF8), Producer.class);
					send(httpExchange, producerController.merge(producer));
				} else if ("DELETE".equals(method) && uriPath.matches("/producers/\\d+")) {
					Integer id = Integer.parseInt(uriPath.replaceAll("\\D", ""));
					producerController.remove(id);
					send(httpExchange, "");
				} else {
					httpExchange.sendResponseHeaders(404, 0);
				}
			} else if (uriPath.matches("/studios(/.*)?")) {
				StudioController studioController = simulardorEjb.get(StudioController.class);
				if ("GET".equals(method) && uriPath.equals("/studios/findByMinMaxIntervalBetweenAwards")) {
					send(httpExchange, studioController.findByMinMaxIntervalBetweenAwards());
				} else if ("GET".equals(method) && uriPath.matches("/studios/\\d+")) {
					Integer id = Integer.parseInt(uriPath.replaceAll("\\D", ""));
					send(httpExchange, studioController.find(id));
				} else if ("GET".equals(method) && uriPath.matches("/studios")) {
					String name = uriQuery.replaceFirst("^(.*[?&])?(name=([^&]+))?.*", "$3");
					send(httpExchange, studioController.find(name));
				} else if ("PUT".equals(method) && uriPath.matches("/studios")) {
					Studio studio = JsonUtil.GSON.fromJson(new InputStreamReader(httpExchange.getRequestBody(), UTF8), Studio.class);
					send(httpExchange, studioController.merge(studio));
				} else if ("DELETE".equals(method) && uriPath.matches("/studios/\\d+")) {
					Integer id = Integer.parseInt(uriPath.replaceAll("\\D", ""));
					studioController.remove(id);
					send(httpExchange, "");
				} else {
					httpExchange.sendResponseHeaders(404, 0);
				}
			} else if (uriPath.matches("/movies(/.*)?")) {
				MovieController movieController = simulardorEjb.get(MovieController.class);
				if ("GET".equals(method) && uriPath.matches("/movies/\\d+")) {
					Integer id = Integer.parseInt(uriPath.replaceAll("\\D", ""));
					send(httpExchange, movieController.find(id));
				} else if ("GET".equals(method) && uriPath.matches("/movies")) {
					String title = uriQuery.replaceFirst("^(.*[?&])?(title=([^&]+))?.*", "$3");
					send(httpExchange, movieController.find(title));
				} else if ("PUT".equals(method) && uriPath.matches("/movies")) {
					Movie movie = JsonUtil.GSON.fromJson(new InputStreamReader(httpExchange.getRequestBody(), UTF8), Movie.class);
					send(httpExchange, movieController.merge(movie));
				} else if ("DELETE".equals(method) && uriPath.matches("/movies/\\d+")) {
					Integer id = Integer.parseInt(uriPath.replaceAll("\\D", ""));
					movieController.remove(id);
					send(httpExchange, "");
				} else {
					httpExchange.sendResponseHeaders(404, 0);
				}
			} else {
				httpExchange.sendResponseHeaders(404, 0);
			}
		}

		private void send(HttpExchange httpExchange, Response response) throws IOException {
			send(httpExchange, (String) response.getEntity());
		}

		private void send(HttpExchange httpExchange, String body) throws IOException {
			byte[] bytes = body.getBytes(UTF8);
			httpExchange.getResponseHeaders().set("Content-Type", JsonUtil.APPLICATION_JSON);
			httpExchange.sendResponseHeaders(200, bytes.length);
			try (OutputStream os = httpExchange.getResponseBody()) {
				os.write(bytes);
			}
		}
	}
}
