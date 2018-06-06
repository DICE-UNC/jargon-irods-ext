package org.irodsext.mdtemplate;

import java.util.List;

import org.irodsext.mdtemplate.dao.TemplateElementDao;
import org.irodsext.mdtemplate.entity.TemplateElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("TemplateElementService")
@Transactional
public class TemplateElementServiceImpl implements TemplateElementService {
	
	@Autowired TemplateElementDao templateElementDao;
	
	public TemplateElement findById(long id) {
		return templateElementDao.findById(id);
	}

	public long createTemplateElement(TemplateElement templateElement) {
		if(templateElement == null){
			return 0;
		}
		
		return (Long) templateElementDao.save(templateElement);
	}

	public List<TemplateElement> findAll() {
		// TODO Auto-generated method stub
		return templateElementDao.findAll(TemplateElement.class);
	}

	public boolean deleteTemplateElements(TemplateElement templateElement) {
		if(templateElement == null) {
			return false;
		}
		
		templateElementDao.delete(templateElement);
		return true;
	}

	public boolean modifyTemplateElement(long id, String attribute, String value, String unit) {
		return templateElementDao.modifyById(id, attribute, value, unit);
	}

	
	}
