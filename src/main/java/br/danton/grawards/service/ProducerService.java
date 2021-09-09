package br.danton.grawards.service;

import br.danton.grawards.dao.ProducerDao;
import br.danton.grawards.dto.MinMaxTO;
import br.danton.grawards.model.Producer;
import br.danton.grawards.dto.ProducerTO;
import br.danton.grawards.util.JpaUtil;
import java.util.List;
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
public class ProducerService {

	@EJB
	private ProducerDao producerDao;
	@EJB
	EntityManager entityManager;

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Producer find(Integer id) {
		return producerDao.find(id);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Producer> find(String name) {
		return producerDao.find(name);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Producer merge(Producer producer) {
		return JpaUtil.executeTransaction(entityManager, () -> producerDao.merge(producer));
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remove(Integer id) {
		JpaUtil.executeTransaction(entityManager, () -> producerDao.delete(id));
	}

	/**
	 * Buscando os produtores com o menor e o maior intervalo de anos entre dois prêmios consecutivos.
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public MinMaxTO<ProducerTO> findByMinMaxIntervalBetweenAwards() {
		List<ProducerTO> list = producerDao.findByMinMaxIntervalBetweenAwards();

		MinMaxTO<ProducerTO> minMaxTO = new MinMaxTO<>();
		int divisionIndex = getDivisionIndexBetweenMinAndMax(list);
		minMaxTO.setMin(divisionIndex == 0 ? list : list.subList(0, divisionIndex));
		minMaxTO.setMax(divisionIndex == 0 ? list : list.subList(divisionIndex, list.size()));
		return minMaxTO;
	}

	/**
	 * Retorna o index da divisão entre os produtores com o menor e o maior intervalo de anos entre dois prêmios consecutivos.
	 */
	private static int getDivisionIndexBetweenMinAndMax(List<ProducerTO> list) {
		int index = 0;
		int maxInterval = list.isEmpty() ? 0 : (int) list.get(list.size() - 1).getInterval();
		for (ProducerTO producer : list) {
			if (producer.getInterval() == maxInterval) {
				break;
			}
			index++;
		}
		return index;
	}
}
