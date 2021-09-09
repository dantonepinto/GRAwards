package br.danton.grawards.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import javax.ejb.EJB;
import javax.persistence.EntityManager;

/**
 * @author Danton Estevam Pinto <dantonepinto@gmail.com>
 */
public class SimuladorEJB {

	private final HashMap<Class, Object> instances = new HashMap<>();

	/**
	 * Retorna uma instância da classe injetando todos os field anotados com @EJB.
	 */
	public <T> T get(Class<T> classe) {
		T instance = (T) instances.get(classe);
		if (instance == null) {
			try {
				if (classe == EntityManager.class) {
					instance = (T) JpaUtil.createEntityManager();
				} else {
					instance = classe.getConstructor().newInstance();
					setAllFields(classe, instance);
				}
			} catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException ex) {
				throw new RuntimeException(ex);
			}
			instances.put(classe, instance);
		}
		return instance;
	}

	private void setAllFields(Class classe, Object instance) throws IllegalArgumentException, IllegalAccessException {
		do {
			for (Field field : classe.getDeclaredFields()) {
				if (field.getAnnotation(EJB.class) != null) {
					boolean accessible = field.canAccess(instance);
					try {
						if (!accessible) {
							field.setAccessible(true);
						}

						field.set(instance, get(field.getType()));
					} finally {
						if (!accessible) {
							field.setAccessible(accessible);
						}
					}
				}
			}
		} while ((classe = classe.getSuperclass()) != null);
	}
}
