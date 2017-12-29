/**
 * 
 */
package org.irodsext.dataprofiler;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.exception.DataNotFoundException;
import org.irods.jargon.core.exception.JargonException;
import org.irods.jargon.core.pub.CollectionAndDataObjectListAndSearchAO;
import org.irods.jargon.core.pub.IRODSAccessObjectFactory;
import org.irods.jargon.core.pub.domain.Collection;
import org.irods.jargon.core.pub.domain.DataObject;
import org.irods.jargon.core.pub.domain.ObjStat;
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
	@SuppressWarnings("rawtypes")
	@Override
	public DataProfile retrieveDataProfile(String irodsAbsolutePath) throws DataNotFoundException, JargonException {
		return retrieveDataProfile(irodsAbsolutePath, this.getDefaultDataProfilerSettings());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.irods.jargon.extensions.dataprofiler.DataProfileService#
	 * retrieveDataProfile(java.lang.String,
	 * org.irods.jargon.extensions.dataprofiler.DataProfilerSettings)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public DataProfile retrieveDataProfile(String irodsAbsolutePath, DataProfilerSettings dataProfilerSettings)
			throws DataNotFoundException, JargonException {
		log.info("retrieveDataProfile()");

		if (irodsAbsolutePath == null || irodsAbsolutePath.isEmpty()) {
			throw new IllegalArgumentException("null or empty irodsAbsolutePath");
		}

		if (dataProfilerSettings == null) {
			throw new IllegalArgumentException("null dataProfilerSettings");
		}

		log.info("irodsAbsolutePath:{}", irodsAbsolutePath);
		log.info("dataProfilerSettings:{}", dataProfilerSettings);

		CollectionAndDataObjectListAndSearchAO collectionAndDataObjectListAndSearchAO = this
				.getIrodsAccessObjectFactory().getCollectionAndDataObjectListAndSearchAO(getIrodsAccount());
		log.info("getting objStat...");

		ObjStat objStat = collectionAndDataObjectListAndSearchAO.retrieveObjectStatForPath(irodsAbsolutePath);

		if (objStat.isSomeTypeOfCollection()) {
			return retrieveDataProfileForCollection(irodsAbsolutePath, objStat, dataProfilerSettings);
		} else {
			return retrieveDataProfileForDataObject(irodsAbsolutePath, objStat, dataProfilerSettings);
		}

	}

	private DataProfile<DataObject> retrieveDataProfileForDataObject(String irodsAbsolutePath, ObjStat objStat,
			DataProfilerSettings dataProfilerSettings) {
		log.info("retriveDataProfileForDataObject()");
		log.info("objStat:{}", objStat);
		DataProfile<DataObject> dataProfile = retrieveBaseDataObjectProfile(irodsAbsolutePath, dataProfilerSettings);

		log.info("look for special attributes");

		checkIfStarred(dataProfile, this.getIrodsAccount().getUserName());
		checkIfShared(dataProfile, this.getIrodsAccount().getUserName());
		extractTags(dataProfile);
		establishDataType(dataProfile);

		return dataProfile;
	}

	private DataProfile<Collection> retrieveDataProfileForCollection(String irodsAbsolutePath, ObjStat objStat,
			DataProfilerSettings dataProfilerSettings) {
		log.info("retriveDataProfileForCollection()");
		log.info("objStat:{}", objStat);
		return null;
	}

}
