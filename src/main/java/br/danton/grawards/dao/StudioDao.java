package br.danton.grawards.dao;

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
public class StudioDao extends AbstractDao<Studio, Integer> {

	@EJB
	EntityManager entityManager;

	public StudioDao() {
		super(Studio.class);
	}

	/**
	 * Lista os produtores com o menor e o maior intervalo de anos entre dois prêmios consecutivos.
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<StudioTO> findByMinMaxIntervalBetweenAwards() {
		return entityManager.createNamedQuery("Studio.findByMinMaxIntervalBetweenAwards", StudioTO.class).getResultList();
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Studio> find(String name) {
		return entityManager.createNamedQuery("Studio.find").setParameter("name", name).getResultList();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void delete(Integer id) {
		entityManager.createNamedQuery("Studio.delete").setParameter("id", id).executeUpdate();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deleteAll() {
		entityManager.createNamedQuery("Studio.deleteAll").executeUpdate();
		entityManager.createNativeQuery("ALTER SEQUENCE seq_studio RESTART WITH 1").executeUpdate();
	}
}
