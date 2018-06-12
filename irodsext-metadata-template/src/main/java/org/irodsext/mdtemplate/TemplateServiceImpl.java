package org.irodsext.mdtemplate;

import java.util.Collections;
import java.util.List;

import org.irodsext.mdtemplate.dao.TemplateDao;
import org.irodsext.mdtemplate.dao.TemplateElementDao;
import org.irodsext.mdtemplate.entity.Template;
import org.irodsext.mdtemplate.entity.TemplateElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("TemplateService")
@Transactional
public class TemplateServiceImpl implements TemplateService {

	@Autowired
	private TemplateDao templateDao;

	@Autowired
	private TemplateElementDao templateElementDao;
	
	
	@Override
	public Long createTemplate(Template template) {	
		Long id = (Long) templateDao.save(template);
		System.out.println("Saved :: "+id);
		return id;

	}

	@Override
	public Template findByName(String templateName) {		
		return templateDao.findByName(templateName);
	}	

	@Override
	public Template findById(long id) {
		// TODO Auto-generated method stub
		return templateDao.findById(id);
	}

	@Override
	public boolean deleteTemplate(long id) {
		List<TemplateElement> templateElements = this.listTemplateFields(id);
		for (TemplateElement templateField : templateElements) {
			templateElementDao.delete(templateField);
		}

		return templateDao.deleteById(id);
	}

	@Override
	public void merge(Template template) {	    
		templateDao.merge(template);
	}

	@Override
	public List<TemplateElement> listTemplateFields(Long id) {
		List<TemplateElement> templateElements = templateDao.listTemplateElements(id);
		//Collections.sort(templateElements);
        return templateElements;
	}

}
