package org.irodsext.mdtemplate.dao;

import java.util.UUID;

import org.irodsext.mdtemplate.entity.Template;
import org.irodsext.mdtemplate.entity.TemplateElement;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateElementDao extends GenericDao<TemplateElement , Long>{

	TemplateElement findById(long id);
	boolean modifyById(long id, String attribute, String value, String unit);
	boolean deleteByGuid(UUID guid);
	public TemplateElement findByGuid(UUID guid);
}
