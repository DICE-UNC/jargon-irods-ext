package org.irodsext.template.config;

import org.irodsext.template.TemplateService;
import org.irodsext.template.TemplateServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("org.irodsext.template.config")
public class AppConfig {

	
	@Bean(name="templateService")
	public TemplateService templateService() {
		return new TemplateServiceImpl();
	}
}
