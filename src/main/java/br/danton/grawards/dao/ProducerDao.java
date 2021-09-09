package br.danton.grawards.dao;

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
public class ProducerDao extends AbstractDao<Producer, Integer> {

	@EJB
	EntityManager entityManager;

	public ProducerDao() {
		super(Producer.class);
	}

	/**
	 * Lista os produtores com o menor e o maior intervalo de anos entre dois prêmios consecutivos.
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<ProducerTO> findByMinMaxIntervalBetweenAwards() {
		return entityManager.createNamedQuery("Producer.findByMinMaxIntervalBetweenAwards", ProducerTO.class).getResultList();
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Producer> find(String name) {
		return entityManager.createNamedQuery("Producer.find").setParameter("name", name).getResultList();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void delete(Integer id) {
		entityManager.createNamedQuery("Producer.delete").setParameter("id", id).executeUpdate();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deleteAll() {
		entityManager.createNamedQuery("Producer.deleteAll").executeUpdate();
		entityManager.createNativeQuery("ALTER SEQUENCE seq_producer RESTART WITH 1").executeUpdate();
	}
}
