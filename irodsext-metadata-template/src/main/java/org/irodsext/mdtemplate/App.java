package org.irodsext.mdtemplate;

import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.UUID;



import org.irodsext.mdtemplate.TemplateService;
import org.irodsext.mdtemplate.config.AppConfig;
import org.irodsext.mdtemplate.entity.Template;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Hello world!
 *
 */

public class App 
{
	/*@Autowired
	private static HelloWorld obj;*/
	
	//@Autowired
	//private static TemplateService templateService;
	
    public static void main( String[] args )
    {
    	ApplicationContext ctx = 
      	         new AnnotationConfigApplicationContext(AppConfig.class);
    	TemplateService templateService = (TemplateService) ctx.getBean("templateService");
  	    App app = new App();
    	app.addTemplate(templateService);
    	//app.findTemplate(templateService);
       
    }
    
    public void addTemplate(TemplateService templateService) {
    	Template template =new Template();
    	template.setTemplateName("template5");
    	Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis());
    	template.setCreateTs(timestamp);
    	UUID uuid = UUID.randomUUID();
		long l = ByteBuffer.wrap(uuid.toString().getBytes()).getLong();
		System.out.println("UUID :: " +UUID.randomUUID().toString());
    	template.setGuid(UUID.randomUUID());  	
    	templateService.createTemplate(template);
    	
    }
    
    public void addTemplateMetadata(TemplateService templateService) {
    	/*TemplateElementEntity element = new TemplateElementEntity();
    	element.setAttribute("Bazinga attribute");
    	element.setAttributeUnit("attr unit");
    	element.setAttributeValue("attr value");
    	element.setTemplate(template);*/
    	//TemplateService templateService = new TemplateServiceImpl();
    	//TemplateElementService templateElementService = new TemplateElementServiceImpl();
    	//templateElementService.createTemplateElement(element);
    }
    public void findTemplate(TemplateService templateService) {
    	
    	Template template = templateService.findByName("template2");
    	System.out.println("Existing template :: " +template.getTemplateName()+ " , and its GUID :: " +template.getGuid());
    }
}
