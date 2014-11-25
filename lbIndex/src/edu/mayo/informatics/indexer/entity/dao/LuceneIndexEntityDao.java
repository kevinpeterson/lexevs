package edu.mayo.informatics.indexer.entity.dao;

import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.StoredFieldVisitor;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
//import org.lexevs.dao.index.access.entity.EntityDao;
import org.lexevs.dao.index.version.LexEvsIndexFormatVersion;

public abstract class LuceneIndexEntityDao 
//implements EntityDao 
{

//	@Override
	public boolean supportsLexEvsIndexFormatVersion(
			LexEvsIndexFormatVersion version) {
		// TODO Auto-generated method stub
		return false;
	}

//	@Override
	public String getIndexName(String codingSchemeUri, String version) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
	public void optimizeIndex(String codingSchemeUri, String version) {
		// TODO Auto-generated method stub

	}


//	@Override
	public void deleteDocuments(String codingSchemeUri, String version,
			Term term) {
		// TODO Auto-generated method stub
		
	}

//	@Override
	public void deleteDocuments(String codingSchemeUri, String version,
			Query query) {
		// TODO Auto-generated method stub
		
	}

//	@Override
	public void addDocuments(String codingSchemeUri, String version,
			List<Document> documents, Analyzer analyzer) {
		// TODO Auto-generated method stub
		
	}


//	@Override
	public List<ScoreDoc> query(String codingSchemeUri, String version,
			Query query) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
	public List<ScoreDoc> query(Query query) {
		// TODO Auto-generated method stub
		return null;
	}



//	@Override
	public List<ScoreDoc> query(String codingSchemeUri, String version,
			List<? extends Query> combinedQueries,
			List<? extends Query> individualQueries) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
	public Document getDocumentById(String codingSchemeUri, String version,
			int id) {
		// TODO Auto-generated method stub
		return null;
	}

	public Document getDocumentById(String codingSchemeUri, String version,
			int id, StoredFieldVisitor fieldVisitor) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
	public Query getMatchAllDocsQuery(String codingSchemeUri, String version) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
	public Filter getBoundaryDocsHitAsAWholeFilter(String codingSchemeUri,
			String version, Query query) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
	public Filter getCodingSchemeFilter(String uri, String version) {
		// TODO Auto-generated method stub
		return null;
	}

}
