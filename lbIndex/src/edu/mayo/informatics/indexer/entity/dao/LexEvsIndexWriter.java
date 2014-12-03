package edu.mayo.informatics.indexer.entity.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.concepts.Entity;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import edu.mayo.informatics.indexer.api.exceptions.InvalidValueException;

public class LexEvsIndexWriter {
	private static final String CODING_SCHEME_URI_VERSION_KEY_FIELD = null;
	private static final String CODING_SCHEME_URI_VERSION_CODE_NAMESPACE_KEY_FIELD = null;
	StandardAnalyzer analyzer; 
	Directory index;
	IndexWriterConfig config;
	
public void init(){
	analyzer = new StandardAnalyzer();
	index = new RAMDirectory();
	config = new IndexWriterConfig(Version.LUCENE_4_10_2, analyzer);
}

public List<Document> addEntityDocuments(
		String codingSchemeName,
		String codingSchemeUri, 
		String codingSchemeVersion, 
		Entity entity) throws InvalidValueException{
	Document entityBase = createEntityDocument(codingSchemeName, codingSchemeUri, codingSchemeVersion, entity);
	List<Document> docs = new ArrayList<Document>();
	Property[] props = entity.getAllProperties();
	for(Property prop: props){
		Document doc = new Document();
		startNewDocument(codingSchemeName + "-" + entity.getEntityCode() + "-" + prop.getPropertyId(),  doc);
		docs.add(createPropertyItem(prop, entity, codingSchemeName, doc));
	}
	docs.add(entityBase);
	return docs;
}

private Document createPropertyItem(Property prop, Entity entity, String codingSchemeName, Document doc) {

	List<Document> propQds = new ArrayList<Document>();
	doc.add(new TextField(prop.getPropertyName(), prop.getValue().getContent(), Field.Store.NO));
	PropertyQualifier[] pqs = prop.getPropertyQualifier();
	for(PropertyQualifier pq : pqs){
		Document dpq = new Document();
		dpq.add(new TextField(pq.getPropertyQualifierName(),pq.getValue().getContent(), Field.Store.NO));
		propQds.add(dpq);
	}

	return doc;
}

private Document createEntityDocument(
		final String codingSchemeName, 
		final String codingSchemeId,
		final String codingSchemeVersion,
		final Entity entity){
	Document d = new Document();
	d.add(new StringField("codingSchemeName", codingSchemeName, Field.Store.NO));
	d.add(new StringField("codingSchemeId", codingSchemeId, Field.Store.NO));
	d.add(new StringField("codeBase", "B", Field.Store.NO));
	d.add(new StringField(CODING_SCHEME_URI_VERSION_KEY_FIELD, 
			createCodingSchemeUriVersionKey(codingSchemeId, codingSchemeVersion), Field.Store.NO));
	d.add(new StringField(CODING_SCHEME_URI_VERSION_CODE_NAMESPACE_KEY_FIELD, 
			createCodingSchemeUriVersionCodeNamespaceKey(
					codingSchemeId, 
					codingSchemeVersion, 
					entity.getEntityCode(), 
					entity.getEntityCodeNamespace()), 
					Field.Store.NO));
//	d.add(new Field(edu.mayo.informatics.indexer.lucene.Index.UNIQUE_DOCUMENT_IDENTIFIER_FIELD,
//			codingSchemeName + "-" + entity.getEntityCode() + "-" + propertyId, Store.YES, Index.NOT_ANALYZED));
	return d;
}

private String createCodingSchemeUriVersionCodeNamespaceKey(String uri,
		String version, String code, String namespace) {
	return createCodingSchemeUriVersionKey(uri, version) + "-" + code + "-" + namespace;
}

public static String createCodingSchemeUriVersionKey(String uri, String version) {
	return uri + "-" + version;
}

public Document startNewDocument(String documentIdentifier, Document document) throws InvalidValueException {
    if (documentIdentifier == null || documentIdentifier.length() < 1) {
        throw new InvalidValueException("Document Identifier.is required");
    }
    document.add(new StringField(edu.mayo.informatics.indexer.lucene.Index.UNIQUE_DOCUMENT_IDENTIFIER_FIELD,
            documentIdentifier, Store.YES));
    return document;
}

public void indexBlockDocs(IndexWriter writer, List<Document> docs) throws IOException{ 
	
	writer.addDocuments(docs);
	writer.commit();
	writer.close();
	
}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
