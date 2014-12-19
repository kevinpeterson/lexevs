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

import org.apache.commons.io.FileUtils;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;

import java.io.IOException;

public class LuceneDirectoryFactory implements FactoryBean {

	private String indexName;
	
	private Resource indexDirectory;
	
	private LuceneDirectoryCreator luceneDirectoryCreator;

    public LuceneDirectoryFactory() {
        super();
    }

    public LuceneDirectoryFactory(String indexName, Resource indexDirectory, LuceneDirectoryCreator luceneDirectoryCreator) {
        super();
        this.indexName = indexName;
        this.indexDirectory = indexDirectory;
        this.luceneDirectoryCreator = luceneDirectoryCreator;
    }

    @Override
	public Object getObject() throws Exception {
		return luceneDirectoryCreator.getDirectory(indexName, indexDirectory.getFile());
	}
	
	@Override
	public Class<?> getObjectType() {
		return NamedDirectory.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexDirectory(Resource indexDirectory) {
		this.indexDirectory = indexDirectory;
	}

	public Resource getIndexDirectory() {
		return indexDirectory;
	}
	
	public void setLuceneDirectoryCreator(LuceneDirectoryCreator luceneDirectoryCreator) {
		this.luceneDirectoryCreator = luceneDirectoryCreator;
	}

	public LuceneDirectoryCreator getLuceneDirectoryCreator() {
		return luceneDirectoryCreator;
	}

	public static class NamedDirectory {
		private Directory directory;
		private String indexName;
		private IndexSearcher indexSearcher;
		private IndexReader indexReader;

		public NamedDirectory(Directory directory, String indexName) {
			super();
			this.directory = directory;
			this.indexName = indexName;
			try {
				this.initIndexDirectory(directory);
				this.indexReader = this.createIndexReader(directory);
				this.indexSearcher = this.createIndexSearcher(this.indexReader);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		public Directory getDirectory() {
			return directory;
		}
		public void setDirectory(Directory directory) {
			this.directory = directory;
		}
		public String getIndexName() {
			return indexName;
		}
		public void setIndexName(String indexName) {
			this.indexName = indexName;
		}
		
		public IndexSearcher getIndexSearcher() {
			return indexSearcher;
		}
		public void setIndexSearcher(IndexSearcher indexSearcher) {
			this.indexSearcher = indexSearcher;
		}
		public IndexReader getIndexReader() {
			return indexReader;
		}
		public void setIndexReader(IndexReader indexReader) {
			this.indexReader = indexReader;
		}
		
		protected IndexSearcher createIndexSearcher(IndexReader indexReader) throws Exception {
			return new IndexSearcher(indexReader);
		}

		protected IndexReader createIndexReader(Directory directory) throws Exception {
			IndexReader reader = IndexReader.open(directory);
			return reader;
		}

		private void initIndexDirectory(Directory directory) throws IOException, LockObtainFailedException {
			if(!DirectoryReader.indexExists(directory)){
				IndexWriter writer = new IndexWriter(
						directory, 
						new IndexWriterConfig(
                                Version.LATEST,
                                LuceneLoaderCode.getAnaylzer()));

				writer.close();
			}
		}
		
		public void refresh() {
			try {
				IndexReader reopenedReader = this.indexReader;
				
				if(reopenedReader != this.indexReader){
					this.indexReader.close();
					this.indexReader = this.createIndexReader(this.directory);
				}
				
				this.indexSearcher = this.createIndexSearcher(this.indexReader);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		protected void remove() {
			try {
				this.indexReader.close();

				if(this.directory instanceof FSDirectory) {
                    FSDirectory dir = (FSDirectory)this.directory;
					FileUtils.deleteDirectory(dir.getDirectory());
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}