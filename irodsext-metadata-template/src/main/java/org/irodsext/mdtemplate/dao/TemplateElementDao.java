package org.irodsext.mdtemplate.dao;

import org.irodsext.mdtemplate.entity.TemplateElement;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateElementDao extends GenericDao<TemplateElement , Long>{

	TemplateElement findById(long id);
	boolean modifyById(long id, String attribute, String value, String unit);
}
