package org.irodsext.template.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.irodsext.template.dao.GenericDao;
import org.springframework.beans.factory.annotation.Autowired;

public class GenericDaoImpl<T> implements GenericDao<T>{

	@Autowired
	private SessionFactory sessionFactory;

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	

	public void save(T entity) {
		Session hibernateSession = this.getSession();
		System.out.println("Hiberate session :: " +hibernateSession);
		hibernateSession.save(entity);
	}
	
	public void merge(T entity) {
        Session hibernateSession = this.getSession();
        hibernateSession.merge(entity);
    }
 
    public void delete(T entity) {
        Session hibernateSession = this.getSession();
        hibernateSession.delete(entity);
    }


}
