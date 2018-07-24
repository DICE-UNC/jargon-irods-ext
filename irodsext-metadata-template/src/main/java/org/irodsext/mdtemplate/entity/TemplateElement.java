package org.irodsext.mdtemplate.entity;

import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "template_elements_poc")
public class TemplateElement implements Comparable<TemplateElement> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "element_id", unique = true, nullable = false)
	private Long id;

	@Column(name = "name",nullable = false, length = 100)
	private String name;

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
	
	@Column(name = "min_cardinality")
	private Integer MINCardinality;	
	
	@Column(name = "max_cardinality")
	private Integer MAXCardinality;	
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAttributeUnit() {
		return attributeUnit;
	}

	public void setAttributeUnit(String attributeUnit) {
		this.attributeUnit = attributeUnit;
	}

	/*public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}*/

	
	public String getType() {
		return type;
	}

	/*public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}*/

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

	public Integer getMINCardinality() {
		return MINCardinality;
	}

	public void setMINCardinality(Integer MINCardinality) {
		this.MINCardinality = MINCardinality;
	}	
	
	public Integer getMAXCardinality() {
		return MAXCardinality;
	}

	public void setMAXCardinality(Integer mAXCardinality) {
		MAXCardinality = mAXCardinality;
	}
	
	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (obj instanceof TemplateElement) {
			TemplateElement te = (TemplateElement) obj;

			if (te.getName() == null || te.getDefaultValue() == null) {
				return false;
			}

			boolean areAttributesEqual = getName().equals(te.getName());
			boolean areValuesEqual = getDefaultValue().equals(te.getDefaultValue());

			if (areAttributesEqual && areValuesEqual) {
				return true;
			}
		}

		return false;
	}

	@Override
	public int hashCode() {
		return (getName() + getDefaultValue()).hashCode();
	}

	@Override
	public int compareTo(TemplateElement o) {
		return this.guid.compareTo(o.guid);
	}
	
}