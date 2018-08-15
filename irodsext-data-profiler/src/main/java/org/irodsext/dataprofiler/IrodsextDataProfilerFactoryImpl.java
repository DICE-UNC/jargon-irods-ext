/**
 * 
 */
package org.irodsext.dataprofiler;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.exception.JargonRuntimeException;
import org.irods.jargon.core.pub.IRODSAccessObjectFactory;
import org.irods.jargon.extensions.dataprofiler.DataProfilerFactory;
import org.irods.jargon.extensions.dataprofiler.DataProfilerService;
import org.irods.jargon.extensions.dataprofiler.DataProfilerSettings;
import org.irods.jargon.extensions.datatyper.DataTypeResolutionServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.emc.metalnx.core.domain.entity.DataGridUser;
import com.emc.metalnx.services.interfaces.FavoritesService;
import com.emc.metalnx.services.interfaces.UserService;

/**
 * Factory for {@link DataProfiler} implementation
 * 
 * @author Mike Conway - NIEHS
 *
 */
public  class IrodsextDataProfilerFactoryImpl implements DataProfilerFactory {

	public static final Logger log = LoggerFactory.getLogger(IrodsextDataProfilerFactoryImpl.class);

	private IRODSAccessObjectFactory irodsAccessObjectFactory;

	private DataProfilerSettings dataProfilerSettings;

	private DataTypeResolutionServiceFactory dataTypeResolutionServiceFactory;

	/**
	 * MetaLnx favorites service is current source of 'favorites' and bookmarks,
	 * this comes from the irods-ext database at moment. This is provided to the
	 * data profiler service instance during factory creation
	 */
	private FavoritesService favoritesService;

	/**
	 * MetaLnx service to map user/zone accounts to the MetaLnx {@link DataGridUser}
	 */
	private UserService userService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irodsext.dataprofiler.DataProfilerFactory#instanceDataProfilerService(org
	 * .irods.jargon.core.connection.IRODSAccount)
	 */
	@Override
	public DataProfilerService instanceDataProfilerService(final IRODSAccount irodsAccount) {
		validateContext();
		IrodsextDataProfilerService dataProfilerService = new IrodsextDataProfilerService(dataProfilerSettings,
				irodsAccessObjectFactory, irodsAccount);
		dataProfilerService.setDataTypeResolutionService(
				dataTypeResolutionServiceFactory.instanceDataTypeResolutionService(irodsAccount));
		dataProfilerService.setFavoritesService(getFavoritesService());
		dataProfilerService.setDataGridUser(resolveDataGridUser(irodsAccount));
		return dataProfilerService;
	}

	public IRODSAccessObjectFactory getIrodsAccessObjectFactory() {
		return irodsAccessObjectFactory;
	}

	public void setIrodsAccessObjectFactory(IRODSAccessObjectFactory irodsAccessObjectFactory) {
		this.irodsAccessObjectFactory = irodsAccessObjectFactory;
	}

	public DataProfilerSettings getDataProfilerSettings() {
		return dataProfilerSettings;
	}

	public void setDataProfilerSettings(DataProfilerSettings dataProfilerSettings) {
		this.dataProfilerSettings = dataProfilerSettings;
	}

	public DataTypeResolutionServiceFactory getDataTypeResolutionServiceFactory() {
		return dataTypeResolutionServiceFactory;
	}

	public void setDataTypeResolutionServiceFactory(DataTypeResolutionServiceFactory dataTypeResolutionServiceFactory) {
		this.dataTypeResolutionServiceFactory = dataTypeResolutionServiceFactory;
	}

	/**
	 * Just a sanity check
	 */
	private void validateContext() {
		if (irodsAccessObjectFactory == null) {
			throw new JargonRuntimeException("null irodsAccessObjectFactory");
		}

		if (dataProfilerSettings == null) {
			throw new JargonRuntimeException("null dataProfilerSettings");
		}

		if (dataTypeResolutionServiceFactory == null) {
			throw new IllegalArgumentException("null dataTypeResolutionServiceFactory");
		}

		if (userService == null) {
			throw new IllegalArgumentException("null dataTypeResolutionServiceFactory");
		}
	}

	public FavoritesService getFavoritesService() {
		return favoritesService;
	}

	public void setFavoritesService(FavoritesService favoritesService) {
		this.favoritesService = favoritesService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	private DataGridUser resolveDataGridUser(final IRODSAccount irodsAccount) {
		log.info("resolveDataGridUser");
		return userService.findByUsernameAndAdditionalInfo(irodsAccount.getUserName(), irodsAccount.getZone());
	}

}
