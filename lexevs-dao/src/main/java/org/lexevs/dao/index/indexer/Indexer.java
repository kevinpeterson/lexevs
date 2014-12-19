package org.lexevs.dao.index.indexer;

import org.apache.lucene.document.Document;

import java.util.List;

public interface Indexer<T> {

    public List<Document> index(T indexable);

}
