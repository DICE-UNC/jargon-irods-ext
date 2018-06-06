package org.irodsext.mdtemplate;

import java.util.List;

import org.irodsext.mdtemplate.entity.TemplateElement;

public interface TemplateElementService {

	public TemplateElement findById(long id);
	public long createTemplateElement(TemplateElement templateElement);
	public List<TemplateElement> findAll();
	public boolean deleteTemplateElements(TemplateElement templateElement);
	boolean modifyTemplateElement(long id, String attribute, String value, String unit);
}
