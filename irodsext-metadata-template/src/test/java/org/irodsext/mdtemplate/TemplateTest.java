package org.irodsext.mdtemplate;

import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.UUID;

import org.irodsext.mdtemplate.config.AppConfig;
import org.irodsext.mdtemplate.entity.Template;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import junit.framework.TestCase;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class TemplateTest extends TestCase{

	@Autowired
	private TemplateService templateService;
	

	@Test
	public void createTemplate() {
		Template template =new Template();
    	template.setTemplateName("template6");
    	Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis());
    	template.setCreateTs(timestamp);
    	UUID uuid = UUID.randomUUID();
		long l = ByteBuffer.wrap(uuid.toString().getBytes()).getLong();
		System.out.println("UUID :: " +UUID.randomUUID().toString());
    	template.setGuid(UUID.randomUUID());  	
    	Long id = (Long) templateService.createTemplate(template);
    	System.out.println("Saved :: "+id);
	}
}
