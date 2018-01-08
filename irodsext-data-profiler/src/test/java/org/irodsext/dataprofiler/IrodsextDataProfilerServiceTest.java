/**
 * 
 */
package org.irodsext.dataprofiler;

import java.io.File;
import java.util.Properties;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.connection.JargonProperties;
import org.irods.jargon.core.connection.SettableJargonProperties;
import org.irods.jargon.core.pub.DataTransferOperations;
import org.irods.jargon.core.pub.IRODSAccessObjectFactory;
import org.irods.jargon.core.pub.IRODSFileSystem;
import org.irods.jargon.core.pub.io.IRODSFile;
import org.irods.jargon.core.pub.io.IRODSFileFactory;
import org.irods.jargon.extensions.dataprofiler.DataProfile;
import org.irods.jargon.extensions.dataprofiler.DataProfilerService;
import org.irods.jargon.extensions.dataprofiler.DataProfilerSettings;
import org.irods.jargon.extensions.datatyper.DataTypeResolutionService;
import org.irods.jargon.extensions.datatyper.DataTyperSettings;
import org.irods.jargon.testutils.IRODSTestSetupUtilities;
import org.irods.jargon.testutils.TestingPropertiesHelper;
import org.irods.jargon.testutils.filemanip.FileGenerator;
import org.irods.jargon.testutils.filemanip.ScratchFileUtils;
import org.irodsext.datatyper.IrodsextDataTypeResolutionService;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Mike Conway - NIEHS
 *
 */
public class IrodsextDataProfilerServiceTest {

