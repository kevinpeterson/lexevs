package org.lexevs.dao.index.model;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;
import org.apache.lucene.document.Document;
import org.lexevs.dao.index.indexer.Indexer;

import java.util.ArrayList;
import java.util.List;

public class IndexableEntity extends IndexedEntity implements BlockIndexable {

    private Indexer<IndexedEntity> entityIndexer;

    private List<IndexableProperty> properties = new ArrayList<IndexableProperty>();

    public IndexableEntity(Entity entity, Indexer<IndexedEntity> entityIndexer, Indexer<IndexableProperty> propertyIndexer) {
        super();
        this.entityIndexer = entityIndexer;

        for(Property property : entity.getAllProperties()) {
            this.properties.add(new IndexableProperty(property, propertyIndexer));
        }
    }

    @Override
    public List<Document> getDocuments() {
        List<Document> documents = new ArrayList<Document>();

        for(IndexableProperty property : this.properties) {
            documents.add(property.getDocument());
        }

        documents.add(this.entityIndexer.index(this));

        return documents;
    }
}
