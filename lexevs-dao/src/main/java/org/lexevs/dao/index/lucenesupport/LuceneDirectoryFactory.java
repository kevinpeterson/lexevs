package org.lexevs.dao.index.lucenesupport;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;

public class LuceneDirectoryFactory implements FactoryBean {

	private String indexName;
	
	private Resource indexDirectory;

	@Override
	public Object getObject() throws Exception {
		Directory directory = FSDirectory.getDirectory(this.indexDirectory.getFile());

		return new NamedDirectory(
				directory, indexName);
	}

	@Override
	public Class getObjectType() {
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
	
	public static class NamedDirectory {
		private Directory directory;
		private String indexName;
		
		public NamedDirectory(Directory directory, String indexName) {
			super();
			this.directory = directory;
			this.indexName = indexName;
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
	}
}
