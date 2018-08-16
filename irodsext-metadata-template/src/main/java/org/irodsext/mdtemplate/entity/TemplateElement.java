package org.irodsext.mdtemplate.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import javax.persistence.CascadeType;
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
@Table(name = "md_template_elements")
public class TemplateElement implements Serializable, Comparable<TemplateElement> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "element_id", unique = true, nullable = false)
	private Long id;

	@Column(name = "name",nullable = false, length = 100)
	private String name;
	
	@Column(name = "description", length = 512)
	private String description;
	
	@Column(name = "i18name", length = 80)
	private String i18nName;

	@Column(name = "i18description")
	private String i18nDescription;

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
	private String validationExp;
	
	@Column(name = "guid", unique = true, nullable = false)
	private String guid;	

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "template_id", nullable = false, updatable = true)
	private Template template;
	
	@Column(name = "min_cardinality")
	private Integer MINCardinality;	
	
	@Column(name = "max_cardinality")
	private Integer MAXCardinality;	
	
	@OneToMany(mappedBy = "templateElement", cascade=CascadeType.ALL, orphanRemoval = true)
	private Set<TemplateElement> elements = new TreeSet<>();
	
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "parent_id", updatable = true)
	private TemplateElement templateElement;
	
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

	public String getValidationExp() {
		return validationExp;
	}

	public void setValidationExp(String validationExp) {
		this.validationExp = validationExp;
	}

	public String getGuid() {
		if(guid.isEmpty())
			return UUID.randomUUID().toString();
		else
			return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/*public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
*/
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
	
	

	public Set<TemplateElement> getElements() {
		return elements;
	}

	public void setElements(Set<TemplateElement> elements) {
		this.elements = elements;
	}

	public TemplateElement getTemplateElement() {
		return templateElement;
	}

	public void setTemplateElement(TemplateElement templateElement) {
		this.templateElement = templateElement;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (obj instanceof TemplateElement) {
			TemplateElement te = (TemplateElement) obj;

			if (te.getName() == null || te.getGuid() == null) {
				return false;
			}

			boolean areAttributesEqual = getName().equals(te.getName());
			boolean areGuidsEqual = getGuid().equals(te.getGuid());

			if (areAttributesEqual && areGuidsEqual) {
				return true;
			}
		}

		return false;
	}

	@Override
	public int hashCode() {
		return (getName() + getGuid()).hashCode();
	}

	@Override
	public int compareTo(TemplateElement o) {
		return this.guid.compareTo(o.guid);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

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

	@Override
	public String toString() {
		final int maxLen = 10;
		StringBuilder builder = new StringBuilder();
		builder.append("TemplateElement [");
		if (id != null)
			builder.append("id=").append(id).append(", ");
		if (name != null)
			builder.append(", name=").append(name).append(", ");
		if (description != null)
			builder.append(", description=").append(description).append(", ");
		if (i18nName != null)
			builder.append(", i18nName=").append(i18nName).append(", ");
		if (i18nDescription != null)
			builder.append(", i18nDescription=").append(i18nDescription).append(", ");
		if (defaultValue != null)
			builder.append(", defaultValue=").append(defaultValue).append(", ");
		if (attributeUnit != null)
			builder.append(", attributeUnit=").append(attributeUnit).append(", ");
		if (type != null)
			builder.append(", type=").append(type).append(", ");
		if (options != null)
			builder.append(", options=").append(options).append(", ");
		if (validationExp != null)
			builder.append(", validationExp=").append(validationExp).append(", ");
		if (guid != null)
			builder.append(", guid=").append(guid).append(", ");
		if (MINCardinality != null)
			builder.append(", MINCardinality=").append(MINCardinality).append(", ");
		if (MAXCardinality != null)
			builder.append(", MAXCardinality=").append(MAXCardinality).append(", ");
		if (elements != null)
			builder.append(", elements=").append(toString(elements, maxLen)).append(", ");
		
		builder.append("]");
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
