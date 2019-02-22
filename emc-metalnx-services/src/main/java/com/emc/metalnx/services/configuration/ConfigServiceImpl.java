/* Copyright (c) 2018, University of North Carolina at Chapel Hill */
/* Copyright (c) 2015-2017, Dell EMC */

package com.emc.metalnx.services.configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.emc.metalnx.services.interfaces.ConfigService;

/**
 * Class that will load all all configurable parameters from *.properties files.
 */
@Service
@Transactional
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.INTERFACES)
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
	public String toString() {
		final int maxLen = 10;
		StringBuilder builder = new StringBuilder();
		builder.append("ConfigServiceImpl [msiAPIVersionSupported=").append(msiAPIVersionSupported)
				.append(", mlxMSIsExpected=").append(mlxMSIsExpected).append(", irods41MSIsExpected=")
				.append(irods41MSIsExpected).append(", irods42MSIsExpected=").append(irods42MSIsExpected)
				.append(", otherMSIsExpected=").append(otherMSIsExpected).append(", irodsHost=").append(irodsHost)
				.append(", irodsPort=").append(irodsPort).append(", irodsZone=").append(irodsZone)
				.append(", irodsJobUser=").append(irodsJobUser).append(", irodsAuthScheme=").append(irodsAuthScheme)
				.append(", populateMsiEnabled=").append(populateMsiEnabled).append(", ticketsEnabled=")
				if (defaultIrodsAuthScheme != null) {
			builder.append("defaultIrodsAuthScheme=").append(defaultIrodsAuthScheme).append(", ");
		}
				.append(ticketsEnabled).append(", uploadRulesEnabled=").append(uploadRulesEnabled)
				.append(", downloadLimit=").append(downloadLimit).append(", handleNoAccessViaProxy=")
				.append(handleNoAccessViaProxy).append(", defaultIrodsAuthScheme=").append(defaultIrodsAuthScheme)
				.append(", getGlobalConfig()=").append(getGlobalConfig()).append(", getMsiAPIVersionSupported()=")
				.append(getMsiAPIVersionSupported()).append(", getMlxMSIsExpected()=")
				.append(getMlxMSIsExpected() != null
						? getMlxMSIsExpected().subList(0, Math.min(getMlxMSIsExpected().size(), maxLen))
						: null)
				.append(", getIrods41MSIsExpected()=")
				.append(getIrods41MSIsExpected() != null
						? getIrods41MSIsExpected().subList(0, Math.min(getIrods41MSIsExpected().size(), maxLen))
						: null)
				.append(", getIrods42MSIsExpected()=")
				.append(getIrods42MSIsExpected() != null
						? getIrods42MSIsExpected().subList(0, Math.min(getIrods42MSIsExpected().size(), maxLen))
						: null)
				.append(", getOtherMSIsExpected()=")
				.append(getOtherMSIsExpected() != null
						? getOtherMSIsExpected().subList(0, Math.min(getOtherMSIsExpected().size(), maxLen))
						: null)
				.append(", getIrodsHost()=").append(getIrodsHost()).append(", getIrodsPort()=").append(getIrodsPort())
				.append(", getIrodsZone()=").append(getIrodsZone()).append(", getIrodsJobUser()=")
				.append(getIrodsJobUser()).append(", getIrodsJobPassword()=").append(getIrodsJobPassword())
				.append(", getIrodsAuthScheme()=").append(getIrodsAuthScheme()).append(", getDownloadLimit()=")
				.append(getDownloadLimit()).append(", isPopulateMsiEnabled()=").append(isPopulateMsiEnabled())
				.append(", isTicketsEnabled()=").append(isTicketsEnabled()).append(", isUploadRulesEnabled()=")
				.append(isUploadRulesEnabled()).append(", isHandleNoAccessViaProxy()=")
				.append(isHandleNoAccessViaProxy()).append(", getDefaultIrodsAuthScheme()=")
				.append(getDefaultIrodsAuthScheme()).append(", getClass()=").append(getClass()).append(", hashCode()=")
				.append(hashCode()).append(", toString()=").append(super.toString()).append("]");
		return builder.toString();
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
}
