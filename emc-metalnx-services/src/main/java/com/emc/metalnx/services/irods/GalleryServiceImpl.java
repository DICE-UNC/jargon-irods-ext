/* Copyright (c) 2018, University of North Carolina at Chapel Hill */
/* Copyright (c) 2015-2017, Dell EMC */

package com.emc.metalnx.services.irods;

import org.irods.jargon.core.exception.JargonException;
import org.irods.jargon.core.pub.RuleProcessingAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.emc.metalnx.services.interfaces.CollectionService;
import com.emc.metalnx.services.interfaces.ConfigService;
import com.emc.metalnx.services.interfaces.IRODSServices;

@Service("ruleService")
@Transactional
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.INTERFACES)
public class GalleryServiceImpl {

	private static final Logger logger = LoggerFactory.getLogger(GalleryServiceImpl.class);

	@Autowired
	CollectionService cs;

	@Autowired
	private IRODSServices is;

	@Autowired
	private ConfigService configService;

	public void list(String path, int offset, int limit) throws JargonException {
		logger.info("list()");
		RuleProcessingAO ruleProcessingAO = is.getRuleProcessingAO();

	}
}
