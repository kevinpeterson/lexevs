package org.lexevs.dao.index.indexer;

import org.apache.lucene.document.*;


public class LuceneDocumentBuilder {

    private Document document;

    public LuceneDocumentBuilder() {
        Document document = new Document();

        this.document = document;
    }

    public Document build() {
        return this.document;
    }

    public LuceneDocumentBuilder withTokenizedField(String name, boolean store, String value) {
        this.document.add(new TextField(name, value, isStore(store)));
        return this;
    }

    public LuceneDocumentBuilder withUntokenizedField(String name, boolean store, String value) {
        this.document.add(new StringField(name, value, isStore(store)));
        return this;
    }

    public LuceneDocumentBuilder withUntokenizedFields(String name, boolean store, String... values) {
        for(String value : values) {
            this.document.add(new StringField(name, value, isStore(store)));
        }
        return this;
    }

    public LuceneDocumentBuilder withUnindexedField(String name, String value) {
        this.document.add(new StoredField(name, value));
        return this;
    }

    public LuceneDocumentBuilder withField(String name, boolean store, int value) {
        this.document.add(new StringField(name, toInt(value), isStore(store)));
        return this;
    }

    public LuceneDocumentBuilder withField(String name, boolean store, boolean value) {
        this.document.add(new StringField(name, toBool(value), isStore(store)));
        return this;
    }

    private Field.Store isStore(boolean store) {
        return store ? Field.Store.YES : Field.Store.NO;
    }

    private String toInt(int value) {
        return Integer.toString(value);
    }

    private String toBool(boolean value) {
        return value ? "T" : "F";
    }


}
