package org.irodsext.mdtemplate.config;

import org.irodsext.mdtemplate.TemplateElementService;
import org.irodsext.mdtemplate.TemplateElementServiceImpl;
import org.irodsext.mdtemplate.TemplateService;
import org.irodsext.mdtemplate.TemplateServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("org.irodsext.mdtemplate")
public class AppConfig {

	
	@Bean(name="templateService")
	public TemplateService templateService() {
		return new TemplateServiceImpl();
	}
	
	@Bean(name="TemplateElementService")
	public TemplateElementService templateElementService() {
		return new TemplateElementServiceImpl();
	}
}
