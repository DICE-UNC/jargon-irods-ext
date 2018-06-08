package org.irodsext.mdtemplate;

import org.irodsext.mdtemplate.entity.Template;

public interface TemplateService {

	 /**
     * Creates a template into the database
     *
     * @param template
     *            template to be saved into the database
     * @return The id of the template just created
     */
    public Long createTemplate(Template template);
    public Template findByName(String templateName);
    public Template findById(long id);
    public boolean deleteTemplate(long id);
}
