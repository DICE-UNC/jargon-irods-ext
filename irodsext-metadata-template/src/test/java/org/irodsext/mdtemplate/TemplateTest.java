package org.irodsext.mdtemplate;

import java.util.UUID;

import org.irods.jargon.metadatatemplate.AbstractMetadataService;
import org.irods.jargon.metadatatemplate.model.MDTemplate;
import org.irods.jargon.metadatatemplate.MetadataTemplateException;
import org.irods.jargon.metadatatemplate.MetadataTemplateNotFoundException;
import org.irodsext.mdtemplate.config.AppConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import junit.framework.TestCase;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class TemplateTest extends TestCase{

	/*@Autowired
	private TemplateService templateService;
	*/
	@Autowired
	private AbstractMetadataService abstractMetadataService;

/*	@Autowired
	private TemplateElementService templateElementService;
*/
	/*@Test
	public void createTemplate() {
		Template template =new Template();
		template.setTemplateName("template2");
		Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis());
		template.setCreateTs(timestamp);
		template.setGuid(UUID.randomUUID());  


		Long id = (Long) templateService.createTemplate(template);
		System.out.println("Saved :: "+id);

	}*/
	@Transactional
	@Test
	public void getTemplateBGuid() throws MetadataTemplateNotFoundException, MetadataTemplateException {	
		MDTemplate template = abstractMetadataService.findTemplateByUUID(UUID.fromString("9f089439-9665-4464-929b-2704d765b588"));
		System.out.println("Saved :: "+template.getTemplateName());

	}
	

	/*@Test
	public void createTemplateWithElement() {
		Template template =new Template();
		template.setTemplateName("template8");
		Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis());
		template.setCreateTs(timestamp);
		template.setGuid(UUID.randomUUID());  


		Long id = (Long) templateService.createTemplate(template);
		System.out.println("Saved :: "+id);

		TemplateElement templateElement = new TemplateElement();
		templateElement.setAttribute("templateAttrTest");
		templateElement.setDefaultValue("defaultVal");
		templateElement.setCardinality((long) 3); 
		templateElement.setTemplate(template);    	
		templateElement.setGuid(UUID.randomUUID());  
		templateElement.setParentId(null); 
		templateElementService.createTemplateElement(templateElement);

	}

	@Test
	public void addElementForExistingTemplate() {
		Template template = templateService.findByName("template8");

		System.out.println("template Id :: " + template.getId());

		TemplateElement templateElement = new TemplateElement();
		templateElement.setAttribute("templateAttrTest1");
		templateElement.setDefaultValue("defaultVal1");
		templateElement.setCardinality((long) 2); 
		templateElement.setTemplate(template);    	
		templateElement.setGuid(UUID.randomUUID());  
		templateElement.setParentId(null); 
		templateElementService.createTemplateElement(templateElement);

	}*/


	/*@Test
	public void deleteTemplate() {	
		Template template = templateService.findByName("template8");
		
		long dId = template.getId();
    	templateService.deleteTemplate(dId);
    	System.out.println("Deleted fir id :: " + dId);
	}
*/
	/*@Test
	public void updateTemplate() {	
		Template template = templateService.findByName("template8");
		
	
		template.setAccess_type("private");

		
    	templateService.merge(template);
    	System.out.println("update for id :: " + template.getId());
	}*/
	
	/*@Test
	public void updateTemplateElement() {
		
		TemplateElement templateElement = templateElementService.
    	templateElement.setAttribute("templateAttrTest_23");
    	templateElement.setDefaultValue("defaultVal_23");
    	templateElement.setCardinality((long) 3); 
    	
    	templateService.merge(template);

	}*/

}
