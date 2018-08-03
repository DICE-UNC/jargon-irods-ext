package org.irodsext.mdtemplate.services;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import org.apache.catalina.valves.rewrite.Substitution.SubstitutionElement;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.LocalDate;
import org.threeten.bp.OffsetDateTime;



@Service("abstractMetadataService")
@Transactional
public class IrodsExtMetadataServiceImpl extends AbstractMetadataService {

	private static final Logger logger = LoggerFactory.getLogger(IrodsExtMetadataServiceImpl.class);
	
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
	public MDTemplate saveTemplate(MDTemplate mdTemplate) throws MetadataTemplateException {
		logger.info("saveTemplate () starts");
		Template template = getTemplateEntityFromJson(mdTemplate);
		Set<TemplateElement> element = template.getElements();
		for(TemplateElement e : element) {
			logger.info("element :: " +e);
			
			Set<TemplateElement> subelement = e.getElements();
			for(TemplateElement subElm : subelement) {
				logger.info("sub elments :: " +subElm);
			}
		}
		Long id = (Long) templateDao.save(template);
		logger.info("saveTemplate () Ends");
		return mdTemplate;
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
		
		logger.info("findTemplateByGuid () starts :: " +guid);
		Template template = templateDao.findByGuid(guid);
		MDTemplate mdTemplate = getTemplateJsonFromEntity(template);
		logger.info("findTemplateByGuid () Ends :: ");
		return mdTemplate;
	}


	@Override
	public MDTemplate updateTemplate(MDTemplate mdTemplate) throws MetadataTemplateException {
		logger.info("updateTemplate() starts :: " +mdTemplate.getGuid());
		Template templateByGuid = templateDao.findByGuid(UUID.fromString(mdTemplate.getGuid()));
		if(templateByGuid == null) {
			logger.info("template does not exist");
			throw new MetadataTemplateException("Template your trying to update does not exists");
		}else {
			Template template = getTemplateEntityFromJson(mdTemplate);
			logger.info("Updating template for id :: " +templateByGuid.getId());
			template.setId(templateByGuid.getId());
			templateDao.merge(template);
		}
		logger.info("updateTemplate() Ends.");
		return mdTemplate;
	}

	@Override
	public MDTemplateElement saveElement(UUID templateGuid, MDTemplateElement mdElement) throws MetadataTemplateException {
		// TODO Auto-generated method stub

		logger.info("saveElement() starts :: " +templateGuid);
		MDTemplate mdTemplate = findTemplateByGuid(templateGuid);
		Template template = templateDao.findByGuid(templateGuid);
		if(mdTemplate == null) {
			logger.info("Template does not exist :: " +templateGuid);
			throw new MetadataTemplateException("Template for this element does not exists");
		}else {
			TemplateElement element = new TemplateElement();			
			element = getElementEntityFromJson(mdElement,template);	
			
			logger.info("Element :: " +element);
			elementDao.save(element);

		}
		logger.info("saveElement() Ends.");
		return mdElement;
	}

	@Override
	public MDTemplateElement updateElement(UUID templateGuid, MDTemplateElement mdElement) throws MetadataTemplateException {
		logger.info("saveElement() starts :: " +templateGuid);
		Template template = templateDao.findByGuid(templateGuid);
		
		if(template == null) {
			logger.info("Template does not exist :: " +templateGuid);
			throw new MetadataTemplateException("Template for this Element does not exists");
		}else {
			TemplateElement element = getElementEntityFromJson(mdElement,template);		
			TemplateElement elementByGuid = elementDao.findByGuid(UUID.fromString(mdElement.getGuid()));
			if(elementByGuid == null) {
				logger.info("element does not exist :: " +mdElement.getGuid());
				throw new MetadataTemplateException("This Element does not exists");
			}else {
				element.setId(elementByGuid.getId());
				logger.info("Updating element for id :: " +elementByGuid.getId());
				logger.info("Element :: " +element);
				elementDao.merge(element);
			}			
		}
		System.out.println("saveElement() Ends");
		return mdElement;
	}

	@Override
	public boolean deleteElementByGuid(UUID templateGuid, UUID elementGuid) throws MetadataTemplateException {
		
		logger.info("deleteElementByGuid starts() :: " +templateGuid+ "element guid :: " +elementGuid);
		//why do we need template id here??
		return elementDao.deleteByGuid(elementGuid);
	}

