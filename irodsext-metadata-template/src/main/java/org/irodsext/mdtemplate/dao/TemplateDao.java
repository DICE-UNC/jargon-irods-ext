package org.irodsext.mdtemplate.dao;

import java.util.List;

import org.irodsext.mdtemplate.entity.Template;
import org.irodsext.mdtemplate.entity.TemplateElement;

public interface TemplateDao extends GenericDao<Template , Long> {

	public Template findByName(String templateName);
	public long getTemplateId(String templateName);
	public Template findById(long id);
	boolean deleteById(long id);
	List<TemplateElement> listTemplateElements(String template);
	List<TemplateElement> listTemplateElements(Long id);

}
