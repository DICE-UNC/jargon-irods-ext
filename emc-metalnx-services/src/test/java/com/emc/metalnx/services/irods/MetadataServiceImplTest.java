package com.emc.metalnx.services.irods;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.pub.DataObjectAO;
import org.irods.jargon.core.pub.DataTransferOperations;
import org.irods.jargon.core.pub.IRODSFileSystem;
import org.irods.jargon.core.pub.domain.AvuData;
import org.irods.jargon.testutils.IRODSTestSetupUtilities;
import org.irods.jargon.testutils.TestingPropertiesHelper;
import org.irods.jargon.testutils.filemanip.FileGenerator;
import org.irods.jargon.testutils.filemanip.ScratchFileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.emc.metalnx.core.domain.entity.DataGridCollectionAndDataObject;
import com.emc.metalnx.core.domain.entity.DataGridMetadataSearch;
import com.emc.metalnx.core.domain.entity.DataGridPageContext;
import com.emc.metalnx.core.domain.entity.enums.DataGridSearchOperatorEnum;
import com.emc.metalnx.services.configuration.ConfigServiceImpl;
import com.emc.metalnx.services.interfaces.AdminServices;
import com.emc.metalnx.services.interfaces.ConfigService;
import com.emc.metalnx.services.interfaces.IRODSServices;
import com.emc.metalnx.services.interfaces.MetadataService;
import com.emc.metalnx.services.interfaces.SpecQueryService;

public class MetadataServiceImplTest {

