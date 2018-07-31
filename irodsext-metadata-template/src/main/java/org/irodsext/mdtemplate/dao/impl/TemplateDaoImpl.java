package org.irodsext.mdtemplate.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.lang.model.element.Element;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
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
		  Query q = this.sessionFactory.getCurrentSession()
	                .createQuery("from Template where templateName = (:templateName)");
	      q.setParameter("templateName", templateName);	
		  return (Template) q.uniqueResult();
	}
	
	
	@Override
	public Template findById(long id) {
		 Query q = this.sessionFactory.getCurrentSession().createQuery("from Template where id=(:id)");
	        q.setParameter("id", id);

	        return (Template) q.uniqueResult();
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
		 Query q = this.sessionFactory.getCurrentSession().createQuery("from Template where guid=:guid and element.parent");

		 
		 Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Template.class,"template")
				 .createAlias("element", "element")
	                .add(Restrictions.eq("template.guid", guid));
	               // .add(Restrictions.isNull("element.parent_id"));

		 
			/*Criteria criteria = session.createCriteria(YourClass.class);
			 YourObject yourObject = criteria.add(Restrictions.eq("yourField", yourFieldValue))
			                              .uniqueResult(); 
			 no if you get the template in hibernate it will find the elements 
			 you just need to get the template in the hib session per above 
			  */
			
			  

		 
		/* select * from templates_poc template
		 join template_elements_poc elmt on elmt.template_id = template.template_id
		 where elmt.parent_id is null*/
		 
		
	        return (Template) criteria.uniqueResult();     
	      // q.setParameter("guid", guid);

	       // return q.uniqueResult();

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
