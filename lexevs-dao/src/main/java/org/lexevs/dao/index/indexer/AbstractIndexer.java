package org.lexevs.dao.index.indexer;

import org.apache.commons.lang.StringUtils;


public abstract class AbstractIndexer<T> implements Indexer<T> {

    private static final char KEY_SEPARATOR = ':';

    protected String toKey(String... components) {
        return StringUtils.join(components, KEY_SEPARATOR);
    }

}
