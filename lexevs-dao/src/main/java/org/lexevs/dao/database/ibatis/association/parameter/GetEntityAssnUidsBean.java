package org.lexevs.dao.database.ibatis.association.parameter;

import java.util.List;

import org.lexevs.dao.database.access.codednodegraph.CodedNodeGraphDao.TripleNode;
import org.lexevs.dao.database.ibatis.parameter.PrefixedTableParameterBean;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery.CodeNamespacePair;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery.QualifierNameValuePair;

public class GetEntityAssnUidsBean extends PrefixedTableParameterBean {
	
	private String codingSchemeUid;
	private String associationPredicateUid;
	private String entityCode;
	private String entityCodeNamespace;
	private List<QualifierNameValuePair> associationQualifiers;
	private List<CodeNamespacePair> mustHaveCodes;
	
	private TripleNode tripleNode;
	
	public String getEntityCode() {
		return entityCode;
	}
	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}

	public void setEntityCodeNamespace(String entityCodeNamespace) {
		this.entityCodeNamespace = entityCodeNamespace;
	}
	public String getEntityCodeNamespace() {
		return entityCodeNamespace;
	}
	public String getCodingSchemeUid() {
		return codingSchemeUid;
	}
	public void setCodingSchemeUid(String codingSchemeUid) {
		this.codingSchemeUid = codingSchemeUid;
	}
	public String getAssociationPredicateUid() {
		return associationPredicateUid;
	}
	public void setAssociationPredicateUid(String associationPredicateUid) {
		this.associationPredicateUid = associationPredicateUid;
	}
	public void setTripleNode(TripleNode tripleNode) {
		this.tripleNode = tripleNode;
	}
	public TripleNode getTripleNode() {
		return tripleNode;
	}
	public void setAssociationQualifiers(List<QualifierNameValuePair> associationQualifiers) {
		this.associationQualifiers = associationQualifiers;
	}
	public List<QualifierNameValuePair> getAssociationQualifiers() {
		return associationQualifiers;
	}
	public void setMustHaveCodes(List<CodeNamespacePair> mustHaveCodes) {
		this.mustHaveCodes = mustHaveCodes;
	}
	public List<CodeNamespacePair> getMustHaveCodes() {
		return mustHaveCodes;
	}
}
