/**
 * 
 */
package org.irodsext.dataprofiler;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.exception.JargonRuntimeException;
import org.irods.jargon.core.pub.IRODSAccessObjectFactory;
import org.irods.jargon.extensions.dataprofiler.DataProfilerService;
import org.irods.jargon.extensions.dataprofiler.DataProfilerFactory;
import org.irods.jargon.extensions.dataprofiler.DataProfilerSettings;
import org.irods.jargon.extensions.datatyper.DataTypeResolutionServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Factory for {@link DataProfiler} implementation
 * 
 * @author Mike Conway - NIEHS
 *
 */
public class IrodsextDataProfilerFactoryImpl implements DataProfilerFactory {

	@Autowired
	private IRODSAccessObjectFactory irodsAccessObjectFactory;

	@Autowired
	private DataProfilerSettings dataProfilerSettings;

	@Autowired
	private DataTypeResolutionServiceFactory dataTypeResolutionServiceFactory;

	/* (non-Javadoc)
	 * @see org.irodsext.dataprofiler.DataProfilerFactory#instanceDataProfilerService(org.irods.jargon.core.connection.IRODSAccount)
	 */
	@Override
	public DataProfilerService instanceDataProfilerService(final IRODSAccount irodsAccount) {
		validateDependencies();
		DataProfilerService dataProfilerService = new IrodsextDataProfilerService(dataProfilerSettings,
				irodsAccessObjectFactory, irodsAccount);
		dataProfilerService.setDataTypeResolutionService(
				dataTypeResolutionServiceFactory.instanceDataTypeResolutionService(irodsAccount));
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
	private void validateDependencies() {
		if (irodsAccessObjectFactory == null) {
			throw new JargonRuntimeException("null irodsAccessObjectFactory");
		}

		if (dataProfilerSettings == null) {
			throw new JargonRuntimeException("null dataProfilerSettings");
		}

		if (dataTypeResolutionServiceFactory == null) {
			throw new IllegalArgumentException("null dataTypeResolutionServiceFactory");
		}
	}

}
