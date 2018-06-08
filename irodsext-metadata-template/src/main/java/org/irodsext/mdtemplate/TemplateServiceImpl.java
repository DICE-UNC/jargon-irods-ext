package org.irodsext.mdtemplate;

import org.irodsext.mdtemplate.dao.TemplateDao;
import org.irodsext.mdtemplate.entity.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("TemplateService")
@Transactional
public class TemplateServiceImpl implements TemplateService {
	
	@Autowired
	private TemplateDao templateDao;
	
	public Long createTemplate(Template template) {	
		Long id = (Long) templateDao.save(template);
		System.out.println("Saved :: "+id);
		return id;
		
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
