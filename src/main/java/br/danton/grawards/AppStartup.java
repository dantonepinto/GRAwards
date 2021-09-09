package br.danton.grawards;

import br.danton.grawards.service.MovieService;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
 * @author Danton Estevam Pinto <dantonepinto@gmail.com>
 */
@Singleton
@Startup
public class AppStartup {

	@EJB
	MovieService movieService;

	/**
	 * Esse método é executado assim que a aplicação é publicada no servidor web.
	 */
	@PostConstruct
	public void start() {
		try {
			movieService.importCsv(AppStartup.class.getResourceAsStream("/movielist.csv"));
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Esse método é executado assim que a aplicação começa a ser parada no servidor web.
	 */
	@PreDestroy
	public void stop() {
	}
}
