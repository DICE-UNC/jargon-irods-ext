/* Copyright (c) 2018, University of North Carolina at Chapel Hill */
/* Copyright (c) 2015-2017, Dell EMC */

package com.emc.metalnx.services.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.irods.jargon.core.connection.AuthScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emc.metalnx.services.interfaces.ConfigService;

/**
 * Class that will load all all configurable parameters from *.properties files.
 */
@Service
@Transactional
//@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.INTERFACES)
public class ConfigServiceImpl implements ConfigService {

	public final static Logger logger = LoggerFactory.getLogger(ConfigServiceImpl.class);

	@Value("${msi.api.version}")
	private String msiAPIVersionSupported;

	@Value("${msi.metalnx.list}")
	private String mlxMSIsExpected;

	@Value("${msi.irods.list}")
	private String irods41MSIsExpected;

	@Value("${msi.irods.42.list}")
	private String irods42MSIsExpected;

	@Value("${msi.other.list}")
	private String otherMSIsExpected;

	@Value("${irods.host}")
	private String irodsHost;

	@Value("${irods.port}")
	private String irodsPort;

	@Value("${irods.zoneName}")
	private String irodsZone;

	@Value("${jobs.irods.username}")
	private String irodsJobUser;

	@Value("${jobs.irods.password}")
	private String irodsJobPassword;

	@Value("${jobs.irods.auth.scheme}")
	private String irodsAuthScheme;

	@Value("${populate.msi.enabled}")
	private boolean populateMsiEnabled;

	@Value("${metalnx.enable.tickets}")
	private boolean ticketsEnabled;

	@Value("${metalnx.enable.upload.rules}")
	private boolean uploadRulesEnabled;

	@Value("${metalnx.download.limit}")
	private long downloadLimit;

	@Value("${access.proxy}")
	private boolean handleNoAccessViaProxy;

	@Value("${irods.auth.scheme}")
	private String defaultIrodsAuthScheme;

	@Value("${metalnx.enable.dashboard}")
	private boolean dashboardEnabled;

	/**
	 * Issuer (iss) in the jwt token for access to microservices
	 */
	@Value("${jwt.issuer}")
	private String jwtIssuer;

	/**
	 * Secret for jwt creation. Note that the underlying property should be treated
	 * as secret data with appropriate controls
	 */
	@Value("${jwt.secret}")
	private String jwtSecret;

	/**
	 * algo for computing JWTs
	 */
	@Value("${jwt.algo}")
	private String jwtAlgo;

	/**
	 * This is a string representation of AuthType mappings in the form
	 * iRODType:userFriendlyType| (bar delimited) This is parsed from the
	 * metalnx.properties and can be accessed as a parsed mapping via
	 * {@code ConfigService.listAuthTypeMappings()}
	 */
	@Value("${metalnx.authtype.mappings}")
	private String authtypeMappings;

	@Override
	public GlobalConfig getGlobalConfig() {
		logger.info("getGlobalConfig()");
		GlobalConfig globalConfig = new GlobalConfig();
		globalConfig.setTicketsEnabled(this.isTicketsEnabled());
		globalConfig.setUploadRulesEnabled(isUploadRulesEnabled());
		globalConfig.setHandleNoAccessViaProxy(handleNoAccessViaProxy);
		globalConfig.setDashboardEnabled(dashboardEnabled);
		logger.debug("globalConfig:{}", globalConfig);
		return globalConfig;
	}

	@Override
	public String getMsiAPIVersionSupported() {
		if (msiAPIVersionSupported == null)
			return "";
		return msiAPIVersionSupported;
	}

	@Override
	public List<String> getMlxMSIsExpected() {
		if (mlxMSIsExpected == null)
			return Collections.emptyList();
		return Arrays.asList(mlxMSIsExpected.split(","));
	}

	@Override
	public List<String> getIrods41MSIsExpected() {
		if (irods41MSIsExpected == null)
			return Collections.emptyList();
		return Arrays.asList(irods41MSIsExpected.split(","));
	}

