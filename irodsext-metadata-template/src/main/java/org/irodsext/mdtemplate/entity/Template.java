package org.irodsext.mdtemplate.entity;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "templates_poc")
public class Template {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "template_id" , unique = true, nullable = false)
	private Long id;

	@Column(name = "template_name", unique = true, nullable = false, length = 100)
	private String templateName;

	@Column(name = "create_ts", nullable = false, length = 60, updatable = false)
	private Date createTs;

	@Column(name = "description", length = 512)
	private String description;

	@Column(name = "guid")
	private UUID guid;
	
	@Column(name = "access_type")
	private String access_type;
	
	@Column(name = "owner")
	private String owner;
	
	@OneToMany(mappedBy = "template", fetch = FetchType.EAGER)
	private Set<TemplateElement> elements;
	
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

	public UUID getGuid() {
		return guid;
	}

	public void setGuid(UUID guid) {
		this.guid = guid;
	}

	public String getAccess_type() {
		return access_type;
	}

	public void setAccess_type(String access_type) {
		this.access_type = access_type;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	/*@OneToMany(mappedBy = "template", fetch = FetchType.EAGER)
	private String s;*/




}
