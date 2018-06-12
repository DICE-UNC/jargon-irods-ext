package org.irodsext.mdtemplate.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "template_elements_poc")
public class TemplateElement {

	private final int MAX_ATTR_LENGTH = 100;
	private final int MAX_VAL_LENGTH = 100;
	private final int MAX_UNT_LENGTH = 100;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "element_id", unique = true, nullable = false)
	private Long id;

	@Column(name = "attribute",nullable = false, length = MAX_ATTR_LENGTH)
	private String attribute;

	@Column(name = "defalut_value", length = MAX_VAL_LENGTH)
	private String defaultValue;

	@Column(name = "attribute_unit", length = MAX_UNT_LENGTH)
	private String attributeUnit;
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "required")
	private boolean required;
	
	@Column(name = "options")
	private String options;
	
	@Column(name = "validation_exp")
	private String validation_exp;
	
	@Column(name = "guid")
	private UUID guid;	

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "template_id", nullable = false, updatable = true)
	private Template template;
	
	@Column(name = "parent_id")
	private Long parentId;	
	
	@Column(name = "cardinality")
	private UUID cardinality;	
	
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getAttributeUnit() {
		return attributeUnit;
	}

	public void setAttributeUnit(String attributeUnit) {
		this.attributeUnit = attributeUnit;
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public String getValidation_exp() {
		return validation_exp;
	}

	public void setValidation_exp(String validation_exp) {
		this.validation_exp = validation_exp;
	}

	public UUID getGuid() {
		return guid;
	}

	public void setGuid(UUID guid) {
		this.guid = guid;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public UUID getCardinality() {
		return cardinality;
	}

	public void setCardinality(UUID cardinality) {
		this.cardinality = cardinality;
	}
	
	
	
}
