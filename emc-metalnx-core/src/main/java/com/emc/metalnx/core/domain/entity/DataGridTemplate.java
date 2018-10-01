 /* Copyright (c) 2018, University of North Carolina at Chapel Hill */
 /* Copyright (c) 2015-2017, Dell EMC */


package com.emc.metalnx.core.domain.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.emc.metalnx.core.domain.exceptions.DataGridTooLongTemplateNameException;

@Entity
//@Audited
@Table(name = "templates")
public class DataGridTemplate implements Serializable, Comparable<DataGridTemplate> {

	private static final long serialVersionUID = 1L;

	@Id
	@NotAudited
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "template_id", unique = true, nullable = false)
	private Long id;

	@Column(name = "template_name", unique = true, nullable = false, length = 100)
	private String templateName;

	@Column(name = "owner", nullable = false, length = 100)
	private String owner;

	@Column(name = "description", nullable = false, length = 512)
	private String description;

	@Column(name = "usage_info", length = 100)
	private String usageInformation;

	@Column(name = "access_type", length = 32)
	private String accessType;

	@Column(name = "version")
	private Integer version;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_ts", nullable = false, length = 60, updatable = false)
	private Date createTs;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modify_ts", nullable = false, length = 60)
	private Date modifyTs;

	@OneToMany(mappedBy = "template", fetch = FetchType.EAGER)
	private Set<DataGridTemplateField> fields;

	private static final int TEMPLATE_NAME_MAX_LENGTH = 100;
	private static final int TEMPLATE_DESC_MAX_LENGTH = 100;

	private boolean isModified = false;

	public DataGridTemplate() {
		// empty constructor
	}

	public DataGridTemplate(String templateName) throws DataGridTooLongTemplateNameException {
		if (templateName.length() > TEMPLATE_NAME_MAX_LENGTH) {
			throw new DataGridTooLongTemplateNameException(
					"Template name exceeded " + TEMPLATE_NAME_MAX_LENGTH + " characters.");
		}

		this.templateName = templateName;
	}

	/**
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * @param owner
	 *            the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 * @throws DataGridTooLongTemplateNameException
	 */
	public void setDescription(String description) throws DataGridTooLongTemplateNameException {
		if (description.length() > TEMPLATE_NAME_MAX_LENGTH) {
			throw new DataGridTooLongTemplateNameException(
					"Template description exceeded " + TEMPLATE_DESC_MAX_LENGTH + " characters.");
		}
		this.description = description;
	}

	/**
	 * @return the createTs
	 */
	public Date getCreateTs() {
		return createTs;
	}

	/**
	 * @param createTs
	 *            the createTs to set
	 */
	public void setCreateTs(Date createTs) {
		this.createTs = createTs;
	}

	/**
	 * @return the modifyTs
	 */
	public Date getModifyTs() {
		return modifyTs;
	}

	/**
	 * @param modifyTs
	 *            the modifyTs to set
	 */
	public void setModifyTs(Date modifyTs) {
		this.modifyTs = modifyTs;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the templateName
	 */
	public String getTemplateName() {
		return templateName;
	}

	/**
	 * @param the
	 *            templateName to set
	 * @throws DataGridTooLongTemplateNameException
	 */
	public void setTemplateName(String templateName) throws DataGridTooLongTemplateNameException {
		if (templateName.length() > TEMPLATE_NAME_MAX_LENGTH) {
			throw new DataGridTooLongTemplateNameException(
					"Template name exceeded " + TEMPLATE_NAME_MAX_LENGTH + " characters.");
		}

		this.templateName = templateName;
	}

	/**
	 * @return the usageInformation
	 */
	public String getUsageInformation() {
		return usageInformation;
	}

	/**
	 * @param usageInformation
	 *            the usageInformation to set
	 */
	public void setUsageInformation(String usageInformation) {
		this.usageInformation = usageInformation;
	}

	/**
	 * @return the accessType
	 */
	public String getAccessType() {
		return accessType;
	}

	/**
	 * @param accessType
	 *            the accessType to set
	 */
	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	/**
	 * @return the fields
	 */
	public Set<DataGridTemplateField> getFields() {
		return fields;
	}

	/**
	 * @param fields
	 *            the fields to set
	 */
	public void setFields(Set<DataGridTemplateField> fields) {
		this.fields = fields;
	}

	/**
	 * @return the version
	 */
	public Integer getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}

	/**
	 * @return the isModified
	 */
	public boolean isModified() {
		return isModified;
	}

	/**
	 * @param isModified
	 *            the isModified to set
	 */
	public void setModified(boolean isModified) {
		this.isModified = isModified;
	}

	@Override
	public int compareTo(DataGridTemplate dgt) {
		return templateName.compareTo(dgt.getTemplateName());
	}

	@Override
	public String toString() {
		final int maxLen = 10;
		StringBuilder builder = new StringBuilder();
		builder.append("DataGridTemplate [");
		if (id != null) {
			builder.append("id=").append(id).append(", ");
		}
		if (templateName != null) {
			builder.append("templateName=").append(templateName).append(", ");
		}
		if (owner != null) {
			builder.append("owner=").append(owner).append(", ");
		}
		if (description != null) {
			builder.append("description=").append(description).append(", ");
		}
		if (usageInformation != null) {
			builder.append("usageInformation=").append(usageInformation).append(", ");
		}
		if (accessType != null) {
			builder.append("accessType=").append(accessType).append(", ");
		}
		if (version != null) {
			builder.append("version=").append(version).append(", ");
		}
		if (createTs != null) {
			builder.append("createTs=").append(createTs).append(", ");
		}
		if (modifyTs != null) {
			builder.append("modifyTs=").append(modifyTs).append(", ");
		}
		if (fields != null) {
			builder.append("fields=").append(toString(fields, maxLen)).append(", ");
		}
		builder.append("isModified=").append(isModified).append("]");
		return builder.toString();
	}

	private String toString(Collection<?> collection, int maxLen) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext() && i < maxLen; i++) {
			if (i > 0) {
				builder.append(", ");
			}
			builder.append(iterator.next());
		}
		builder.append("]");
		return builder.toString();
	}

}
