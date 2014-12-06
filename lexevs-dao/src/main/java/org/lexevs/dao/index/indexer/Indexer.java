package org.lexevs.dao.index.indexer;

import org.apache.lucene.document.Document;

public interface Indexer<T> {

    public Document index(T indexable);

}
