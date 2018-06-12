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

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "element_id", unique = true, nullable = false)
	private Long id;

	@Column(name = "attribute",nullable = false, length = 100)
	private String attribute;

	@Column(name = "default_value", length = 100)
	private String defaultValue;

	@Column(name = "attribute_unit", length = 100)
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
	private Long cardinality;	
	
	
	
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

	public Long getCardinality() {
		return cardinality;
	}

	public void setCardinality(Long cardinality) {
		this.cardinality = cardinality;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (obj instanceof TemplateElement) {
			TemplateElement te = (TemplateElement) obj;

			if (te.getAttribute() == null || te.getDefaultValue() == null) {
				return false;
			}

			boolean areAttributesEqual = getAttribute().equals(te.getAttribute());
			boolean areValuesEqual = getDefaultValue().equals(te.getDefaultValue());

			if (areAttributesEqual && areValuesEqual) {
				return true;
			}
		}

		return false;
	}

	@Override
	public int hashCode() {
		return (getAttribute() + getDefaultValue()).hashCode();
	}
	
}
