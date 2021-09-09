package br.danton.grawards.dao;

import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;

/**
 *
 * @author Danton Estevam Pinto <dantonepinto@gmail.com>
 * @param <E> Classe da entidade
 * @param <K> Tipo da chave primária
 */
public abstract class AbstractDao<E, K> {

	@EJB
	private EntityManager entityManager;

	private final Class<E> entityClass;

	public AbstractDao(Class<E> entityClass) {
		this.entityClass = entityClass;
	}

	/**
	 * Procura a entidade pela chave primária.
	 *
	 * @param primaryKey
	 * @return entity
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public E find(K primaryKey) {
		return entityManager.find(entityClass, primaryKey);
	}

	/**
	 * Persiste a entidade.
	 *
	 * @param entity
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void persist(E entity) {
		entityManager.persist(entity);
	}

	/**
	 * Mescla a entidade.
	 *
	 * @param entity
	 * @return entity
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public E merge(E entity) {
		return entityManager.merge(entity);
	}

	/**
	 * Deleta todos os registros da tabela.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deleteAll() {
		throw new UnsupportedOperationException();
	}
}
