package org.lexevs.dao.index.model;

import java.util.ArrayList;
import java.util.List;

public class IndexedProperty {

    protected String id;
    protected String entityCode;
    protected String entityCodeNamespace;
    protected String entityDescription;
    protected String propertyType;
    protected String propertyName;
    protected String value;
    protected List<TagValue> propertyQualifiers = new ArrayList<TagValue>();
    protected List<String> sources = new ArrayList<String>();
    protected List<String> usageContexts = new ArrayList<String>();
    protected float score;


}
