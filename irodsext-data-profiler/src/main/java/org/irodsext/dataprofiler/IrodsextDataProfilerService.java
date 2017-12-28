/**
 * 
 */
package org.irodsext.dataprofiler;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.exception.DataNotFoundException;
import org.irods.jargon.core.exception.JargonException;
import org.irods.jargon.core.pub.IRODSAccessObjectFactory;
import org.irods.jargon.core.pub.domain.Collection;
import org.irods.jargon.core.pub.domain.DataObject;
import org.irods.jargon.extensions.dataprofiler.DataProfile;
import org.irods.jargon.extensions.dataprofiler.DataProfileService;
import org.irods.jargon.extensions.dataprofiler.DataProfilerSettings;
import org.irods.jargon.extensions.datatyper.DataTypeResolutionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mike Conway - NIEHS
 *
 */
public class IrodsextDataProfilerService extends DataProfileService {

	public static final Logger log = LoggerFactory.getLogger(IrodsextDataProfilerService.class);

	/**
	 * @param defaultDataProfilerSettings
	 * @param dataTypeResolutionService
	 * @param irodsAccessObjectFactory
	 * @param irodsAccount
	 */
	public IrodsextDataProfilerService(DataProfilerSettings defaultDataProfilerSettings,
			DataTypeResolutionService dataTypeResolutionService, IRODSAccessObjectFactory irodsAccessObjectFactory,
			IRODSAccount irodsAccount) {
		super(defaultDataProfilerSettings, dataTypeResolutionService, irodsAccessObjectFactory, irodsAccount);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.irods.jargon.extensions.dataprofiler.DataProfileService#
	 * retrieveDataProfile(java.lang.String)
	 */
	@Override
	public DataProfile retrieveDataProfile(String irodsAbsolutePath) throws DataNotFoundException, JargonException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.irods.jargon.extensions.dataprofiler.DataProfileService#
	 * retrieveDataProfile(java.lang.String,
	 * org.irods.jargon.extensions.dataprofiler.DataProfilerSettings)
	 */
	@Override
	public DataProfile retrieveDataProfile(String irodsAbsolutePath, DataProfilerSettings dataProfilerSettings)
			throws DataNotFoundException, JargonException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.irods.jargon.extensions.dataprofiler.DataProfileService#
	 * retrieveDataProfileForCollection(java.lang.String)
	 */
	@Override
	public DataProfile<Collection> retrieveDataProfileForCollection(String irodsAbsolutePath)
			throws DataNotFoundException, JargonException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.irods.jargon.extensions.dataprofiler.DataProfileService#
	 * retrieveDataProfileForCollection(java.lang.String,
	 * org.irods.jargon.extensions.dataprofiler.DataProfilerSettings)
	 */
	@Override
	public DataProfile<Collection> retrieveDataProfileForCollection(String irodsAbsolutePath,
			DataProfilerSettings dataProfilerSettings) throws DataNotFoundException, JargonException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.irods.jargon.extensions.dataprofiler.DataProfileService#
	 * retrieveDataProfileForDataObject(java.lang.String)
	 */
	@Override
	public DataProfile<DataObject> retrieveDataProfileForDataObject(String irodsAbsolutePath)
			throws DataNotFoundException, JargonException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.irods.jargon.extensions.dataprofiler.DataProfileService#
	 * retrieveDataProfileForDataObject(java.lang.String,
	 * org.irods.jargon.extensions.dataprofiler.DataProfilerSettings)
	 */
	@Override
	public DataProfile<DataObject> retrieveDataProfileForDataObject(String irodsAbsolutePath,
			DataProfilerSettings dataProfilerSettings) throws DataNotFoundException, JargonException {
		// TODO Auto-generated method stub
		return null;
	}

}
