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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test copying collections with metadata.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-services-context.xml")
@WebAppConfiguration
public class TestCopyCollWithMetadata {
    private static final String BASE_COLL_NAME = "test-coll-transfer-";
    private static final int NUMBER_OF_COLLS = 3;

    @Value("${irods.zoneName}")
    private String zone;

    @Value("${jobs.irods.username}")
    private String username;

    @Autowired
    private MetadataService metadataService;

    @Autowired
    private CollectionService cs;

    @Autowired
    private FileOperationService fos;

    private String parentPath, srcPath, dstPath;

    private List<String> expectedMetadataList;

    @Before
    public void setUp() throws DataGridException {
        parentPath = String.format("/%s/home/%s", zone, username);
        srcPath = String.format("%s/test-metadata-transfer", parentPath);
        dstPath = String.format("%s/dst-test-metadata-transfer", parentPath);

        fos.deleteCollection(srcPath, true);
        fos.deleteCollection(dstPath, true);

        cs.createCollection(new DataGridCollectionAndDataObject(srcPath, parentPath, true));
        cs.createCollection(new DataGridCollectionAndDataObject(dstPath, parentPath, true));

        expectedMetadataList = new ArrayList<>();
        expectedMetadataList.add("attr1 val1 unit1");
        expectedMetadataList.add("attr2 val2 unit2");
        expectedMetadataList.add("attr3 val3 unit3");

        for(int i = 0; i < NUMBER_OF_COLLS; i++) {
            String collname = BASE_COLL_NAME + i;
            String collSrcPath = String.format("%s/%s", srcPath, collname);
            cs.createCollection(new DataGridCollectionAndDataObject(collSrcPath, srcPath, true));

            for(String s: expectedMetadataList) {
                String[] metadata = s.split(" ");
                String attr = metadata[0], val = metadata[1], unit = metadata[2];
                metadataService.addMetadataToPath(collSrcPath, attr, val, unit);
            }
        }
    }

    @After
    public void tearDown() throws DataGridException {
        fos.deleteCollection(srcPath, true);
        fos.deleteCollection(dstPath, true);
    }

    @Test
    public void testCopyOneFileWithMetadata() throws DataGridException {
        String collname = BASE_COLL_NAME + "0";
        String collSrcPath = String.format("%s/%s", srcPath, collname);
        fos.copy(collSrcPath, dstPath, true);
        String collDstPath = String.format("%s/%s", dstPath, collname);
        assertMetadataInPath(collDstPath);
    }

    @Test
    public void testCopyOneFileWithoutMetadata() throws DataGridException {
        String collname = BASE_COLL_NAME + "0";
        String collSrcPath = String.format("%s/%s", srcPath, collname);
        fos.copy(collSrcPath, dstPath, false);
        String collDstPath = String.format("%s/%s", dstPath, collname);
        assertTrue(metadataService.findMetadataValuesByPath(collDstPath).isEmpty());
    }

    @Test
    public void testCopySeveralFilesWithMetadata() throws  DataGridException {
        for(int i = 0; i < NUMBER_OF_COLLS; i++) {
            String collname = BASE_COLL_NAME + i;
            String collSrcPath = String.format("%s/%s", srcPath, collname);
            fos.copy(collSrcPath, dstPath, true);
            String collDstPath = String.format("%s/%s", dstPath, collname);
            assertMetadataInPath(collDstPath);
        }
    }

    @Test
    public void testCopySeveralFilesWithoutMetadata() throws  DataGridException {
        for(int i = 0; i < NUMBER_OF_COLLS; i++) {
            String collname = BASE_COLL_NAME + i;
            String collSrcPath = String.format("%s/%s", srcPath, collname);
            fos.copy(collSrcPath, dstPath, false);
            String collDstPath = String.format("%s/%s", dstPath, collname);
            assertTrue(metadataService.findMetadataValuesByPath(collDstPath).isEmpty());
        }
    }

    private void assertMetadataInPath(String path) throws DataGridConnectionRefusedException {
        List<DataGridMetadata> actualMetadataList = metadataService.findMetadataValuesByPath(path);

        assertEquals(expectedMetadataList.size(), actualMetadataList.size());

        for (DataGridMetadata m: actualMetadataList) {
            String metadataStr = m.getAttribute() + " " + m.getValue() + " " + m.getUnit();
            assertTrue(expectedMetadataList.contains(metadataStr));
        }
    }
}
