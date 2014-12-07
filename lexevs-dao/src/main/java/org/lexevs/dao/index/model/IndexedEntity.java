package org.lexevs.dao.index.model;

import org.apache.lucene.document.Document;

public class IndexedEntity {

    private String entityUuid;

    private String codeSystemUri;
    private String codeSystemVersion;

    private String entityCode;
    private String entityCodeNamespace;
    private String entityDescription;

    public IndexedEntity() {
        super();
    }

    public IndexedEntity(Document entity) {
        super();
        this.indexEntity(entity);
    }

    protected void indexEntity(Document entity) {
        //TODO
    }

    public String getEntityUuid() {
        return entityUuid;
    }

    public String getCodeSystemUri() {
        return codeSystemUri;
    }

    public String getCodeSystemVersion() {
        return codeSystemVersion;
    }

    public String getEntityCode() {
        return entityCode;
    }

    public String getEntityCodeNamespace() {
        return entityCodeNamespace;
    }

    public String getEntityDescription() {
        return entityDescription;
    }

    protected void setEntityUuid(String entityUuid) {
        this.entityUuid = entityUuid;
    }

    protected void setCodeSystemUri(String codeSystemUri) {
        this.codeSystemUri = codeSystemUri;
    }

    protected void setCodeSystemVersion(String codeSystemVersion) {
        this.codeSystemVersion = codeSystemVersion;
    }

    protected void setEntityCode(String entityCode) {
        this.entityCode = entityCode;
    }

    protected void setEntityCodeNamespace(String entityCodeNamespace) {
        this.entityCodeNamespace = entityCodeNamespace;
    }

    protected void setEntityDescription(String entityDescription) {
        this.entityDescription = entityDescription;
    }
}
