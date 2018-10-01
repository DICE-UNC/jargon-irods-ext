/* Copyright (c) 2018, University of North Carolina at Chapel Hill */
/* Copyright (c) 2015-2017, Dell EMC */

package com.emc.metalnx.services.tests.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.irods.jargon.core.exception.JargonException;
import org.irods.jargon.core.exception.OperationNotSupportedByThisServerException;
import org.irods.jargon.core.rule.IRODSRuleExecResultOutputParameter;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.emc.metalnx.core.domain.entity.DataGridResource;
import com.emc.metalnx.core.domain.entity.DataGridRule;
import com.emc.metalnx.core.domain.exceptions.DataGridConnectionRefusedException;
import com.emc.metalnx.core.domain.exceptions.DataGridException;
import com.emc.metalnx.core.domain.exceptions.DataGridRuleException;
import com.emc.metalnx.services.interfaces.CollectionService;
import com.emc.metalnx.services.interfaces.ConfigService;
import com.emc.metalnx.services.interfaces.IRODSServices;
import com.emc.metalnx.services.interfaces.ResourceService;
import com.emc.metalnx.services.interfaces.RuleService;
import com.emc.metalnx.services.irods.RuleServiceImpl;

/**
 * Test for Rule Service
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-services-context.xml")
@WebAppConfiguration
public class TestRuleService {

	private static final String RM_TRASH_RODS_ADMIN_FLAG = "irodsAdminRmTrash=";
	private static final String RM_TRASH_RODS_USER_FLAG = "irodsRmTrash=";
	private static final String HOST = "icat.test.com";
	private static final String RESOURCE = "demoResc";
	private static String msiVersion;

	@InjectMocks
	private RuleService ruleService;

	@Mock
	private CollectionService collectionService;

	@Mock
	private ResourceService resourceService;

	@Mock
	private IRODSServices irodsServices;

	@Mock
	private ConfigService configService;

	@PostConstruct
	public void init() {
		msiVersion = "1.1.0";
	}

	@Before
	public void setUp() throws JargonException, DataGridException, URISyntaxException, IOException {
		ruleService = spy(RuleServiceImpl.class); // partial mocking

		MockitoAnnotations.initMocks(this);

		when(configService.getIrodsHost()).thenReturn("icat.test.com");
		when(configService.getMsiAPIVersionSupported()).thenReturn(msiVersion);

		DataGridResource resc = new DataGridResource(1, RESOURCE, "zone", "unixfilesystem", "/test/resc/path");
		resc.setHost("icat.test.com");
		when(resourceService.find(anyString())).thenReturn(resc);
		when(ruleService.executeRule(anyString())).thenReturn(new HashMap<>());
	}

	@Test
	public void testEmptyTrashRuleInAdminMode() throws DataGridConnectionRefusedException, DataGridRuleException {
		ruleService.execEmptyTrashRule(RESOURCE, "/tempZone/home/rods", true);
		final ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		verify(ruleService, times(1)).executeRule(captor.capture());
		final String rule = captor.getValue();

		assertTrue(rule.contains(RM_TRASH_RODS_ADMIN_FLAG));
	}

	@Test
	public void testEmptyTrashRuleAsRodsUser() throws DataGridConnectionRefusedException, DataGridRuleException {
		ruleService.execEmptyTrashRule(RESOURCE, "/tempZone/home/rods", false);
		final ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		verify(ruleService, times(1)).executeRule(captor.capture());
		final String rule = captor.getValue();

		assertTrue(rule.contains(RM_TRASH_RODS_USER_FLAG));
	}

	@Test
	public void testIRODS42Rule() throws DataGridConnectionRefusedException {
		when(irodsServices.isAtLeastIrods420()).thenReturn(true);
		DataGridRule rule = new DataGridRule(DataGridRule.VCF_RULE, "icat.test.com", true);
		assertTrue(rule.declareRuleOutputParams());
	}

	@Test
	public void testRuleWithNoVariableDeclarationForIRODS420() throws DataGridConnectionRefusedException {
		DataGridRule rule = new DataGridRule(DataGridRule.ILLUMINA_RULE, "icat.test.com", false);
		rule.setInputRuleParams("param1", "param2");
		rule.setOutputRuleParams("output_param");
		assertFalse(rule.toString().contains("*output_param=\"\";"));
	}

	@Test
	public void testRuleWithVariableDeclarationForIRODS41X() throws DataGridConnectionRefusedException {
		DataGridRule rule = new DataGridRule(DataGridRule.ILLUMINA_RULE, "icat.test.com", false);
		rule.setInputRuleParams("param1", "param2");
		rule.setOutputRuleParams("output_param");
		assertFalse(rule.toString().contains("*output_param=\"\";"));
	}

	@Test
	public void testRuleWithInputParams() {
		DataGridRule rule = new DataGridRule(DataGridRule.VCF_RULE, "icat.test.com");
		rule.setInputRuleParams("param1", "param2");

		assertNotNull(rule.toString());
		assertTrue(rule.toString().contains("INPUT *p0=\"param1\", *p1=\"param2\""));
	}

	@Test
	public void testRuleWithOutputParams() {
		DataGridRule rule = new DataGridRule(DataGridRule.VCF_RULE, "icat.test.com");
		rule.setInputRuleParams("param1", "param2");
		rule.setOutputRuleParams("output_param");

		assertNotNull(rule.toString());
		assertTrue(rule.toString().contains("INPUT *p0=\"param1\", *p1=\"param2\""));
		assertTrue(rule.toString().contains("OUTPUT *output_param"));
	}

	@Ignore /* switched to jargon list msis */
	public void testGetMSIsRule() throws OperationNotSupportedByThisServerException, JargonException {
		Map<String, IRODSRuleExecResultOutputParameter> result = new HashMap<>();
		IRODSRuleExecResultOutputParameter output = new IRODSRuleExecResultOutputParameter();
		output.setOutputParamType(IRODSRuleExecResultOutputParameter.OutputParamType.STRING);
		output.setParameterName("*msis");
		output.setResultObject("libmsi1.so, libmsi2.so, libmsi3.so, libmsi4.so");
		result.put("*msis", output);

		when(ruleService.executeRule(anyString())).thenReturn(result);

		List<String> msis = ruleService.execGetMSIsRule("icat.test.com");
		assertNotNull(msis);
		assertFalse(msis.isEmpty());
		assertTrue(msis.contains("libmsi1.so"));
		assertTrue(msis.contains("libmsi2.so"));
		assertTrue(msis.contains("libmsi3.so"));
		assertTrue(msis.contains("libmsi4.so"));
	}

	@Ignore /* switched to jargon list msis */
	public void testGetMSIsRuleNoReturn() throws OperationNotSupportedByThisServerException, JargonException {
		Map<String, IRODSRuleExecResultOutputParameter> result = new HashMap<>();
		IRODSRuleExecResultOutputParameter output = new IRODSRuleExecResultOutputParameter();
		output.setOutputParamType(IRODSRuleExecResultOutputParameter.OutputParamType.STRING);
		output.setParameterName("*msis");
		output.setResultObject(null);
		result.put("*msis", output);

		when(ruleService.executeRule(anyString())).thenReturn(result);

		List<String> msis = ruleService.execGetMSIsRule(HOST);
		assertNotNull(msis);
		assertTrue(msis.isEmpty());
	}

	@Test
	public void testGetVersionRule() throws DataGridRuleException, DataGridConnectionRefusedException {
		Map<String, IRODSRuleExecResultOutputParameter> result = new HashMap<>();
		IRODSRuleExecResultOutputParameter output = new IRODSRuleExecResultOutputParameter();
		output.setOutputParamType(IRODSRuleExecResultOutputParameter.OutputParamType.STRING);
		output.setParameterName("*version");
		output.setResultObject(msiVersion);
		result.put("*version", output);

		when(ruleService.executeRule(anyString())).thenReturn(result);

		String version = ruleService.execGetVersionRule("icat.test.com");
		assertEquals(msiVersion, version);
	}

	@Test
	public void testReplicateObjRule() throws DataGridRuleException, DataGridConnectionRefusedException {
		String path = "this/is/a/path";

		ruleService.execReplDataObjRule(RESOURCE, path, false);

		verify(resourceService, times(1)).find(RESOURCE);
		verify(ruleService, times(1)).executeRule(anyString());
	}

	@Test
	public void testReplicateObjRuleInAdminMode() throws DataGridRuleException, DataGridConnectionRefusedException {
		String path = "this/is/a/path";

		ruleService.execReplDataObjRule(RESOURCE, path, true);

		verify(resourceService, times(1)).find(RESOURCE);
		verify(ruleService, times(1)).executeRule(anyString());
	}

	@Test
	public void testPopulateMetadataRule() throws DataGridRuleException, DataGridConnectionRefusedException {
		when(configService.isPopulateMsiEnabled()).thenReturn(true);
		ruleService.execPopulateMetadataRule(HOST, "/testZone/home/rods");
		verify(ruleService, times(1)).executeRule(anyString());
	}

	@Test
	public void testImageRule() throws DataGridRuleException, DataGridConnectionRefusedException {
		ruleService.execImageRule(HOST, "/zone/home/rods/test.jpg", "/var/lib/irods/test.jpg");
		verify(ruleService, times(1)).executeRule(anyString());
	}

	@Test
	public void testVCFMetadataRule() throws DataGridRuleException, DataGridConnectionRefusedException {
		ruleService.execVCFMetadataRule(HOST, "/zone/home/rods/test.vcf", "/var/lib/irods/test.vcf");
		verify(ruleService, times(1)).executeRule(anyString());
	}

	@Test
	public void testBamCramMetadataRule() throws DataGridRuleException, DataGridConnectionRefusedException {
		ruleService.execBamCramMetadataRule(HOST, "/zone/home/rods/test.bam", "/var/lib/irods/test.bam");
		verify(ruleService, times(1)).executeRule(anyString());
	}

	@Test
	public void testManifestFileRule() throws Exception {
		when(collectionService.getSubCollectionsAndDataObjectsUnderPath(anyString())).thenReturn(new ArrayList<>());
		ruleService.execManifestFileRule(HOST, "/zone/home/rods", "/zone/home/rods/test.xml",
				"/var/lib/irods/test.xml");
		verify(ruleService, atMost(1)).executeRule(anyString());
	}

	@Test
	public void testIlluminaMetadataRule() throws DataGridRuleException, DataGridConnectionRefusedException {
		DataGridResource dgDestResc = resourceService.find(RESOURCE);
		ruleService.execIlluminaMetadataRule(dgDestResc, "/zone/home/rods", "/zone/home/rods/test_SSU.tar");

		// these two methods should never be called since there is no objects under the
		// test path
		verify(ruleService, atMost(2)).executeRule(anyString());
	}

	@Test
	public void testDeploymentRule() throws DataGridConnectionRefusedException, DataGridRuleException {
		when(irodsServices.isAtLeastIrods420()).thenReturn(true);

		String ruleName = "test_deployment_rule.re";
		String ruleVaultPath = String.format("/var/lib/irods/Vault/.rulecache/%s", ruleName);
		ruleService.execDeploymentRule(configService.getIrodsHost(), ruleName, ruleVaultPath);
		verify(ruleService, times(1)).executeRule(anyString());
	}

	@Test
	public void testDeploymentRuleNotIRods42() throws DataGridConnectionRefusedException, DataGridRuleException {
		when(irodsServices.isAtLeastIrods420()).thenReturn(false);

		String ruleName = "test_deployment_rule.re";
		String ruleVaultPath = String.format("/var/lib/irods/Vault/.rulecache/%s", ruleName);
		ruleService.execDeploymentRule(configService.getIrodsHost(), ruleName, ruleVaultPath);
		verify(ruleService, never()).executeRule(anyString());
	}

	@Test(expected = DataGridRuleException.class)
	public void testDeploymentRuleException() throws DataGridConnectionRefusedException, DataGridRuleException {
		when(irodsServices.isAtLeastIrods420()).thenReturn(true);
		doThrow(DataGridRuleException.class).when(ruleService).executeRule(anyString());

		String ruleName = "test_deployment_rule.re";
		String ruleVaultPath = String.format("/var/lib/irods/Vault/.rulecache/%s", ruleName);
		ruleService.execDeploymentRule(configService.getIrodsHost(), ruleName, ruleVaultPath);
		verify(ruleService, times(1)).executeRule(anyString());
	}
}
