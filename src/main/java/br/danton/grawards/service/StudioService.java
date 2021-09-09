package br.danton.grawards.service;

import br.danton.grawards.dao.StudioDao;
import br.danton.grawards.dto.MinMaxTO;
import br.danton.grawards.model.Studio;
import br.danton.grawards.dto.StudioTO;
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
public class StudioService {

	@EJB
	private StudioDao studioDao;
	@EJB
	EntityManager entityManager;

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Studio find(Integer id) {
		return studioDao.find(id);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Studio> find(String name) {
		return studioDao.find(name);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Studio merge(Studio studio) {
		return JpaUtil.executeTransaction(entityManager, () -> studioDao.merge(studio));
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remove(Integer id) {
		JpaUtil.executeTransaction(entityManager, () -> studioDao.delete(id));
	}

	/**
	 * Buscando os produtores com o menor e o maior intervalo de anos entre dois prêmios consecutivos.
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public MinMaxTO<StudioTO> findByMinMaxIntervalBetweenAwards() {
		List<StudioTO> list = studioDao.findByMinMaxIntervalBetweenAwards();

		MinMaxTO<StudioTO> minMaxTO = new MinMaxTO<>();
		int divisionIndex = getDivisionIndexBetweenMinAndMax(list);
		minMaxTO.setMin(divisionIndex == 0 ? list : list.subList(0, divisionIndex));
		minMaxTO.setMax(divisionIndex == 0 ? list : list.subList(divisionIndex, list.size()));
		return minMaxTO;
	}

	/**
	 * Retorna o index da divisão entre os produtores com o menor e o maior intervalo de anos entre dois prêmios consecutivos.
	 */
	private static int getDivisionIndexBetweenMinAndMax(List<StudioTO> list) {
		int index = 0;
		int maxInterval = list.isEmpty() ? 0 : (int) list.get(list.size() - 1).getInterval();
		for (StudioTO studio : list) {
			if (studio.getInterval() == maxInterval) {
				break;
			}
			index++;
		}
		return index;
	}
}
