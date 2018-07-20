package org.irodsext.mdtemplate.dao;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("rawtypes")
public interface GenericDao<T, guid extends Serializable> {

	public Serializable save(T entity);
	public void merge(T entity);
	public void delete(T entity);
	public List<T> findAll(Class clazz);
}
