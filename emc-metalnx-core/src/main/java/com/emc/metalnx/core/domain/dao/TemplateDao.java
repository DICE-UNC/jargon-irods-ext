 /* Copyright (c) 2018, University of North Carolina at Chapel Hill */
 /* Copyright (c) 2015-2017, Dell EMC */
 

package com.emc.metalnx.core.domain.dao;

import com.emc.metalnx.core.domain.dao.generic.GenericDao;
import com.emc.metalnx.core.domain.entity.DataGridTemplate;
import com.emc.metalnx.core.domain.entity.DataGridTemplateField;

import java.util.List;

public interface TemplateDao extends GenericDao<DataGridTemplate, Long> {

    /**
     * Gets the template id based on its name (the template name given has to
     * match exactly the template name existing in the database).
     *
     * @param templateName
     *            name of the template
     * @return the template id if the template is found. Null, otherwise.
     */
    long getTemplateId(String templateName);

    /**
     * Find a template by its name
     *
     * @param templateName
     *            name of the template
     * @return Template matching the name
     */
    DataGridTemplate findByName(String templateName);

    /**
     * Find a template by its id
     *
     * @param id
     *            template id
     * @return Template if the id exists. Null, if the id was not found.
     */
    DataGridTemplate findById(long id);

    /**
     * Deletes a template by its id
     *
     * @param id
     *            id of the template to be removed
     * @return true if the template was successfully removed. False, otherwise.
     */
    boolean deleteById(long id);

    /**
     * Finds templates by a query string
     *
     * @param query
     *            string containing the search term to match template names
     * @return list of templates
     */
    List<DataGridTemplate> findByQueryString(String query);

    /**
     * Lists all fields existing in a template
     *
     * @param template
     *            name of the template
     * @return List of template fields, if any
     */
    List<DataGridTemplateField> listTemplateFields(String template);

    /**
     * Lists all fields existing in a template
     *
     * @param id
     *            id of the template
     * @return List of template fields, if any
     */
    List<DataGridTemplateField> listTemplateFields(Long id);

    /**
     * Returns all the public metadata templates
     *
     * @return List of template fields, if any
     */
    List<DataGridTemplate> listPublicTemplates();

    /**
     * Returns all the private metadata templates
     *
     * @return List of template fields, if any
     */
    List<DataGridTemplate> listPrivateTemplatesByUser(String user);

    @Override
    /**
     * Overrides the merge method in order to handle the version number of the
     * current template.
     */
    void merge(DataGridTemplate template);
}