	@Override
	public MDTemplateElement findElementByGuid(UUID templateGuid, UUID elementGuid)
			throws MetadataTemplateNotFoundException, MetadataTemplateException {

		MDTemplate template = findTemplateByGuid(templateGuid);	
		if(template == null) {
			//discuss this logic when parent does not exists.
			//do we need to pass template guid here. since element to template has no hard dependency.
			logger.info("Template does not exist :: " +templateGuid);
			throw new MetadataTemplateException("Template for this element does not exists");
		}else {
			TemplateElement element = elementDao.findByGuid(elementGuid);
			MDTemplateElement mdElement = getElementJsonFromEntity(element);	
			return mdElement;
		}

	}

	public TemplateElement getElementEntityFromJson(MDTemplateElement mdElement, Template template) {
	
		logger.info("Element for Template :: " +template.getTemplateName()+ " , id :: " +template.getId() );
		
		logger.info("Element :: " + mdElement.getName());
		TemplateElement templateElement = new TemplateElement();
		templateElement.setDefaultValue(mdElement.getDefaultValue());
		templateElement.setAttributeUnit(mdElement.getUnit());
		templateElement.setName(mdElement.getName());
		templateElement.setGuid(mdElement.getGuid());
		templateElement.setOptions(mdElement.getOptions());
		templateElement.setRequired(mdElement.isRequired());
		templateElement.setMAXCardinality(mdElement.getCardinalityMax());
		templateElement.setMINCardinality(mdElement.getCardinalityMin());
		templateElement.setType(mdElement.getType());
		templateElement.setValidationExp(mdElement.getValidationExp());
		templateElement.setTemplate(template);
		
		
		Set<TemplateElement> childElementsSet = new TreeSet<>();
		for (MDTemplateElement ce : mdElement.getElements()) {
			logger.info("Sub Element :: " +ce.getName());
			TemplateElement childElement = new TemplateElement();
			childElement.setName(ce.getName());										
			//e.setGuid(UUID.fromString(element.getGuid()));
			String guid = ce.getGuid() !=null ? ce.getGuid() : UUID.randomUUID().toString();
			childElement.setGuid(guid);
			childElement.setOptions(ce.getOptions());
			childElement.setDefaultValue(ce.getDefaultValue());				
			childElement.setRequired(ce.isRequired());					
			childElement.setMINCardinality(ce.getCardinalityMin());							
			childElement.setMAXCardinality(ce.getCardinalityMax());							
			childElement.setType(ce.getType());							
			childElement.setValidationExp(ce.getValidationExp());	
			//logger.info("Adding the child template to metadata :: " +template.getTemplateName()+" , and id :: " +template.getId());
			childElement.setTemplate(template);						
			//logger.info("Adding the child meatdata to metadata :: " +childElement.getName());
			childElement.setTemplateElement(templateElement);							
			childElementsSet.add(childElement);
		}
		
		//templateElement.setTemplateElement(templateElement);
		templateElement.setElements(childElementsSet);
		
		//templateElementSet.add(element);
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
		mdElement.setValidationExp(element.getValidationExp());
		
		List<MDTemplateElement> childElementsList = new ArrayList<>();
		for (TemplateElement ce : element.getElements()) {
			MDTemplateElement mdChildElement = new MDTemplateElement();
			mdChildElement.setName(ce.getName());										
			//e.setGuid(UUID.fromString(element.getGuid()));
			mdChildElement.setGuid(ce.getGuid().toString());
			mdChildElement.setOptions(ce.getOptions());
			mdChildElement.setDefaultValue(ce.getDefaultValue());				
			mdChildElement.setRequired(ce.isRequired());					
			mdChildElement.setCardinalityMin(ce.getMINCardinality());							
			mdChildElement.setCardinalityMax(ce.getMAXCardinality());							
			mdChildElement.setType(ce.getType());							
			mdChildElement.setValidationExp(ce.getValidationExp());										
			//mdChildElement.setTemplate(template);								
			childElementsList.add(mdChildElement);
		}		
		mdElement.setElements(childElementsList);		
		return mdElement;
	}

