 /* Copyright (c) 2018, University of North Carolina at Chapel Hill */
 /* Copyright (c) 2015-2017, Dell EMC */
 


package com.emc.metalnx.services.tests.metadata;

import com.emc.metalnx.core.domain.entity.DataGridCollectionAndDataObject;
import com.emc.metalnx.core.domain.entity.DataGridMetadata;
import com.emc.metalnx.core.domain.exceptions.DataGridConnectionRefusedException;
import com.emc.metalnx.core.domain.exceptions.DataGridException;
import com.emc.metalnx.services.interfaces.CollectionService;
import com.emc.metalnx.services.interfaces.FileOperationService;
import com.emc.metalnx.services.interfaces.MetadataService;
import com.emc.metalnx.services.interfaces.UploadService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static junit.framework.Assert.assertTrue;

/**
 * Test metadata service.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-services-context.xml")
@WebAppConfiguration
public class TestAddMetadataToObjs {
    private static final String BASE_FILE_NAME = "test-file-transfer-";
    private static final String RESOURCE = "demoResc";
    private static final int NUMBER_OF_FILES = 3;
    private static final int NUMBER_OF_METADATA_TAGS = 3;

    @Value("${irods.zoneName}")
    private String zone;

    @Value("${jobs.irods.username}")
    private String username;

    @Autowired
    private MetadataService metadataService;

    @Autowired
    private UploadService us;

    @Autowired
    private CollectionService cs;

    @Autowired
    private FileOperationService fos;

    private String parentPath, srcPath, dstPath;

    private List<String> expectedMetadataList;

    private long time;

    @Before
    public void setUp() throws DataGridException {
        time = System.currentTimeMillis();

        parentPath = String.format("/%s/home/%s", zone, username);
        srcPath = String.format("%s/test-metadata-transfer-%d", parentPath, time);
        dstPath = String.format("%s/dst-test-metadata-transfer-%d", parentPath, time);

        fos.deleteCollection(srcPath, true);
        fos.deleteCollection(dstPath, true);

        cs.createCollection(new DataGridCollectionAndDataObject(srcPath, parentPath, true));
        cs.createCollection(new DataGridCollectionAndDataObject(dstPath, parentPath, true));

        expectedMetadataList = MetadataUtils.createRandomMetadataAsString(NUMBER_OF_METADATA_TAGS);

        MockMultipartFile file;
        for(int i = 0; i < NUMBER_OF_FILES; i++) {
            String filename = BASE_FILE_NAME + i;
            String fileSrcPath = String.format("%s/%s", srcPath, filename);

            file = new MockMultipartFile(filename, "Hello World Transfer".getBytes());
            fos.deleteDataObject(fileSrcPath, true);
            us.upload(file, srcPath, false, false, "", RESOURCE, false);

            for(String metadataStr: expectedMetadataList) {
                String[] metadata = metadataStr.split(" ");
                String attr = metadata[0], val = metadata[1], unit = metadata[2];
                metadataService.addMetadataToPath(fileSrcPath, attr, val, unit);
            }
        }
    }

    @After
    public void tearDown() throws DataGridException {
        fos.deleteCollection(srcPath, true);
        fos.deleteCollection(dstPath, true);
    }

    @Test
    public void testAddMetadataToObj() throws DataGridConnectionRefusedException {
        for (int i = 0; i < NUMBER_OF_FILES; i++) {
            String filename = BASE_FILE_NAME + i;
            String filePath = String.format("%s/%s", srcPath, filename);
            assertMetadataInPath(filePath);
        }
    }

    private void assertMetadataInPath(String path) throws DataGridConnectionRefusedException {
        List<DataGridMetadata> actualMetadataList = metadataService.findMetadataValuesByPath(path);

        Assert.assertEquals(expectedMetadataList.size(), actualMetadataList.size());

        for (DataGridMetadata m: actualMetadataList) {
            String metadataStr = m.getAttribute() + " " + m.getValue() + " " + m.getUnit();
            assertTrue(expectedMetadataList.contains(metadataStr));
        }
    }
}
