package org.irodsext.mdtemplate.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "md_templates")
public class Template implements Serializable, Comparable<Template>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "template_id" , unique = true, nullable = false)
	private Long id;

	@Column(name = "template_name", unique = true, nullable = false, length = 100)
	private String templateName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_ts", nullable = false, length = 60, updatable = false)
	private Date createTs;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modify_ts", length = 60)
	private Date modifyTs;
	
	@Column(name = "version")
	private Integer version;	
	
	@Column(name = "description", length = 512)
	private String description;
	
	@Column(name = "i18name", length = 80)
	private String i18nName;

	@Column(name = "i18description")
	private String i18nDescription;

	@Column(name = "guid", unique = true, nullable = false)
	private String guid;
	
	@Column(name = "access_type")
	private String accessType;
	
	@Column(name = "owner")
	private String owner;
		
	@OneToMany(mappedBy = "template", cascade=CascadeType.ALL, orphanRemoval = true)
	private Set<TemplateElement> elements = new TreeSet<>();
	
	public String getI18nName() {
		return i18nName;
	}

	public void setI18nName(String i18nName) {
		this.i18nName = i18nName;
	}

	public String getI18nDescription() {
		return i18nDescription;
	}

	public void setI18nDescription(String i18nDescription) {
		this.i18nDescription = i18nDescription;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public Date getCreateTs() {
		return createTs;
	}

	public void setCreateTs(Date createTs) {
		this.createTs = createTs;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Set<TemplateElement> getElements() {
		return elements;
	}

	public void setElements(Set<TemplateElement> fields) {
		this.elements = fields;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Date getModifyTs() {
		return modifyTs;
	}

	public void setModifyTs(Date modifyTs) {
		this.modifyTs = modifyTs;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	@Override
	public int compareTo(Template template) {
		return templateName.compareTo(template.getTemplateName());
	}

	@Override
	public String toString() {
		final int maxLen = 10;
		StringBuilder builder = new StringBuilder();
		builder.append("Template [id=").append(id).append(", templateName=").append(templateName).append(", createTs=")
				.append(createTs).append(", modifyTs=").append(modifyTs).append(", version=").append(version)
				.append(", description=").append(description).append(", i18nName=").append(i18nName)
				.append(", i18nDescription=").append(i18nDescription).append(", guid=").append(guid)
				.append(", accessType=").append(accessType).append(", owner=").append(owner).append(", elements=")
				.append(elements != null ? toString(elements, maxLen) : null).append("]");
		return builder.toString();
	}

	private String toString(Collection<?> collection, int maxLen) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext() && i < maxLen; i++) {
			if (i > 0)
				builder.append(", ");
			builder.append(iterator.next());
		}
		builder.append("]");
		return builder.toString();
	}	

}
