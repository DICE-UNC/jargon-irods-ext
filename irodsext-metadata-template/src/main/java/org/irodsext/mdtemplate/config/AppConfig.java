package org.irodsext.mdtemplate.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.irods.jargon.metadatatemplate.AbstractMetadataService;
import org.irodsext.mdtemplate.TemplateElementService;
import org.irodsext.mdtemplate.TemplateElementServiceImpl;
import org.irodsext.mdtemplate.TemplateService;
import org.irodsext.mdtemplate.TemplateServiceImpl;
import org.irodsext.mdtemplate.dao.TemplateDao;
import org.irodsext.mdtemplate.dao.impl.TemplateDaoImpl;
import org.irodsext.mdtemplate.services.IrodsExtMetadataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan({ "org.irodsext.mdtemplate",
	"org.irods.jargon.metadatatemplate" })
@PropertySource(value = { "classpath:db.properties" })
public class AppConfig {

	
	@Bean(name="templateService")
	public TemplateService templateService() {
		return new TemplateServiceImpl();
	}
	
	@Bean(name="templateElementService")
	public TemplateElementService templateElementService() {
		return new TemplateElementServiceImpl();
	}
	
	@Bean(name="abstractMetadataService")
	public AbstractMetadataService abstractMetadataService() {
		return new IrodsExtMetadataServiceImpl();
	}
	
	
	@Autowired
    private Environment environment;
 
    
	@Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(new String[] { "org.irodsext.mdtemplate" });
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
     }
     
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getRequiredProperty("db.driver"));
        dataSource.setUrl(environment.getRequiredProperty("db.url"));
        dataSource.setUsername(environment.getRequiredProperty("db.username"));
        dataSource.setPassword(environment.getRequiredProperty("db.password"));
        return dataSource;
    }
     
    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
        properties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"));
        return properties;        
    }
     
    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory s) {
       HibernateTransactionManager txManager = new HibernateTransactionManager();
       txManager.setSessionFactory(s);
       return txManager;
    }
}
