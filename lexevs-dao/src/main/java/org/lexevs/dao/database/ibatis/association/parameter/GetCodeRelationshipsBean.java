/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.lexevs.dao.database.ibatis.association.parameter;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.lexevs.dao.database.ibatis.parameter.PrefixedTableParameterBean;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery.CodeNamespacePair;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery.QualifierNameValuePair;

public class GetCodeRelationshipsBean extends PrefixedTableParameterBean {
	
	private String codingSchemeUid;
	private String relationsContainerName;
	private String sourceCode;
	private String sourceNamespace;
	private String targetCode;
	private String targetNamespace;
	private List<QualifierNameValuePair> associationQualifiers;
	private List<String> associations;
	private List<CodeNamespacePair> mustHaveSourceCodes;
	private List<CodeNamespacePair> mustHaveTargetCodes;
	private List<String> mustHaveSourceNamespaces;
	private List<String> mustHaveTargetNamespaces;
	private List<String> mustHaveEntityTypes;
	private Boolean restrictToAnonymous;
	private boolean useTransitive = false;
	
	public String getCodingSchemeUid() {
		return codingSchemeUid;
	}
	public void setCodingSchemeUid(String codingSchemeUid) {
		this.codingSchemeUid = codingSchemeUid;
	}
	public String getSourceCode() {
		return sourceCode;
	}
	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}
	public String getSourceNamespace() {
		return sourceNamespace;
	}
	public void setSourceNamespace(String sourceNamespace) {
		this.sourceNamespace = sourceNamespace;
	}
	public String getTargetCode() {
		return targetCode;
	}
	public void setTargetCode(String targetCode) {
		this.targetCode = targetCode;
	}
	public String getTargetNamespace() {
		return targetNamespace;
	}
	public void setTargetNamespace(String targetNamespace) {
		this.targetNamespace = targetNamespace;
	}
	public List<QualifierNameValuePair> getAssociationQualifiers() {
		return associationQualifiers;
	}
	public void setAssociationQualifiers(
			List<QualifierNameValuePair> associationQualifiers) {
		this.associationQualifiers = associationQualifiers;
	}
	public List<String> getAssociations() {
		return associations;
	}
	public void setAssociations(List<String> associations) {
		this.associations = associations;
	}
	public List<CodeNamespacePair> getMustHaveSourceCodes() {
		return mustHaveSourceCodes;
	}
	public void setMustHaveSourceCodes(List<CodeNamespacePair> mustHaveSourceCodes) {
		this.mustHaveSourceCodes = mustHaveSourceCodes;
	}
	public List<CodeNamespacePair> getMustHaveTargetCodes() {
		return mustHaveTargetCodes;
	}
	public void setMustHaveTargetCodes(List<CodeNamespacePair> mustHaveTargetCodes) {
		this.mustHaveTargetCodes = mustHaveTargetCodes;
	}
	public List<String> getMustHaveSourceNamespaces() {
		return mustHaveSourceNamespaces;
	}
	public void setMustHaveSourceNamespaces(List<String> mustHaveSourceNamespaces) {
		this.mustHaveSourceNamespaces = mustHaveSourceNamespaces;
	}
	public List<String> getMustHaveTargetNamespaces() {
		return mustHaveTargetNamespaces;
	}
	public void setMustHaveTargetNamespaces(List<String> mustHaveTargetNamespaces) {
		this.mustHaveTargetNamespaces = mustHaveTargetNamespaces;
	}
	public boolean isUseTransitive() {
		return useTransitive;
	}
	public void setUseTransitive(boolean useTransitive) {
		this.useTransitive = useTransitive;
	}
	public void setRelationsContainerName(String relationsContainerName) {
		this.relationsContainerName = relationsContainerName;
	}
	public String getRelationsContainerName() {
		return relationsContainerName;
	}	

	public List<String> getMustHaveEntityTypes() {
		return mustHaveEntityTypes;
	}
	public void setMustHaveEntityTypes(List<String> mustHaveEntityTypes) {
		this.mustHaveEntityTypes = mustHaveEntityTypes;
	}
	public Boolean getRestrictToAnonymous() {
		return restrictToAnonymous;
	}
	public void setRestrictToAnonymous(Boolean restrictToAnonymous) {
		this.restrictToAnonymous = restrictToAnonymous;
	}
	public boolean isNeedsEntityJoin() {
		return CollectionUtils.isNotEmpty(this.mustHaveEntityTypes) || (this.restrictToAnonymous != null);
	}
}