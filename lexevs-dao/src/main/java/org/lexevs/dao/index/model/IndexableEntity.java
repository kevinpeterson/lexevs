package org.lexevs.dao.index.model;

import org.lexevs.dao.database.ibatis.entity.model.IdableEntity;

public class IndexableEntity {

    private String codingSchemeUri;
    private String codingSchemeVersion;
    private IdableEntity entity;

    public IndexableEntity(IdableEntity entity, String codingSchemeVersion, String codingSchemeUri) {
        this.entity = entity;
        this.codingSchemeVersion = codingSchemeVersion;
        this.codingSchemeUri = codingSchemeUri;
    }

    public String getCodingSchemeUri() {
        return codingSchemeUri;
    }

    public String getCodingSchemeVersion() {
        return codingSchemeVersion;
    }

    public IdableEntity getEntity() {
        return entity;
    }
}
