package org.lexevs.dao.index.lucene.v2014;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.lexevs.dao.index.access.AbstractBaseIndexDao;
import org.lexevs.dao.index.access.entity.EntityDao;
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
    public void optimizeIndex(String codingSchemeUri, String version) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<ScoreDoc> query(String codingSchemeUri, String version, List<? extends Query> combinedQueries, List<? extends Query> individualQueries) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void deleteDocuments(String codingSchemeUri, String version, Term term) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void deleteDocuments(String codingSchemeUri, String version, Query query) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addDocuments(String codingSchemeUri, String version, List<Document> documents, Analyzer analyzer) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Document getDocumentById(String codingSchemeUri, String version, int id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Document getDocumentById(String codingSchemeUri, String version, int id, Set<String> fields) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Query getMatchAllDocsQuery(String codingSchemeUri, String version) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<ScoreDoc> query(String codingSchemeUri, String version, Query query) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<ScoreDoc> query(Query query) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Filter getBoundaryDocsHitAsAWholeFilter(String codingSchemeUri, String version, Query query) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Filter getCodingSchemeFilter(String uri, String version) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
