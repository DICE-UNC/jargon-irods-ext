 /* Copyright (c) 2018, University of North Carolina at Chapel Hill */
 /* Copyright (c) 2015-2017, Dell EMC */
 


package com.emc.metalnx.services.tests.rules;

import com.emc.metalnx.core.domain.entity.DataGridCollectionAndDataObject;
import com.emc.metalnx.core.domain.exceptions.DataGridConnectionRefusedException;
import com.emc.metalnx.core.domain.exceptions.DataGridException;
import com.emc.metalnx.core.domain.exceptions.DataGridRuleException;
import com.emc.metalnx.services.interfaces.*;
import com.emc.metalnx.services.irods.ResourceServiceImpl;
import com.emc.metalnx.services.irods.RuleDeploymentServiceImpl;
import org.irods.jargon.core.exception.JargonException;
import org.irods.jargon.core.pub.io.IRODSFile;
import org.irods.jargon.core.pub.io.IRODSFileFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Test for Rule Service
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-services-context.xml")
@WebAppConfiguration
public class TestRuleDeploymentService {
    private static final String TEST_RULE_NAME = "test_rule_deployment.re";
    private static final String RULE_CACHE_DIR = ".rulecache";
    public static final String RULE_FILE_EXTENSION = ".re";

    @Autowired
    private CollectionService collectionService;

    @Autowired
    private IRODSServices irodsServices;

    @Autowired
    private ConfigService configService;

    @Spy
    private ResourceService resourceService = new ResourceServiceImpl();

    @Mock
    private RuleService ruleService;

    @InjectMocks
    private RuleDeploymentService ruleDeploymentService;

    private MockMultipartFile file;
    private String ruleCachePath;

    @Before
    public void setUp() throws DataGridRuleException, DataGridConnectionRefusedException, JargonException {
        ruleCachePath = String.format("/%s/%s", configService.getIrodsZone(), RULE_CACHE_DIR);

        ruleDeploymentService = spy(RuleDeploymentServiceImpl.class); // partial mocking

        MockitoAnnotations.initMocks(this);

        ReflectionTestUtils.setField(ruleDeploymentService, "irodsServices", irodsServices);
        ReflectionTestUtils.setField(ruleDeploymentService, "configService", configService);

        removeRuleCacheColl();
        createRuleCacheColl();

        file = new MockMultipartFile(TEST_RULE_NAME, "Hello World".getBytes());

        when(resourceService.find(anyString())).thenCallRealMethod();
        when(ruleService.executeRule(anyString())).thenReturn(new HashMap<>());
    }

    @After
    public void tearDown() throws DataGridConnectionRefusedException, JargonException {
        removeRuleCacheColl();
    }

    @Test
    public void testDeployRule() throws DataGridException {
        ArgumentCaptor<String> argCaptor = ArgumentCaptor.forClass(String.class);
        ruleDeploymentService.deployRule(file);
        verify(ruleService).execDeploymentRule(argCaptor.capture(), argCaptor.capture(), argCaptor.capture());
        assertFalse(argCaptor.getAllValues().get(1).endsWith(RULE_FILE_EXTENSION));

        List<DataGridCollectionAndDataObject> items =
                collectionService.getSubCollectionsAndDataObjetsUnderPath(ruleCachePath);

        boolean ruleInCache = false;
        for(DataGridCollectionAndDataObject item: items) {
            if(TEST_RULE_NAME.equals(item.getName())) {
                ruleInCache = true;
                break;
            }
        }

        assertTrue(ruleInCache);
    }

    /**
     * Create the rule cache collection in the grid
     * @throws DataGridConnectionRefusedException
     * @throws JargonException
     */
    private void createRuleCacheColl() throws DataGridConnectionRefusedException, JargonException {
        IRODSFileFactory iff = irodsServices.getIRODSFileFactory();
        IRODSFile ruleCacheColl = iff.instanceIRODSFile("/" + configService.getIrodsZone(), RULE_CACHE_DIR);
        irodsServices.getIRODSFileSystemAO().mkdir(ruleCacheColl, false);
    }

    /**
     * Removes the rule cache collection from the grid
     * @throws JargonException
     * @throws DataGridConnectionRefusedException
     */
    private void removeRuleCacheColl() throws JargonException, DataGridConnectionRefusedException {
        IRODSFile collectionToBeRemoved = irodsServices.getIRODSFileFactory().instanceIRODSFile(ruleCachePath);
        if(collectionToBeRemoved.exists()) {
            irodsServices.getIRODSFileSystemAO().directoryDeleteForce(collectionToBeRemoved);
        }
    }
}