	public Template getTemplateEntityFromJson(MDTemplate mdTemplate) {			
		Template template = new Template();
		logger.info("Template :: " +mdTemplate.getTemplateName());
		template.setTemplateName(mdTemplate.getTemplateName());
		template.setDescription(mdTemplate.getDescription());
		template.setGuid(mdTemplate.getGuid());
		template.setCreateTs(DateTimeUtils.toDate(mdTemplate.getCreateTs().toZonedDateTime().toInstant()));
		template.setModifyTs(DateTimeUtils.toDate(mdTemplate.getModifyTs().toZonedDateTime().toInstant()));
		template.setOwner(mdTemplate.getOwner());
		template.setAccessType(mdTemplate.getAccessType());
		
		
		if(mdTemplate.getElements()!=null && !mdTemplate.getElements().isEmpty()) {
			Set<TemplateElement> templateElementSet = new TreeSet<>();
			for (MDTemplateElement e : mdTemplate.getElements()){		
				logger.info("Element :: " +e.getName());
				TemplateElement element = new TemplateElement();
				element.setName(e.getName());
				String guid = e.getGuid().isEmpty() ?UUID.randomUUID().toString() : e.getGuid();
				logger.info("Element Guid is :: " +guid);
				element.setGuid(guid);
				element.setOptions(e.getOptions());
				element.setDefaultValue(e.getDefaultValue());
				element.setRequired(e.isRequired());
				element.setMINCardinality(e.getCardinalityMin());
				element.setMAXCardinality(e.getCardinalityMax());
				element.setType(e.getType());
				element.setValidationExp(e.getValidationExp());
				element.setTemplate(template);

				Set<TemplateElement> childElementsSet = new TreeSet<>();
				if(e.getElements() != null && !e.getElements().isEmpty()) {
					for (MDTemplateElement ce : e.getElements()) {
						logger.info("Sub element :: " +ce.getName());
						TemplateElement childElement = new TemplateElement();
						childElement.setName(ce.getName());		
						String subGuid = ce.getGuid().isEmpty() ?UUID.randomUUID().toString() : ce.getGuid();
						logger.info("sub element Guid is :: " +subGuid);
						childElement.setGuid(subGuid);
						childElement.setOptions(ce.getOptions());
						childElement.setDefaultValue(ce.getDefaultValue());				
						childElement.setRequired(ce.isRequired());					
						childElement.setMINCardinality(ce.getCardinalityMin());							
						childElement.setMAXCardinality(ce.getCardinalityMax());							
						childElement.setType(ce.getType());							
						childElement.setValidationExp(ce.getValidationExp());							
						childElement.setTemplate(template);					
						childElement.setTemplateElement(element);							
						childElementsSet.add(childElement);
					}
				}
				element.setElements(childElementsSet);
				logger.info("adding element to the templateElementSet :: " +element.getName());
				templateElementSet.add(element);
				logger.info("element set size inside :: " +templateElementSet.size());
			}
			logger.info("element set size :: " +templateElementSet.size());
			template.setElements(templateElementSet);
		}
		

		return template;		
	}

	public MDTemplate getTemplateJsonFromEntity(Template template) {
		MDTemplate mdTemplate = new MDTemplate();

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
			mdElement.setValidationExp(element.getValidationExp());
			
			List<MDTemplateElement> childElementsList = new ArrayList<>();
			if(element.getElements() != null && !element.getElements().isEmpty()) {
				for (TemplateElement ce : element.getElements()) {			
					MDTemplateElement mdChildElement = new MDTemplateElement();
					mdChildElement.setName(ce.getName());										
					//e.setGuid(UUID.fromString(element.getGuid()));
					mdChildElement.setGuid(ce.getGuid().toString());
					mdChildElement.setOptions(ce.getOptions());
					mdChildElement.setDefaultValue(ce.getDefaultValue());				
					mdChildElement.setRequired(ce.isRequired());					
					mdChildElement.setCardinalityMin(ce.getMINCardinality());							
					mdChildElement.setCardinalityMax(ce.getMAXCardinality());							
					mdChildElement.setType(ce.getType());							
					mdChildElement.setValidationExp(ce.getValidationExp());							
					
					//mdChildElement.setTemplate(template);										
					childElementsList.add(mdChildElement);
				}				
				mdElement.setElements(childElementsList);
			}			
			elementsList.add(mdElement);
		}

		mdTemplate.setElements(elementsList);
		return mdTemplate;
	}


}
