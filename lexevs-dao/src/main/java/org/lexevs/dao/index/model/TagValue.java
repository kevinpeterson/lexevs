package org.lexevs.dao.index.model;

/**
 * Scala-style Tuple.
 */
public class TagValue {

    private String tag;
    private String value;

    public TagValue(String tag, String value) {
        super();
        this.tag = tag;
        this.value = value;
    }

    public String getTag() {
        return tag;
    }

    public String getValue() {
        return value;
    }

}
