package org.irodsext.mdtemplate.services;
import java.util.List;
import java.util.UUID;
import org.irods.jargon.metadatatemplate.AbstractMetadataService;
import org.irods.jargon.metadatatemplate.MetadataTemplateException;
import org.irods.jargon.metadatatemplate.MetadataTemplateNotFoundException;
import org.irods.jargon.metadatatemplate.model.MDTemplate;
import org.irodsext.mdtemplate.TemplateService;
import org.irodsext.mdtemplate.dao.TemplateDao;
import org.irodsext.mdtemplate.entity.Template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.threeten.bp.DateTimeUtils;

@Service("abstractMetadataService")
@Transactional
public class IrodsExtMetadataServiceImpl extends AbstractMetadataService {

	@Autowired
	private TemplateDao templateDao;
	

	@Override
	public List<MDTemplate> listPublicTemplates() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MDTemplate> listAllTemplates(String path) throws MetadataTemplateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteTemplateByUUID(String uniqueName) throws MetadataTemplateException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public MDTemplate findTemplateByUUID(UUID uuid)
			throws MetadataTemplateNotFoundException, MetadataTemplateException {
		
		System.out.println("irods-ext findTemplateByUUID {} searching by uuid");
		Template template = templateDao.findByGuid(uuid);
		MDTemplate mdTemplate = new MDTemplate();
		mdTemplate.setTemplateName(template.getTemplateName());
		mdTemplate.setGuid(template.getGuid().toString());
		System.out.println("TemplateName is :: " + template.getTemplateName());
		System.out.println("TemplateId is :: " + template.getId());
		return mdTemplate;
	}

	@Override
	public UUID saveTemplate(MDTemplate mdTemplate) throws MetadataTemplateException {

		Template template = new Template();
		template.setTemplateName(mdTemplate.getTemplateName());
		template.setDescription(mdTemplate.getDescription());
		template.setGuid(UUID.fromString(mdTemplate.getGuid()));
		template.setCreateTs(DateTimeUtils.toDate(mdTemplate.getCreateTs().toZonedDateTime().toInstant()));
		template.setModifyTs(DateTimeUtils.toDate(mdTemplate.getModifyTs().toZonedDateTime().toInstant()));
		template.setOwner(mdTemplate.getOwner());
		UUID guid = (UUID) templateDao.save(template);
		return null;
	}

}
