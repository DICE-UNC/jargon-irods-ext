package org.irodsext.mdtemplate.services;
import java.util.ArrayList;
import java.util.Date;
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
import org.irodsext.mdtemplate.dao.TemplateElementDao;
import org.irodsext.mdtemplate.entity.Template;
import org.irodsext.mdtemplate.entity.TemplateElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.LocalDate;
import org.threeten.bp.OffsetDateTime;



@Service("abstractMetadataService")
@Transactional
public class IrodsExtMetadataServiceImpl extends AbstractMetadataService {

	@Autowired
	private TemplateDao templateDao;
	
	@Autowired
	private TemplateElementDao elementDao;
	

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
	public UUID saveTemplate(MDTemplate mdTemplate) throws MetadataTemplateException {

		Template template = getTemplateEntityFromJson(mdTemplate);
		Long id = (Long) templateDao.save(template);
		return template.getGuid();
	}

	@Override
	public MDTemplate findTemplateByName(String templateName)
			throws MetadataTemplateNotFoundException, MetadataTemplateException {
		
		Template template = templateDao.findByName(templateName);
		MDTemplate mdTemplate = getTemplateJsonFromEntity(template);
		
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

	@Override
	public MDTemplate findTemplateByGuid(UUID guid)
			throws MetadataTemplateNotFoundException, MetadataTemplateException {
		Template template = templateDao.findByGuid(guid);
		MDTemplate mdTemplate = getTemplateJsonFromEntity(template);
		
		return mdTemplate;
	}

	
	@Override
	public UUID updateTemplate(MDTemplate mdTemplate) throws MetadataTemplateException {
		
		Template template = new Template();
		template.setTemplateName(mdTemplate.getTemplateName());
		template.setDescription(mdTemplate.getDescription());
		template.setGuid(UUID.fromString(mdTemplate.getGuid()));
		template.setCreateTs(DateTimeUtils.toDate(mdTemplate.getCreateTs().toZonedDateTime().toInstant()));
		template.setModifyTs(DateTimeUtils.toDate(mdTemplate.getModifyTs().toZonedDateTime().toInstant()));
		template.setOwner(mdTemplate.getOwner());
		
		templateDao.merge(template);
		return template.getGuid();
	}

	@Override
	public UUID saveElement(UUID templateGuid, MDTemplateElement mdElement) throws MetadataTemplateException {
		// TODO Auto-generated method stub
		
		System.out.println("Savinf element in services");
		MDTemplate mdTemplate = findTemplateByGuid(templateGuid);
		UUID guid = null;
		if(mdTemplate == null) {
			//discuss this logic when parent does not exists
			return guid;
		}else {
			TemplateElement element = new TemplateElement();			
			element = getElementEntityFromJson(mdElement);	
			element.setTemplate(getTemplateEntityFromJson(mdTemplate));
			elementDao.save(element);
			guid = element.getGuid();
			
		}
	
		return guid;
	}

	@Override
	public UUID updateElement(UUID templateGuid, MDTemplateElement metadataTemplate) throws MetadataTemplateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteElementByGuid(UUID templateGuid, UUID elementGuid) throws MetadataTemplateException {
		MDTemplate template = findTemplateByGuid(templateGuid);
		boolean isDeleted = false;
		if(template == null) {
			//discuss this logic when parent does not exists.
			//do we need to pass template guid here. since element to template has no hard dependency.
			return isDeleted;
		}else {
			isDeleted = elementDao.deleteByGuid(elementGuid);
		}
		return isDeleted;
	}

	@Override
	public MDTemplateElement findElementByGuid(UUID templateGuid, UUID elementGuid)
			throws MetadataTemplateNotFoundException, MetadataTemplateException {
		
		MDTemplate template = findTemplateByGuid(templateGuid);	
		if(template == null) {
			//discuss this logic when parent does not exists.
			//do we need to pass template guid here. since element to template has no hard dependency.
			return null;
		}else {
			TemplateElement element = elementDao.findByGuid(elementGuid);
			MDTemplateElement mdElement = getElementJsonFromEntity(element);	
			return mdElement;
		}
		
	}
	
	public TemplateElement getElementEntityFromJson(MDTemplateElement mdElement) {
		TemplateElement templateElement = new TemplateElement();
		templateElement.setDefaultValue(mdElement.getDefaultValue());
		templateElement.setAttributeUnit(mdElement.getUnit());
		templateElement.setName(mdElement.getName());
		templateElement.setGuid(UUID.fromString(mdElement.getGuid()));
		templateElement.setOptions(mdElement.getOptions());
		templateElement.setRequired(mdElement.isRequired());
		templateElement.setMAXCardinality(mdElement.getCardinalityMax());
		templateElement.setMINCardinality(mdElement.getCardinalityMin());
		templateElement.setType(mdElement.getType());
		templateElement.setValidation_exp(mdElement.getValidationExp());
		return templateElement;
	}
	
	public MDTemplateElement getElementJsonFromEntity(TemplateElement element) {
		MDTemplateElement mdElement = new MDTemplateElement();
		mdElement.setDefaultValue(element.getDefaultValue());
		mdElement.setUnit(element.getAttributeUnit());
		mdElement.setName(element.getName());
		mdElement.setGuid(element.getGuid().toString());
		mdElement.setOptions(element.getOptions());
		mdElement.setRequired(element.isRequired());
		mdElement.setCardinalityMax(element.getMAXCardinality());
		mdElement.setCardinalityMin(element.getMINCardinality());
		mdElement.setType(element.getType());
		mdElement.setValidationExp(element.getValidation_exp());
		return mdElement;
	}
	
	public Template getTemplateEntityFromJson(MDTemplate mdTemplate) {			
		Template template = new Template();
		template.setTemplateName(mdTemplate.getTemplateName());
		template.setDescription(mdTemplate.getDescription());
		template.setGuid(UUID.fromString(mdTemplate.getGuid()));
		template.setCreateTs(DateTimeUtils.toDate(mdTemplate.getCreateTs().toZonedDateTime().toInstant()));
		template.setModifyTs(DateTimeUtils.toDate(mdTemplate.getModifyTs().toZonedDateTime().toInstant()));
		template.setOwner(mdTemplate.getOwner());
		template.setAccessType(mdTemplate.getAccessType());
		
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
			e.setValidation_exp(element.getValidationExp());
			e.setTemplate(template);
					
			Set<TemplateElement> childElements = new TreeSet<>();
			for (MDTemplateElement subElements : element.getElements()) {
				
				TemplateElement childElement = new TemplateElement();
				childElement.setName(subElements.getName());
				//e.setGuid(UUID.fromString(element.getGuid()));
				childElement.setGuid(UUID.randomUUID());
				childElement.setOptions(subElements.getOptions());
				childElement.setDefaultValue(subElements.getDefaultValue());
				childElement.setRequired(subElements.isRequired());
				childElement.setMINCardinality(subElements.getCardinalityMin());
				childElement.setMAXCardinality(subElements.getCardinalityMax());
				childElement.setType(subElements.getType());
				childElement.setValidation_exp(subElements.getValidationExp());
				childElement.setTemplate(template);
				childElement.setTemplateElement(e);
				
				childElements.add(childElement);
				
			}
			e.setElements(childElements);
			templateElement.add(e);
		}
		
		template.setElements(templateElement);
		
		return template;		
	}
	
