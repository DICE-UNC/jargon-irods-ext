package org.irodsext.template.dao;

public interface GenericDao<T> {

	public void save(T entity);
	public void merge(T entity);
	public void delete(T entity);
}
