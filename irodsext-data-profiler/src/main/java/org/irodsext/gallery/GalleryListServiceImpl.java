/**
 * 
 */
package org.irodsext.gallery;

import java.util.ArrayList;
import java.util.List;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.exception.JargonException;
import org.irods.jargon.core.pub.IRODSAccessObjectFactory;
import org.irods.jargon.core.pub.RuleProcessingAO;
import org.irods.jargon.core.rule.IRODSRuleParameter;
import org.irods.jargon.core.rule.RuleInvocationConfiguration;
import org.irods.jargon.core.service.AbstractJargonService;
import org.irods.jargon.core.utils.LocalFileUtils;
import org.irods.jargon.extensions.thumbnail.GalleryListService;
import org.irods.jargon.extensions.thumbnail.ThumbnailList;
import org.irodsext.dataprofiler.IrodsextDataProfilerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author conwaymc
 *
 */
public class GalleryListServiceImpl extends AbstractJargonService implements GalleryListService {

	public static final Logger log = LoggerFactory.getLogger(IrodsextDataProfilerService.class);
	private final ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * Default (no values) constructor
	 */
	public GalleryListServiceImpl() {
		super();
	}

	/**
	 * @param irodsAccessObjectFactory {@link IRODSAccessObjectFactory}
	 * @param irodsAccount             {@link IRODSAccount}
	 */
	public GalleryListServiceImpl(IRODSAccessObjectFactory irodsAccessObjectFactory, IRODSAccount irodsAccount) {
		super(irodsAccessObjectFactory, irodsAccount);
	}

	@Override
	public ThumbnailList list(String irodsFileAbsolutePath, int offset, int length) throws JargonException {
		log.info("list()");

		if (irodsFileAbsolutePath == null || irodsFileAbsolutePath.isEmpty()) {
			throw new IllegalArgumentException("null or empty irodsFileAbsolutePath");
		}

		if (offset < 0) {
			throw new IllegalArgumentException("offset cannot be < 0");
		}

		if (length <= 0) {
			throw new IllegalArgumentException("length cannot be <= 0");
		}

		log.info("irodsFileAbsolutePath:{}", irodsFileAbsolutePath);
		log.info("offset:{}", offset);
		log.info("length:{}", length);

		List<IRODSRuleParameter> irodsRuleParameters = new ArrayList<>();
		irodsRuleParameters.add(new IRODSRuleParameter("absPath", irodsFileAbsolutePath));
		irodsRuleParameters.add(new IRODSRuleParameter("offset", offset));
		irodsRuleParameters.add(new IRODSRuleParameter("length", length));
		RuleInvocationConfiguration ruleInvocationConfiguration = RuleInvocationConfiguration
				.instanceWithDefaultAutoSettings();

		RuleProcessingAO ruleProcessingAO = this.getIrodsAccessObjectFactory().getRuleProcessingAO(getIrodsAccount());
		// IRODSRuleExecResult result =
		// ruleProcessingAO.executeRuleFromResource("/rules/call_gallery_list.r",
		// irodsRuleParameters, ruleInvocationConfiguration);

		// log.debug("result:{}", result);
		// String jsonString = result.get
		// FIXME: this is a test shim
		String galleryData = LocalFileUtils.getClasspathResourceFileAsString("/data/gallery_response.json");
		try {
			ThumbnailList thumbnailListEntry = objectMapper.readerFor(ThumbnailList.class).readValue(galleryData);
			return thumbnailListEntry;
		} catch (JsonProcessingException e) {
			log.error("error parsing thumbnail response", e);
			throw new JargonException("Invalid listing response", e);
		}

	}

}
