package org.lexevs.dao.index.lucene.entity;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.lexevs.dao.index.access.AbstractBaseIndexDao;
import org.lexevs.dao.index.access.entity.EntityDao;
import org.lexevs.dao.index.model.IndexableEntity;
import org.lexevs.dao.index.model.IndexedEntity;
import org.lexevs.dao.index.version.LexEvsIndexFormatVersion;

import java.util.List;
import java.util.Set;

public class LuceneEntityDao extends AbstractBaseIndexDao implements EntityDao {

    @Override
    public List<LexEvsIndexFormatVersion> doGetSupportedLexEvsIndexFormatVersions() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getIndexName(String codingSchemeUri, String version) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<IndexedEntity> query(String codingSchemeUri, String version, List<? extends Query> combinedQueries, List<? extends Query> individualQueries) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void deleteDocuments(String codingSchemeUri, String version, Term term) {
        this.getTemplate(codingSchemeUri, version).removeDocuments(term);
    }

    @Override
    public void deleteDocuments(String codingSchemeUri, String version, Query query) {
        this.getTemplate(codingSchemeUri, version).removeDocuments(query);
    }

    @Override
    public void addDocuments(String codingSchemeUri, String version, List<IndexableEntity> documents, Analyzer analyzer) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public IndexedEntity getDocumentById(String codingSchemeUri, String version, int id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public IndexedEntity getDocumentById(String codingSchemeUri, String version, int id, Set<String> fields) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Query getMatchAllDocsQuery(String codingSchemeUri, String version) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<IndexedEntity> query(String codingSchemeUri, String version, Query query) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<IndexedEntity> query(Query query) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
