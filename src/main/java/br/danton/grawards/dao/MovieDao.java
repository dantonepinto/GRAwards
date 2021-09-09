package br.danton.grawards.dao;

import br.danton.grawards.model.Movie;
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
public class MovieDao extends AbstractDao<Movie, Integer> {

	@EJB
	EntityManager entityManager;

	public MovieDao() {
		super(Movie.class);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Movie> find(String title) {
		return entityManager.createNamedQuery("Movie.find").setParameter("title", title).getResultList();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void delete(Integer id) {
		entityManager.createNamedQuery("Movie.delete").setParameter("id", id).executeUpdate();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deleteAll() {
		entityManager.createNamedQuery("Movie.deleteAll").executeUpdate();
		entityManager.createNativeQuery("ALTER SEQUENCE seq_movie RESTART WITH 1").executeUpdate();
	}
}
