package org.irodsext.mdtemplate.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.irodsext.mdtemplate.dao.GenericDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@SuppressWarnings({ "rawtypes", "unchecked" })
public class GenericDaoImpl<T, guid extends Serializable> implements GenericDao<T, guid>{

	@Autowired
	private SessionFactory sessionFactory;

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	public guid save(T entity) {
		Session hibernateSession = this.getSession();
		return (guid) hibernateSession.save(entity);
	}
	
	public void merge(T entity) {
        Session hibernateSession = this.getSession();
        hibernateSession.merge(entity);
    }
 
    public void delete(T entity) {
        Session hibernateSession = this.getSession();
        hibernateSession.delete(entity);
    }


	public List<T> findAll(Class clazz) {
		Session hibernateSession = this.getSession();
        List<T> T = null;
        Query query = hibernateSession.createQuery("from " + clazz.getName());
        T = query.list();
        return T;
	}


}
