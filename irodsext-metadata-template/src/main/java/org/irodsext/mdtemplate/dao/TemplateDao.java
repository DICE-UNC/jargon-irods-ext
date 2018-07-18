package org.irodsext.mdtemplate.dao;

import java.util.List;
import java.util.UUID;

import org.irodsext.mdtemplate.entity.Template;
import org.irodsext.mdtemplate.entity.TemplateElement;

public interface TemplateDao extends GenericDao<Template , Long> {

	public Template findByName(String templateName);
	public long getTemplateId(String templateName);
	public Template findById(long id);
	public Template findByGuid(UUID guid);
	boolean deleteById(long id);
	public List<TemplateElement> listTemplateElements(String template);
	public List<TemplateElement> listTemplateElements(Long id);
	public List<Template> findByQueryString(String query);
	

}
