package org.irodsext.template;

import org.irodsext.template.entity.Template;

public interface TemplateService {

	 /**
     * Creates a template into the database
     *
     * @param template
     *            template to be saved into the database
     * @return The id of the template just created
     */
    public void createTemplate(Template template);
    public Template findByName(String templateName);
    public Template findById(long id);
    public boolean deleteTemplate(long id);
}
