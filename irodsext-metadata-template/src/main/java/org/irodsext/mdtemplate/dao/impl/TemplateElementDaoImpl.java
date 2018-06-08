package org.irodsext.mdtemplate.dao.impl;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.irodsext.mdtemplate.dao.TemplateElementDao;
import org.irodsext.mdtemplate.entity.TemplateElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("TemplateElementDao")
public class TemplateElementDaoImpl extends GenericDaoImpl<TemplateElement , Long> implements TemplateElementDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	public TemplateElement findById(long id) {
		Query<TemplateElement> q = sessionFactory.getCurrentSession().createQuery("from TemplateElement where element_id=(:id)");
		q.setParameter("id", id);
		
		return (TemplateElement)q.uniqueResult();
	}

	public boolean modifyById(long id, String attribute, String value, String unit) {
		TemplateElement element = findById(id);

        if (element == null) {
            return false;
        }

        element.setAttribute(attribute);
        element.setAttributeValue(value);
        element.setAttributeUnit(unit);
        merge(element);

        return true;
	}
	

}
