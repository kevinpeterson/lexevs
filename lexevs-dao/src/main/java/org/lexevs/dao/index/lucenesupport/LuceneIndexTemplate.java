/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.lexevs.dao.index.lucenesupport;

import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.StoredFieldVisitor;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.lexevs.dao.index.lucenesupport.BaseLuceneIndexTemplate.IndexReaderCallback;
import org.lexevs.dao.index.lucenesupport.BaseLuceneIndexTemplate.IndexSearcherCallback;
import org.lexevs.dao.index.lucenesupport.BaseLuceneIndexTemplate.IndexWriterCallback;

public interface LuceneIndexTemplate {

	public void addDocuments(List<Document> documents,
			Analyzer analyzer);

	public void removeDocuments(Term term);

	public void removeDocuments(Query query);

	public void search(Query query, Filter filter,
			Collector hitCollector);
	
	public void optimize();
	
	public int getMaxDoc();
	
	public Document getDocumentById(int id, Set<String> fieldsToAdd);
	
	public Document getDocumentById(int id);

	public String getIndexName();
	
	public <T> T executeInIndexReader(IndexReaderCallback<T> callback);
	
	public <T> T executeInIndexSearcher(IndexSearcherCallback<T> callback);

	public <T> T executeInIndexWriter(IndexWriterCallback<T> callback);
	
	public List<ScoreDoc> search(final Query query, final Filter filter);
}