	private static Properties testingProperties = new Properties();
	private static TestingPropertiesHelper testingPropertiesHelper = new TestingPropertiesHelper();
	private static ScratchFileUtils scratchFileUtils = null;
	public static final String IRODS_TEST_SUBDIR_PATH = "IrodsextDataProfilerServiceTest";
	private static IRODSTestSetupUtilities irodsTestSetupUtilities = null;
	private static IRODSFileSystem irodsFileSystem;
	private static JargonProperties jargonOriginalProperties = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		TestingPropertiesHelper testingPropertiesLoader = new TestingPropertiesHelper();
		testingProperties = testingPropertiesLoader.getTestProperties();
		scratchFileUtils = new ScratchFileUtils(testingProperties);
		scratchFileUtils.clearAndReinitializeScratchDirectory(IRODS_TEST_SUBDIR_PATH);
		irodsTestSetupUtilities = new IRODSTestSetupUtilities();
		irodsTestSetupUtilities.initializeIrodsScratchDirectory();
		irodsTestSetupUtilities.initializeDirectoryForTest(IRODS_TEST_SUBDIR_PATH);
		irodsFileSystem = IRODSFileSystem.instance();
		SettableJargonProperties settableJargonProperties = new SettableJargonProperties(
				irodsFileSystem.getJargonProperties());
		jargonOriginalProperties = settableJargonProperties;
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		irodsFileSystem.closeAndEatExceptions();
	}

	@Before
	public void before() throws Exception {
		// be sure that normal config is set up
		irodsFileSystem.getIrodsSession().setJargonProperties(jargonOriginalProperties);
	}

	@After
	public void afterEach() throws Exception {
		irodsFileSystem.closeAndEatExceptions();
	}

	/**
	 * Test method for
	 * {@link org.irodsext.dataprofiler.IrodsextDataProfilerService#retrieveDataProfile(java.lang.String, org.irods.jargon.extensions.dataprofiler.DataProfilerSettings)}.
	 */
	@Test
	public void testBasicDataProfileWithCollection() throws Exception {
		IRODSAccount irodsAccount = testingPropertiesHelper.buildIRODSAccountFromTestProperties(testingProperties);
		IRODSAccessObjectFactory accessObjectFactory = irodsFileSystem.getIRODSAccessObjectFactory();
		String targetIrodsCollection = testingPropertiesHelper
				.buildIRODSCollectionAbsolutePathFromTestProperties(testingProperties, IRODS_TEST_SUBDIR_PATH);

		DataProfilerSettings dataProfilerSettings = new DataProfilerSettings();
		dataProfilerSettings.setDetectMimeAndInfoType(false);
		dataProfilerSettings.setRetrieveAcls(true);
		dataProfilerSettings.setRetrieveMetadata(true);
		dataProfilerSettings.setRetrieveReplicas(false);
		dataProfilerSettings.setRetrieveShared(false);
		dataProfilerSettings.setRetrieveStarred(false);
		dataProfilerSettings.setRetrieveTickets(false);
		DataTyperSettings dataTyperSettings = new DataTyperSettings();
		dataTyperSettings.setDetailedDetermination(false);
		dataTyperSettings.setPersistDataTypes(false);
		DataTypeResolutionService dataTyperService = new IrodsextDataTypeResolutionService(accessObjectFactory,
				irodsAccount, dataTyperSettings);

		DataProfilerService dataProfilerService = new IrodsextDataProfilerService(dataProfilerSettings,
				accessObjectFactory, irodsAccount);
		dataProfilerService.setDataTypeResolutionService(dataTyperService);
		@SuppressWarnings("rawtypes")
		DataProfile dataProfile = dataProfilerService.retrieveDataProfile(targetIrodsCollection);
		Assert.assertNotNull("null data profile returned", dataProfile);
		Assert.assertFalse("no acls", dataProfile.getAcls().isEmpty());
		Assert.assertFalse("no child name", dataProfile.getChildName().isEmpty());
		Assert.assertFalse("no parent path", dataProfile.getParentPath().isEmpty());
		Assert.assertFalse("no path components", dataProfile.getPathComponents().isEmpty());
		Assert.assertNotNull("no domain object", dataProfile.getDomainObject());

	}

	@Test
	public void testBasicDataProfileWithTextFileAndFileExtensionTyping() throws Exception {
		IRODSAccount irodsAccount = testingPropertiesHelper.buildIRODSAccountFromTestProperties(testingProperties);
		IRODSAccessObjectFactory accessObjectFactory = irodsFileSystem.getIRODSAccessObjectFactory();

		String testFileName = "testBasicDataProfileWithTextFileAndFileExtensionTyping.txt";
		String absPath = scratchFileUtils.createAndReturnAbsoluteScratchPath(IRODS_TEST_SUBDIR_PATH);
		String localFileName = FileGenerator.generateFileOfFixedLengthGivenName(absPath, testFileName, 1);

		File localFile = new File(localFileName);

		// now put the file
		String targetIrodsFile = testingPropertiesHelper
				.buildIRODSCollectionAbsolutePathFromTestProperties(testingProperties, IRODS_TEST_SUBDIR_PATH);

		IRODSFileFactory irodsFileFactory = accessObjectFactory.getIRODSFileFactory(irodsAccount);
		IRODSFile destFile = irodsFileFactory.instanceIRODSFile(targetIrodsFile);
		DataTransferOperations dataTransferOperations = accessObjectFactory.getDataTransferOperations(irodsAccount);
		dataTransferOperations.putOperation(localFile, destFile, null, null);
		String dataName = destFile.getAbsolutePath() + "/" + testFileName;

		DataProfilerSettings dataProfilerSettings = new DataProfilerSettings();
		dataProfilerSettings.setDetectMimeAndInfoType(true);
		dataProfilerSettings.setRetrieveAcls(true);
		dataProfilerSettings.setRetrieveMetadata(true);
		dataProfilerSettings.setRetrieveReplicas(false);
		dataProfilerSettings.setRetrieveShared(false);
		dataProfilerSettings.setRetrieveStarred(false);
		dataProfilerSettings.setRetrieveTickets(false);
		DataTyperSettings dataTyperSettings = new DataTyperSettings();
		dataTyperSettings.setDetailedDetermination(false);
		dataTyperSettings.setPersistDataTypes(false);
		DataTypeResolutionService dataTyperService = new IrodsextDataTypeResolutionService(accessObjectFactory,
				irodsAccount, dataTyperSettings);

		DataProfilerService dataProfilerService = new IrodsextDataProfilerService(dataProfilerSettings,
				accessObjectFactory, irodsAccount);
		dataProfilerService.setDataTypeResolutionService(dataTyperService);
		@SuppressWarnings("rawtypes")
		DataProfile dataProfile = dataProfilerService.retrieveDataProfile(dataName);
		Assert.assertNotNull("null data profile returned", dataProfile);
		Assert.assertFalse("no acls", dataProfile.getAcls().isEmpty());
		Assert.assertFalse("no child name", dataProfile.getChildName().isEmpty());
		Assert.assertFalse("no parent path", dataProfile.getParentPath().isEmpty());
		Assert.assertFalse("no path components", dataProfile.getPathComponents().isEmpty());
		Assert.assertNotNull("no domain object", dataProfile.getDomainObject());
		Assert.assertTrue("is not a file", dataProfile.isFile());
		Assert.assertNotNull("null data type", dataProfile.getDataType());

	}

}
