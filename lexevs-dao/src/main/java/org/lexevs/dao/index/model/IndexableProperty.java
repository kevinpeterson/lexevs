package org.lexevs.dao.index.model;

import org.LexGrid.commonTypes.Property;
import org.apache.lucene.document.Document;
import org.lexevs.dao.index.indexer.Indexer;

import java.util.ArrayList;
import java.util.List;

public class IndexableProperty implements Indexable {

    private String id;
    private String entityCode;
    private String entityCodeNamespace;
    private String entityDescription;
    private String propertyType;
    private String propertyName;
    private String value;
    private List<TagValue> propertyQualifiers = new ArrayList<TagValue>();
    private List<String> sources = new ArrayList<String>();
    private List<String> usageContexts = new ArrayList<String>();
    private float score;

    private Indexer<IndexableProperty> indexer;

    public IndexableProperty(Property property, Indexer<IndexableProperty> indexer) {
        super();
        this.indexer = indexer;
    }

    @Override
    public Document getDocument() {
        return this.indexer.index(this);
    }
}