	@Override
	public List<String> getIrods42MSIsExpected() {
		if (irods42MSIsExpected == null)
			return Collections.emptyList();
		return Arrays.asList(irods42MSIsExpected.split(","));
	}

	@Override
	public List<String> getOtherMSIsExpected() {
		if (otherMSIsExpected == null)
			return Collections.emptyList();
		return Arrays.asList(otherMSIsExpected.split(","));
	}

	@Override
	public String getIrodsHost() {
		return irodsHost;
	}

	@Override
	public String getIrodsPort() {
		return irodsPort;
	}

	@Override
	public String getIrodsZone() {
		return irodsZone;
	}

	@Override
	public String getIrodsJobUser() {
		return irodsJobUser;
	}

	@Override
	public String getIrodsJobPassword() {
		return irodsJobPassword;
	}

	@Override
	public String getIrodsAuthScheme() {
		return irodsAuthScheme;
	}

	@Override
	public long getDownloadLimit() {
		return downloadLimit;
	}

	@Override
	public boolean isPopulateMsiEnabled() {
		return populateMsiEnabled;
	}

	public boolean isTicketsEnabled() {
		return ticketsEnabled;
	}

	public void setTicketsEnabled(boolean ticketsEnabled) {
		this.ticketsEnabled = ticketsEnabled;
	}

	@Override
	public boolean isUploadRulesEnabled() {
		return uploadRulesEnabled;
	}

	public void setUploadRulesEnabled(boolean uploadRulesEnabled) {
		this.uploadRulesEnabled = uploadRulesEnabled;
	}

	public boolean isHandleNoAccessViaProxy() {
		return handleNoAccessViaProxy;
	}

	public void setHandleNoAccessViaProxy(boolean handleNoAccessViaProxy) {
		this.handleNoAccessViaProxy = handleNoAccessViaProxy;
	}

	@Override
	public String getDefaultIrodsAuthScheme() {
		return defaultIrodsAuthScheme;
	}

	public void setDefaultIrodsAuthScheme(String defaultIrodsAuthScheme) {
		this.defaultIrodsAuthScheme = defaultIrodsAuthScheme;
	}

	@Override
	public boolean isDashboardEnabled() {
		return dashboardEnabled;
	}

	public void setDashboardEnabled(boolean dashboardEnabled) {
		this.dashboardEnabled = dashboardEnabled;
	}

	@Override
	public List<AuthTypeMapping> listAuthTypeMappings() {
		List<AuthTypeMapping> authTypeList = new ArrayList<AuthTypeMapping>();
		if (this.getAuthtypeMappings() == null || this.getAuthtypeMappings().isEmpty()
				|| this.getAuthtypeMappings().equals("${metalnx.authtype.mappings}")) {
			for (String scheme : AuthScheme.getAuthSchemeList()) {
				authTypeList.add(new AuthTypeMapping(scheme, scheme));
			}
		} else {
			String[] entries;
			// parse and create a custom auth type list
			entries = this.getAuthtypeMappings().split("\\|");
			for (String entry : entries) {
				String[] parsedEntry = entry.split(":");
				if (parsedEntry.length != 2) {
					throw new IllegalArgumentException("unparsable authTypeMapping");
				}
				authTypeList.add(new AuthTypeMapping(parsedEntry[0], parsedEntry[1]));
			}

		}
		return authTypeList;
	}

	public String getAuthtypeMappings() {
		return authtypeMappings;
	}

	public void setAuthtypeMappings(String authtypeMappings) {
		this.authtypeMappings = authtypeMappings;
	}

	@Override
	public String getJwtIssuer() {
		return jwtIssuer;
	}

	public void setJwtIssuer(String jwtIssuer) {
		this.jwtIssuer = jwtIssuer;
	}

	@Override
	public String getJwtSecret() {
		return jwtSecret;
	}

	public void setJwtSecret(String jwtSecret) {
		this.jwtSecret = jwtSecret;
	}

	@Override
	public String getJwtAlgo() {
		return jwtAlgo;
	}

	public void setJwtAlgo(String jwtAlgo) {
		this.jwtAlgo = jwtAlgo;
	}
}
