package org.irodsext.template.dao;

import java.util.List;

import org.irodsext.template.entity.Template;
import org.irodsext.template.entity.TemplateElement;

public interface TemplateDao extends GenericDao<Template> {

	public Template findByName(String templateName);
	public long getTemplateId(String templateName);
	public Template findById(long id);
	boolean deleteById(long id);
	List<TemplateElement> listTemplateElements(String template);
	List<TemplateElement> listTemplateElements(Long id);

}
