package org.irodsext.datatyper;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.pub.IRODSAccessObjectFactory;
import org.irods.jargon.extensions.datatyper.DataType;
import org.irods.jargon.extensions.datatyper.DataTypeResolutionService;
import org.irods.jargon.extensions.datatyper.DataTyperSettings;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class IrodsextDataTypeResolutionServiceTest {

	@Test
	public void testResolveDataTypeString() throws Exception {
		String testName = "/a/path/file.txt";
		IRODSAccount dummyAccount = Mockito.mock(IRODSAccount.class);
		IRODSAccessObjectFactory irodsAccessObjectFactory = Mockito.mock(IRODSAccessObjectFactory.class);
		DataTyperSettings dataTyperSettings = new DataTyperSettings();
		dataTyperSettings.setDetailedDetermination(false);
		dataTyperSettings.setPersistDataTypes(false);
		DataTypeResolutionService dtrs = new IrodsextDataTypeResolutionService(irodsAccessObjectFactory, dummyAccount,
				dataTyperSettings);
		DataType actual = dtrs.resolveDataType(testName);
		Assert.assertNotNull("no type returned", actual);
		Assert.assertEquals("text/plain", actual.getMimeType());

	}

	@Test
	public void testQuickType() throws Exception {
		String testName = "/a/path/file.txt";
		IRODSAccount dummyAccount = Mockito.mock(IRODSAccount.class);
		IRODSAccessObjectFactory irodsAccessObjectFactory = Mockito.mock(IRODSAccessObjectFactory.class);
		DataTyperSettings dataTyperSettings = new DataTyperSettings();
		dataTyperSettings.setDetailedDetermination(false);
		dataTyperSettings.setPersistDataTypes(false);
		DataTypeResolutionService dtrs = new IrodsextDataTypeResolutionService(irodsAccessObjectFactory, dummyAccount,
				dataTyperSettings);
		String actual = dtrs.quickMimeType(testName);
		Assert.assertNotNull("no type returned", actual);
		Assert.assertEquals("text/plain", actual);

	}

}
