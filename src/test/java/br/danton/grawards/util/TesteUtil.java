package br.danton.grawards.util;

import br.danton.grawards.AppStartup;
import br.danton.grawards.util.SimuladorEJB;

/**
 *
 * @author Danton Estevam Pinto <dantonepinto@gmail.com>
 */
public class TesteUtil {

	private static SimuladorEJB simulardorEjb;

	public synchronized static SimuladorEJB getSimulardorEjb() {
		if (simulardorEjb == null) {
			simulardorEjb = new SimuladorEJB();

			//Simulando a inicialização da aplicação.
			//Lê o arquivo CSV que está nos resources e inserir os filmes na base de dados.
			AppStartup appStartup = simulardorEjb.get(AppStartup.class);
			appStartup.start();
		}
		return simulardorEjb;
	}
}