	public MDTemplate getTemplateJsonFromEntity(Template template) {
		MDTemplate mdTemplate = new MDTemplate();
		
		mdTemplate.setId(template.getId());
		mdTemplate.setTemplateName(template.getTemplateName());
		mdTemplate.setGuid(template.getGuid().toString());		
		//**************THis dates needs to be changed*****************
		mdTemplate.setCreateTs(OffsetDateTime.now());
		mdTemplate.setModifyTs(OffsetDateTime.now());
		//*************************************************************
		mdTemplate.setAccessType(template.getAccessType());
		mdTemplate.setDescription(template.getDescription());
		mdTemplate.setOwner(template.getOwner());
				
		List<MDTemplateElement> elementsList = new ArrayList<>();		
		for(TemplateElement element : template.getElements()) {
			MDTemplateElement mdElement = new MDTemplateElement();
			//mdElement.setAccessType(element.get);
			mdElement.setCardinalityMax(element.getMAXCardinality());
			mdElement.setCardinalityMin(element.getMINCardinality());
			mdElement.setDefaultValue(element.getDefaultValue());
			mdElement.setGuid(element.getGuid().toString());
			mdElement.setName(element.getName());
			mdElement.setOptions(element.getOptions());
			mdElement.setRequired(element.isRequired());
			mdElement.setType(element.getType());
			mdElement.setUnit(element.getAttributeUnit());
			mdElement.setValidationExp(element.getValidation_exp());
			
			elementsList.add(mdElement);
		}
		
		mdTemplate.setElements(elementsList);
		return mdTemplate;
	}

	
}