	private static Properties testingProperties = new Properties();
	private static TestingPropertiesHelper testingPropertiesHelper = new TestingPropertiesHelper();
	private static ScratchFileUtils scratchFileUtils = null;
	public static final String IRODS_TEST_SUBDIR_PATH = "MetadataServiceImplTest";
	private static IRODSTestSetupUtilities irodsTestSetupUtilities = null;
	private static IRODSFileSystem irodsFileSystem;

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
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		irodsFileSystem.closeAndEatExceptions();
	}

	@After
	public void afterEach() throws Exception {
		irodsFileSystem.closeAndEatExceptions();
	}

	@Test // https://github.com/DICE-UNC/jargon-irods-ext/issues/22
	public void testFindByMetadataNoReplica() throws Exception {
		String testFileName = "testFindByMetadataNoReplica.dat";
		String absPath = scratchFileUtils.createAndReturnAbsoluteScratchPath(IRODS_TEST_SUBDIR_PATH);
		String localFileName = FileGenerator.generateFileOfFixedLengthGivenName(absPath, testFileName, 10);

		IRODSAccount irodsAccount = testingPropertiesHelper.buildIRODSAccountFromTestProperties(testingProperties);

		// put scratch file into irods in the right place on the first resource

		String targetIrodsCollection = testingPropertiesHelper
				.buildIRODSCollectionAbsolutePathFromTestProperties(testingProperties, IRODS_TEST_SUBDIR_PATH);

		String dataObjectAbsPath = targetIrodsCollection + '/' + testFileName;

		DataTransferOperations dto = irodsFileSystem.getIRODSAccessObjectFactory()
				.getDataTransferOperations(irodsAccount);
		dto.putOperation(localFileName, targetIrodsCollection, irodsAccount.getDefaultStorageResource(), null, null);

		DataObjectAO dataObjectAO = irodsFileSystem.getIRODSAccessObjectFactory().getDataObjectAO(irodsAccount);

		// initialize the AVU data
		String expectedAttribName = "testFindByMetadataNoReplica-attrib" + System.currentTimeMillis();
		String expectedAttribValue = "testFindByMetadataNoReplica-val" + System.currentTimeMillis();
		String expectedAttribUnits = "testFindByMetadataNoReplica-unit" + System.currentTimeMillis();

		AvuData avuData = AvuData.instance(expectedAttribName, expectedAttribValue, expectedAttribUnits);

		dataObjectAO.deleteAVUMetadata(dataObjectAbsPath, avuData);
		dataObjectAO.addAVUMetadata(dataObjectAbsPath, avuData);

		IRODSServices irodsServices = new IRODSServicesImpl(irodsAccount);
		irodsServices.setIrodsAccessObjectFactory(irodsFileSystem.getIRODSAccessObjectFactory());
		AdminServices adminServices = new AdminServicesImpl();
		ConfigService configService = new ConfigServiceImpl();
		configService.setIrodsHost(irodsAccount.getHost());
		configService.setIrodsZone(irodsAccount.getZone());
		configService.setIrodsPort(String.valueOf(irodsAccount.getPort()));
		adminServices.setConfigService(configService);
		adminServices.setIrodsAccessObjectFactory(irodsFileSystem.getIRODSAccessObjectFactory());
		adminServices.setIrodsAccount(irodsAccount);

		SpecQueryService specQueryService = new SpecQueryServiceImpl();
		specQueryService.setIrodsServices(irodsServices);
		specQueryService.setAdminServices(adminServices);
		MetadataService metadataService = new MetadataServiceImpl();
		metadataService.setIrodsServices(irodsServices);
		metadataService.setSpecQueryService(specQueryService);

		List<DataGridMetadataSearch> searchList = new ArrayList<>();
		searchList.add(new DataGridMetadataSearch(expectedAttribName, expectedAttribValue, "",
				DataGridSearchOperatorEnum.LIKE));
		DataGridPageContext pageContext = new DataGridPageContext();
		List<DataGridCollectionAndDataObject> actual = metadataService.findByMetadata(searchList, pageContext, 0, 5000);
		Assert.assertNotNull("no results", actual);
	}

	@Test // replicates https://github.com/DICE-UNC/jargon-irods-ext/issues/22
	public void testFindByMetadataWithReplica() throws Exception {
		String testFileName = "testFindByMetadataWithReplica.dat";
		String absPath = scratchFileUtils.createAndReturnAbsoluteScratchPath(IRODS_TEST_SUBDIR_PATH);
		String localFileName = FileGenerator.generateFileOfFixedLengthGivenName(absPath, testFileName, 10);

		IRODSAccount irodsAccount = testingPropertiesHelper.buildIRODSAccountFromTestProperties(testingProperties);

		// put scratch file into irods in the right place on the first resource

		String targetIrodsCollection = testingPropertiesHelper
				.buildIRODSCollectionAbsolutePathFromTestProperties(testingProperties, IRODS_TEST_SUBDIR_PATH);

		String dataObjectAbsPath = targetIrodsCollection + '/' + testFileName;

		DataTransferOperations dto = irodsFileSystem.getIRODSAccessObjectFactory()
				.getDataTransferOperations(irodsAccount);
		dto.putOperation(localFileName, targetIrodsCollection, irodsAccount.getDefaultStorageResource(), null, null);

		DataObjectAO dataObjectAO = irodsFileSystem.getIRODSAccessObjectFactory().getDataObjectAO(irodsAccount);

		// initialize the AVU data
		String expectedAttribName = "testFindByMetadataWithReplica-attrib" + System.currentTimeMillis();
		String expectedAttribValue = "testFindByMetadataWithReplica-val" + System.currentTimeMillis();
		String expectedAttribUnits = "testFindByMetadataWithReplica-unit" + System.currentTimeMillis();

		AvuData avuData = AvuData.instance(expectedAttribName, expectedAttribValue, expectedAttribUnits);

		dataObjectAO.deleteAVUMetadata(dataObjectAbsPath, avuData);
		dataObjectAO.addAVUMetadata(dataObjectAbsPath, avuData);

		dataObjectAO.replicateIrodsDataObject(dataObjectAbsPath,
				testingProperties.getProperty(TestingPropertiesHelper.IRODS_SECONDARY_RESOURCE_KEY));

		IRODSServices irodsServices = new IRODSServicesImpl(irodsAccount);
		irodsServices.setIrodsAccessObjectFactory(irodsFileSystem.getIRODSAccessObjectFactory());
		AdminServices adminServices = new AdminServicesImpl();
		ConfigService configService = new ConfigServiceImpl();
		configService.setIrodsHost(irodsAccount.getHost());
		configService.setIrodsZone(irodsAccount.getZone());
		configService.setIrodsPort(String.valueOf(irodsAccount.getPort()));
		adminServices.setConfigService(configService);
		adminServices.setIrodsAccessObjectFactory(irodsFileSystem.getIRODSAccessObjectFactory());
		adminServices.setIrodsAccount(irodsAccount);

		SpecQueryService specQueryService = new SpecQueryServiceImpl();
		specQueryService.setIrodsServices(irodsServices);
		specQueryService.setAdminServices(adminServices);
		MetadataService metadataService = new MetadataServiceImpl();
		metadataService.setIrodsServices(irodsServices);
		metadataService.setSpecQueryService(specQueryService);

		List<DataGridMetadataSearch> searchList = new ArrayList<>();
		searchList.add(new DataGridMetadataSearch(expectedAttribName, expectedAttribValue, "",
				DataGridSearchOperatorEnum.LIKE));
		DataGridPageContext pageContext = new DataGridPageContext();
		List<DataGridCollectionAndDataObject> actual = metadataService.findByMetadata(searchList, pageContext, 0,
				Integer.MAX_VALUE);
		Assert.assertNotNull("no results", actual);
	}

	@Test // replicates https://github.com/DICE-UNC/jargon-irods-ext/issues/22
	public void testFindByMetadataWithReplicaMaxValue() throws Exception {
		String testFileName = "testFindByMetadataWithReplicaMaxVal.dat";
		String absPath = scratchFileUtils.createAndReturnAbsoluteScratchPath(IRODS_TEST_SUBDIR_PATH);
		String localFileName = FileGenerator.generateFileOfFixedLengthGivenName(absPath, testFileName, 10);

		IRODSAccount irodsAccount = testingPropertiesHelper.buildIRODSAccountFromTestProperties(testingProperties);

		// put scratch file into irods in the right place on the first resource

		String targetIrodsCollection = testingPropertiesHelper
				.buildIRODSCollectionAbsolutePathFromTestProperties(testingProperties, IRODS_TEST_SUBDIR_PATH);

		String dataObjectAbsPath = targetIrodsCollection + '/' + testFileName;

		DataTransferOperations dto = irodsFileSystem.getIRODSAccessObjectFactory()
				.getDataTransferOperations(irodsAccount);
		dto.putOperation(localFileName, targetIrodsCollection, irodsAccount.getDefaultStorageResource(), null, null);

		DataObjectAO dataObjectAO = irodsFileSystem.getIRODSAccessObjectFactory().getDataObjectAO(irodsAccount);

		// initialize the AVU data
		String expectedAttribName = "testFindByMetadataWithReplicaMaxVal-attrib" + System.currentTimeMillis();
		String expectedAttribValue = "testFindByMetadataWithReplicaMaxVal-val" + System.currentTimeMillis();
		String expectedAttribUnits = "testFindByMetadataWithReplicaMaxVal-unit" + System.currentTimeMillis();

		AvuData avuData = AvuData.instance(expectedAttribName, expectedAttribValue, expectedAttribUnits);

		dataObjectAO.deleteAVUMetadata(dataObjectAbsPath, avuData);
		dataObjectAO.addAVUMetadata(dataObjectAbsPath, avuData);

		dataObjectAO.replicateIrodsDataObject(dataObjectAbsPath,
				testingProperties.getProperty(TestingPropertiesHelper.IRODS_SECONDARY_RESOURCE_KEY));

		IRODSServices irodsServices = new IRODSServicesImpl(irodsAccount);
		irodsServices.setIrodsAccessObjectFactory(irodsFileSystem.getIRODSAccessObjectFactory());
		AdminServices adminServices = new AdminServicesImpl();
		ConfigService configService = new ConfigServiceImpl();
		configService.setIrodsHost(irodsAccount.getHost());
		configService.setIrodsZone(irodsAccount.getZone());
		configService.setIrodsPort(String.valueOf(irodsAccount.getPort()));
		adminServices.setConfigService(configService);
		adminServices.setIrodsAccessObjectFactory(irodsFileSystem.getIRODSAccessObjectFactory());
		adminServices.setIrodsAccount(irodsAccount);

		SpecQueryService specQueryService = new SpecQueryServiceImpl();
		specQueryService.setIrodsServices(irodsServices);
		specQueryService.setAdminServices(adminServices);
		MetadataService metadataService = new MetadataServiceImpl();
		metadataService.setIrodsServices(irodsServices);
		metadataService.setSpecQueryService(specQueryService);

		List<DataGridMetadataSearch> searchList = new ArrayList<>();
		searchList.add(new DataGridMetadataSearch(expectedAttribName, expectedAttribValue, "",
				DataGridSearchOperatorEnum.LIKE));
		DataGridPageContext pageContext = new DataGridPageContext();
		List<DataGridCollectionAndDataObject> actual = metadataService.findByMetadata(searchList, pageContext, 0, 5000);
		Assert.assertNotNull("no results", actual);
	}

}
