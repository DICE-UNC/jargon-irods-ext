package org.irodsext.mdtemplate.services;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import org.irods.jargon.metadatatemplate.AbstractMetadataService;
import org.irods.jargon.metadatatemplate.MetadataTemplateException;
import org.irods.jargon.metadatatemplate.MetadataTemplateNotFoundException;
import org.irods.jargon.metadatatemplate.model.MDTemplate;
import org.irods.jargon.metadatatemplate.model.MDTemplateElement;
import org.irodsext.mdtemplate.TemplateService;
import org.irodsext.mdtemplate.dao.TemplateDao;
import org.irodsext.mdtemplate.entity.Template;
import org.irodsext.mdtemplate.entity.TemplateElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.threeten.bp.DateTimeUtils;



@Service("abstractMetadataService")
@Transactional
public class IrodsExtMetadataServiceImpl extends AbstractMetadataService {

	@Autowired
	private TemplateDao templateDao;
	

	@Override
	public List<MDTemplate> listPublicTemplates() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MDTemplate> listAllTemplates(String path) throws MetadataTemplateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteTemplateByName(String uniqueName) throws MetadataTemplateException {
		// TODO Auto-generated method stub
		return templateDao.deleteByName(uniqueName);
	}

	@Override
	public MDTemplate findTemplateByUUID(UUID uuid)
			throws MetadataTemplateNotFoundException, MetadataTemplateException {
		
		Template template = templateDao.findByGuid(uuid);
		MDTemplate mdTemplate = new MDTemplate();
		mdTemplate.setTemplateName(template.getTemplateName());
		mdTemplate.setGuid(template.getGuid().toString());
		
		return mdTemplate;
	}

	@Override
	public UUID saveTemplate(MDTemplate mdTemplate) throws MetadataTemplateException {

		Template template = new Template();
		template.setTemplateName(mdTemplate.getTemplateName());
		template.setDescription(mdTemplate.getDescription());
		template.setGuid(UUID.fromString(mdTemplate.getGuid()));
		template.setCreateTs(DateTimeUtils.toDate(mdTemplate.getCreateTs().toZonedDateTime().toInstant()));
		template.setModifyTs(DateTimeUtils.toDate(mdTemplate.getModifyTs().toZonedDateTime().toInstant()));
		template.setOwner(mdTemplate.getOwner());
		
		Set<TemplateElement> templateElement = new TreeSet<>();
		
		for (MDTemplateElement element : mdTemplate.getElements()){
			
			TemplateElement e = new TemplateElement();
			e.setName(element.getName());
			//e.setGuid(UUID.fromString(element.getGuid()));
			e.setGuid(UUID.randomUUID());
			e.setOptions(element.getOptions());
			e.setDefaultValue(element.getDefaultValue());
			e.setRequired(element.isRequired());
			e.setMINCardinality(element.getCardinalityMin());
			e.setMAXCardinality(element.getCardinalityMax());
			e.setType(element.getType());
			
			templateElement.add(e);
		}
		
		template.setElements(templateElement);
		Long id = (Long) templateDao.save(template);
		return template.getGuid();
	}

	@Override
	public MDTemplate findTemplateByName(String templateName)
			throws MetadataTemplateNotFoundException, MetadataTemplateException {
		
		Template template = templateDao.findByName(templateName);
		MDTemplate mdTemplate = new MDTemplate();
		mdTemplate.setTemplateName(template.getTemplateName());
		mdTemplate.setGuid(template.getGuid().toString());
		
		return mdTemplate;
	}

	@Override
	public boolean isTemplateExist(String templateName)
			throws MetadataTemplateNotFoundException, MetadataTemplateException {
		
		boolean result = false;
		Template template = templateDao.findByName(templateName);
				
		if(template != null) {
			result = true;
		}
		return result;
	}

	@Override
	public boolean deleteTemplateByGuid(UUID guid) throws MetadataTemplateException {
		// TODO Auto-generated method stub
		return templateDao.deleteByGuid(guid);
	}

}
