package org.irodsext.mdtemplate.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.irodsext.mdtemplate.dao.TemplateDao;
import org.irodsext.mdtemplate.entity.Template;
import org.irodsext.mdtemplate.entity.TemplateElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("TemplateEntityDao")
public class TemplateDaoImpl extends GenericDaoImpl<Template , Long> implements TemplateDao{
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public long getTemplateId(String templateName) {
		Template template = this.findByName(templateName);

        if (template == null) {
            return -1;
        }

        return template.getId();
	}
	
	@Override
	public Template findByName(String templateName) {
		  Query<Template> q = this.sessionFactory.getCurrentSession()
	                .createQuery("from Template where templateName = (:templateName)");
	      q.setParameter("templateName", templateName);	
		  return q.uniqueResult();
	}
	
	
	@Override
	public Template findById(long id) {
		 Query<Template> q = this.sessionFactory.getCurrentSession().createQuery("from Template where id=(:id)");
	        q.setParameter("id", id);

	        return q.uniqueResult();
	}
	
	@Override
	public List<TemplateElement> listTemplateElements(String template) {
		long id = this.getTemplateId(template);

        if (id > 0) {
            return this.listTemplateElements(id);
        }

        return new ArrayList<TemplateElement>();
	}
	
	@Override
	public List<TemplateElement> listTemplateElements(Long id) {
		 Query q = this.sessionFactory.getCurrentSession()
	                .createQuery("from TemplateElement where template_id = :templateID");

	        q.setParameter("templateID", id);

	        return q.list();
	}

	@Override
	public List<Template> findByQueryString(String query) {
		 Query q = this.sessionFactory.getCurrentSession()
	                .createQuery("from Template where templateName like :templateName");

	        q.setParameter("templateName", "%" + query + "%");

	        // Returning results
	        return q.list();
	}
	
	@Override
	public Template findByGuid(UUID guid) {
		 Query<Template> q = this.sessionFactory.getCurrentSession().createQuery("from Template where guid=:guid");
	        q.setParameter("guid", guid);

	        return q.uniqueResult();

	}

	@Override
	public boolean deleteByName(String uniqueName) {
		Template template = this.findByName(uniqueName);
        if (template == null) {
            return false;
        }
        this.delete(template);
        return true;
	}

	@Override
	public boolean deleteByGuid(UUID guid) {
		Template template = this.findByGuid(guid);
        if (template == null) {
            return false;
        }
       this.delete(template);
       return true;
	}

	@Override
	public boolean deleteById(long id) {
		Template template = this.findById(id);

        if (template == null) {
            return false;
        }

        this.delete(template);

        return true;
	}
}
