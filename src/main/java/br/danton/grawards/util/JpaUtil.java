package br.danton.grawards.util;

import java.util.concurrent.Callable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 *
 * @author Danton Estevam Pinto <dantonepinto@gmail.com>
 */
public class JpaUtil {

	private static final String PERSISTENT_UNIT_NAME = "PU";
	private static EntityManagerFactory entityManagerFactory;

	private static synchronized EntityManagerFactory getEntityManagerFactory() {
		if (entityManagerFactory == null) {
			entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENT_UNIT_NAME);
		}
		return entityManagerFactory;
	}

	public static EntityManager createEntityManager() {
		return getEntityManagerFactory().createEntityManager();
	}

	/**
	 * Necessário pois a aplicação não está sendo executada em um containner EJB e, portanto, a anotação @TransactionAttribute(TransactionAttributeType.REQUIRED) não tem efeito.
	 */
	public static void executeTransaction(EntityManager entityManager, Runnable runnable) {
		executeTransaction(entityManager, () -> {
			runnable.run();
			return null;
		});
	}

	/**
	 * Necessário pois a aplicação não está sendo executada em um containner EJB e, portanto, a anotação @TransactionAttribute(TransactionAttributeType.REQUIRED) não tem efeito.
	 */
	public static <T> T executeTransaction(EntityManager entityManager, Callable<T> callable) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		T result;
		try {
			result = callable.call();
		} catch (Exception ex) {
			try {
				transaction.rollback();
			} catch (Throwable ex2) {
			}
			throw ex;
		}
		transaction.commit();
		return result;
	}

	public static interface Callable<T> {

		public T call();
	}
}
