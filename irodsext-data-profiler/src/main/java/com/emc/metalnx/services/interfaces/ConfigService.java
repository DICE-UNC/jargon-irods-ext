 /* Copyright (c) 2018, University of North Carolina at Chapel Hill */
 /* Copyright (c) 2015-2017, Dell EMC */
 


package com.emc.metalnx.services.interfaces;

import java.util.List;

import com.emc.metalnx.services.configuration.GlobalConfig;

/**
 * Service used to retrieve all configurable parameters from *.properties files.
 */
public interface ConfigService {
	/**
	 * Finds the MSI API version supported by the current version of Metalnx.
	 * 
	 * @return string representing the version
	 */
	String getMsiAPIVersionSupported();

	/**
	 * Finds the list of all expected Metalnx microservices.
	 * 
	 * @return list of all Metalnx microservices.
	 */
	List<String> getMlxMSIsExpected();

	/**
	 * Finds the list of all expected iRODS 4.1.X microservices.
	 * 
	 * @return list of all iRODS 4.1.X microservices.
	 */
	List<String> getIrods41MSIsExpected();

	/**
	 * Finds the list of all expected irods 4.2.X microservices.
	 * 
	 * @return list of all irods 4.2.X microservices.
	 */
	List<String> getIrods42MSIsExpected();

	/**
	 * Finds the list of all third-party microservices.
	 * 
	 * @return list of all third-party microservices.
	 */
	List<String> getOtherMSIsExpected();

	/**
	 * Find the iCAT hostname.
	 * 
	 * @return String representing the iCAT machine's hostname.
	 */
	String getIrodsHost();

	/**
	 * Find the irods port number.
	 * 
	 * @return String representing irods port number.
	 */
	String getIrodsPort();

	/**
	 * Find the irods default zone.
	 * 
	 * @return String representing the irods default zone.
	 */
	String getIrodsZone();

	/**
	 * Find the jobs username.
	 * 
	 * @return String representing the username used for synchronizing Metalnx and
	 *         iRODS.
	 */
	String getIrodsJobUser();

	/**
	 * Find the jobs password.
	 * 
	 * @return String representing the password used for synchronizing Metalnx and
	 *         iRODS.
	 */
	String getIrodsJobPassword();

	/**
	 * Find the authentication scheme used for authenticating against iRODS.
	 * 
	 * @return String representing the authentication scheme.
	 */
	String getIrodsAuthScheme();

	/**
	 * Find file download limit
	 * 
	 * @return long representing the download limit in Megabytes
	 */
	long getDownloadLimit();

	/**
	 * Checks whether or not the populate MSI flag is enabled
	 * 
	 * @return True, if populate is enabled. False, otherwise.
	 */
	boolean isPopulateMsiEnabled();

	/**
	 * Get a summary config object of global behavior settings, these can be
	 * injected into templates to control the layout of pages and exposing/hiding
	 * functionality
	 * 
	 * @return {@link GlobalConfig} with system-wide optional settings that control
	 *         appearance and behavior
	 */
	GlobalConfig getGlobalConfig();

	/**
	 * Global setting turning on or off the application of rules based on file type
	 * during upload
	 * 
	 * @return <code>boolean</code> that is <code>true</code> when upload rules are
	 *         applied based on file type
	 */
	boolean isUploadRulesEnabled();
}
