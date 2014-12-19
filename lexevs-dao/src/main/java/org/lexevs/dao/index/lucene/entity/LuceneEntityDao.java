package org.lexevs.dao.index.lucene.entity;


import org.LexGrid.LexBIG.Utility.Constructors;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.lexevs.dao.index.access.AbstractBaseIndexDao;
import org.lexevs.dao.index.access.entity.EntityDao;
import org.lexevs.dao.index.indexer.EntityIndexer;
import org.lexevs.dao.index.indexer.Indexer;
import org.lexevs.dao.index.model.IndexableEntity;
import org.lexevs.dao.index.model.IndexedEntity;
import org.lexevs.dao.index.model.IndexedEntityReference;
import org.lexevs.dao.index.version.LexEvsIndexFormatVersion;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LuceneEntityDao extends AbstractBaseIndexDao implements EntityDao {

    private Indexer<IndexableEntity> indexer = new EntityIndexer();

    @Override
    public String getIndexName(String codingSchemeUri, String version) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<IndexedEntityReference> query(String codingSchemeUri, String version, List<? extends Query> combinedQueries, List<? extends Query> individualQueries) {
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
    public void addDocuments(String codingSchemeUri, String version, List<IndexableEntity> entities, Analyzer analyzer) {
        List<Document> documents = new ArrayList<Document>();

        for(IndexableEntity entity : entities) {
            documents.addAll(this.indexer.index(entity));
        }

        this.getTemplate(codingSchemeUri, version).addDocuments(documents, analyzer);
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
        return null;
    }

    @Override
    public List<IndexedEntityReference> query(String codingSchemeUri, String version, Query query) {
        List<IndexedEntityReference> returnList = new ArrayList<IndexedEntityReference>();

        for(ScoreDoc doc : this.getTemplate(
                Constructors.createAbsoluteCodingSchemeVersionReference(codingSchemeUri, version)).search(query, null)) {
            returnList.add(new IndexedEntityReference(doc.doc, doc.score));
        }

        return returnList;
    }


    @Override
    public boolean supportsLexEvsIndexFormatVersion(LexEvsIndexFormatVersion version) {
        return true;
    }
}
