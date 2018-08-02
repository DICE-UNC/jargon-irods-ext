package org.irodsext.mdtemplate.dao.impl;

import java.util.UUID;

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
		Query q = sessionFactory.getCurrentSession().createQuery("from TemplateElement where element_id=(:id)");
		q.setParameter("id", id);
		
		return (TemplateElement) q.uniqueResult();
	}

	public boolean modifyById(long id, String name, String value, String unit) {
		TemplateElement element = findById(id);

        if (element == null) {
            return false;
        }

        element.setName(name);
        element.setDefaultValue(value);
        element.setAttributeUnit(unit);
        merge(element);

        return true;
	}

	@Override
	public boolean deleteByGuid(UUID guid) {
		TemplateElement element = this.findByGuid(guid);
        if (element == null) {
            return false;
        }
       this.delete(element);
       return true;
	}

	@Override
	public TemplateElement findByGuid(UUID guid) {
		 Query q = this.sessionFactory.getCurrentSession().
				 createQuery("from TemplateElement where guid=:guid");
	        q.setParameter("guid", guid.toString());

	        return (TemplateElement) q.uniqueResult();

	}
	

}
