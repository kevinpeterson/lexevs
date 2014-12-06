package org.lexevs.dao.index.model;

import org.apache.lucene.document.Document;

public class IndexedEntity {

    protected String id;

    protected String entityCode;

    protected String entityCodeNamespace;

    protected String entityDescription;

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

    protected Document toDocument() {
        return null;
    }
}
