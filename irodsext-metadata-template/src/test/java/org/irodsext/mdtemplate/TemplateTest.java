package org.irodsext.mdtemplate;



import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.irods.jargon.metadatatemplate.AbstractMetadataService;
import org.irods.jargon.metadatatemplate.model.MDTemplate;
import org.irods.jargon.metadatatemplate.model.MDTemplateElement;
import org.irods.jargon.metadatatemplate.MetadataTemplateException;
import org.irodsext.mdtemplate.config.AppConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.threeten.bp.OffsetDateTime;

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
	@Transactional
	@Test
	public void createTemplate() 
	{
		MDTemplate mdTemplate = new MDTemplate();
		mdTemplate.setTemplateName("T1");
		mdTemplate.setCreateTs(OffsetDateTime.now());
		mdTemplate.modifyTs(OffsetDateTime.now());
		mdTemplate.setGuid(UUID.randomUUID().toString());  
		
		List<MDTemplateElement> mdTemplateElementList = new ArrayList<>();
		
		MDTemplateElement mdtemplateElement1 = new MDTemplateElement();
		mdtemplateElement1.setName("MiseqTest");
		//mdtemplateElement1.setGuid(UUID.randomUUID().toString());
		mdtemplateElement1.setOptions("miseq,novaseq,thatseq");
		mdtemplateElement1.required(false);
		mdtemplateElement1.setType("String");
		mdtemplateElement1.setCardinalityMax(1);
		mdtemplateElement1.setCardinalityMax(3);
		
		
		mdTemplateElementList.add(mdtemplateElement1);

		mdTemplate.setElements(mdTemplateElementList);
		try {
			abstractMetadataService.saveTemplate(mdTemplate);
		} catch (MetadataTemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}
	
	/*@Transactional
	@Test
	public void getTemplateBGuid() throws MetadataTemplateException {	
		MDTemplate template = abstractMetadataService.findTemplateByGuid(UUID.fromString("6e201f58-8b83-4ed0-a04a-487a448afe8a"));
		System.out.println("Found  :: "+template);

	}*/
	

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
