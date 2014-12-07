package org.lexevs.dao.index.model;

import org.LexGrid.commonTypes.Property;
import org.apache.lucene.document.Document;
import org.lexevs.dao.index.indexer.Indexer;

public class IndexableProperty extends IndexedProperty implements Indexable {

    private Indexer<IndexedProperty> indexer;

    public IndexableProperty(Property property, Indexer<IndexedProperty> indexer) {
        super();
        this.indexer = indexer;
    }

    @Override
    public Document getDocument() {
        return this.indexer.index(this);
    }
}
