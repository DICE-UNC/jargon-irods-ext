package com.emc.metalnx.services.irods.mail;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.internet.AddressException;
import javax.mail.internet.ParseException;

import org.irods.jargon.midtier.utils.configuration.MidTierConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emc.metalnx.services.interfaces.IRODSServices;
import com.emc.metalnx.services.interfaces.UserService;
import com.emc.metalnx.services.interfaces.mail.Mail;
import com.emc.metalnx.services.interfaces.mail.MailService;

/**
 * Mail services
 * 
 * @author Mike Conway - NIEHS
 *
 */

@Service
@Transactional
public class MailServiceImpl implements MailService {

	@Autowired
	private IRODSServices irodsServices;

	@Autowired
	private UserService userService;

	@Autowired
	private MailServiceFactory mailServiceFactory;

	@Autowired
	private MailProperties mailProperties;

	@Autowired
	private MidTierConfiguration midTierConfiguration;

	private JavaMailSender javaMailSender;

	private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

	public IRODSServices getIrodsServices() {
		return irodsServices;
	}

	public void setIrodsServices(IRODSServices irodsServices) {
		this.irodsServices = irodsServices;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public MailServiceFactory getMailServiceFactory() {
		return mailServiceFactory;
	}

	public void setMailServiceFactory(MailServiceFactory mailServiceFactory) {
		this.mailServiceFactory = mailServiceFactory;
	}

	/**
	 * Init method will check if mail is enabled, and if so create a mail sender. If
	 * not enabled mail functions are ignored.
	 */
	@PostConstruct
	public void init() {
		logger.info("init()");

		if (mailProperties == null) {
			logger.error("no mail properties configured for init()");
			throw new IllegalStateException("uninitialized mail properties");
		}

		if (mailServiceFactory == null) {
			logger.error("no mail service factory configured for init()");
			throw new IllegalStateException("uninitialized mail service factory");
		}

		if (!this.getMailProperties().isEnabled()) {
			logger.info("mail is not enabled, will not create mail sender");
		} else {
			this.javaMailSender = mailServiceFactory.instance();
			logger.info("mail sender initialized");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.emc.metalnx.services.irods.mail.MailService#isMailEnabled()
	 */
	@Override
	public boolean isMailEnabled() {
		if (mailProperties == null) {
			logger.error("no mail properties configured for init()");
			throw new IllegalStateException("uninitialized mail properties");
		}
		return this.getMailProperties().isEnabled();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.emc.metalnx.services.irods.mail.MailService#sendEmail(com.emc.metalnx.
	 * services.interfaces.mail.Mail)
	 */
	@Override
	public void sendEmail(Mail mail) throws SendFailedException{
		logger.info("sendMail()");
		if (mail == null) {
			throw new IllegalArgumentException("Null mail");
		}

		if (!isMailEnabled()) {
			logger.warn("#################################################");
			logger.warn("mail not enabled, this should not be called, will ignore");
			logger.warn("#################################################");
			return;
		}

		//logger.info("sending mail:{}", mail);
		logger.info("#################################################");
		logger.info("Mail To :: ", mailProperties.getTo().toString());
		logger.info("Mail From :: ", mailProperties.getFrom().toString());
		logger.info("#################################################");

		if (mail.getMailTo() == null || mail.getMailTo().isEmpty()) {
			
			if(mailProperties.getTo() != null && !mailProperties.getTo().isEmpty()) {
				logger.info("setting to email id from properties.");
				mail.setMailTo(mailProperties.getTo());
			}else if (midTierConfiguration.getIrodsAdminEmail() == null
					|| midTierConfiguration.getIrodsAdminEmail().isEmpty()) {

				logger.warn("#################################################");
				logger.warn("mail To address for rods admin not configured, will ignore this message");
				logger.warn("#################################################");
				throw new SendFailedException();
			} else {
				logger.info("no email to field, use rods admin");
				mail.setMailTo(midTierConfiguration.getIrodsAdminEmail());
			}
		}

		if (mail.getMailFrom() == null || mail.getMailFrom().isEmpty()) {
			
			if (mailProperties.getFrom() != null && !mailProperties.getFrom().isEmpty()) {
				logger.info("setting from email id from properties.");
				mail.setMailFrom(mailProperties.getFrom());
			}else if(midTierConfiguration.getIrodsAdminEmail()== null || midTierConfiguration.getIrodsAdminEmail().isEmpty()) {
				logger.warn("#################################################");
				logger.warn("mail From address for rods admin not configured, will ignore this message");
				logger.warn("#################################################");
				throw new SendFailedException();
			} else {
				logger.info("no email from field, use rods admin");
				mail.setMailFrom(midTierConfiguration.getIrodsAdminEmail());
			}
		}

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(mail.getMailFrom());
		message.setTo(mail.getMailTo());		
		message.setSubject(mail.getMailSubject());
		message.setText(mail.getMailContent());

		if (mailProperties.getCc() != null && !mailProperties.getCc().isEmpty()) {
			logger.info("setting cc email id from properties.");
			String cc = mailProperties.getCc();
			String[] ccRecipient =  cc.split(",");
			mail.setMailCc(ccRecipient);
			message.setCc(mail.getMailCc());
		}

		logger.info("sending mail::{}", message);
		javaMailSender.send(message);


	}

	public MailProperties getMailProperties() {
		return mailProperties;
	}

	public void setMailProperties(MailProperties mailProperties) {
		this.mailProperties = mailProperties;
	}

	public JavaMailSender getJavaMailSender() {
		return javaMailSender;
	}

	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	public MidTierConfiguration getMidTierConfiguration() {
		return midTierConfiguration;
	}

	public void setMidTierConfiguration(MidTierConfiguration midTierConfiguration) {
		this.midTierConfiguration = midTierConfiguration;
	}

}
