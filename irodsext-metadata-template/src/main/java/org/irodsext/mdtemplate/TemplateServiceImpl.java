package org.irodsext.template;

import org.irodsext.template.dao.TemplateDao;
import org.irodsext.template.entity.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("TemplateService")
@Transactional
public class TemplateServiceImpl implements TemplateService {
	
	@Autowired
	private TemplateDao templateDao;
	
	public void createTemplate(Template template) {	
		
		templateDao.save(template);
	}

	public Template findByName(String templateName) {
		
		return templateDao.findByName(templateName);
	}

	public Template findById(long id) {
		// TODO Auto-generated method stub
		return templateDao.findById(id);
	}

	

	public boolean deleteTemplate(long id) {
		
		//delete all the elements for this template beforethe actual template.
		/*List<TemplateElementEntity> templateFields = this.listTemplateFields(id);
        for (TemplateElementEntity templateField : templateFields) {
            templateFieldDao.delete(templateField);
        }*/

        return templateDao.deleteById(id);
	}

	
	

}
