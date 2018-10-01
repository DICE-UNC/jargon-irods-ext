 /* Copyright (c) 2018, University of North Carolina at Chapel Hill */
 /* Copyright (c) 2015-2017, Dell EMC */
 


package com.emc.metalnx.services.tests.upload;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.emc.metalnx.core.domain.entity.DataGridCollectionAndDataObject;
import com.emc.metalnx.core.domain.exceptions.DataGridConnectionRefusedException;
import com.emc.metalnx.core.domain.exceptions.DataGridException;
import com.emc.metalnx.core.domain.exceptions.DataGridFileAlreadyExistsException;
import com.emc.metalnx.services.interfaces.CollectionService;
import com.emc.metalnx.services.interfaces.FileOperationService;
import com.emc.metalnx.services.interfaces.UploadService;

/**
 * Test Upload.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-services-context.xml")
@WebAppConfiguration
public class TestUploadService {

	public static final String TEST_FILE_NAME = "test.txt";
	public static final String RESOURCE = "demoResc";

	@Value("${irods.zoneName}")
	private String zone;

	@Value("${jobs.irods.username}")
	private String user;

	@Autowired
	private UploadService us;

	@Autowired
	private FileOperationService fs;

	@Autowired
	private CollectionService cs;

	private String targetPath;

	@Before
	public void setUp() throws DataGridConnectionRefusedException {
		targetPath = String.format("/%s/home/%s", zone, user);
		fs.deleteItem(targetPath + "/" + TEST_FILE_NAME, true);
	}

	@After
	public void tearDown() throws DataGridConnectionRefusedException {
		fs.deleteItem(targetPath + "/" + TEST_FILE_NAME, true);
	}

	@Test
	public void testDirectTransfer() throws Exception {
		boolean fileUploaded = false;

		MockMultipartFile file = new MockMultipartFile(TEST_FILE_NAME, "Hello World".getBytes());

		us.upload(file, targetPath, false, false, "", RESOURCE, false);

		List<DataGridCollectionAndDataObject> items = cs.getSubCollectionsAndDataObjectsUnderPath(targetPath);

		for (DataGridCollectionAndDataObject item : items) {
			if (TEST_FILE_NAME.equals(item.getName())) {
				fileUploaded = true;
				break;
			}
		}

		assertTrue(fileUploaded);
	}

	@Test(expected = DataGridFileAlreadyExistsException.class)
	public void testExistingFileTransfer() throws DataGridException {
		MockMultipartFile file = new MockMultipartFile(TEST_FILE_NAME, "Hello World".getBytes());

		us.upload(file, targetPath, false, false, "", RESOURCE, false);

		// should raise an exception
		us.upload(file, targetPath, false, false, "", RESOURCE, false);
	}
}
