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
package org.lexevs.dao.index.service.entity;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.concepts.Entity;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.lexevs.dao.index.access.IndexDaoManager;
import org.lexevs.dao.index.indexer.EntityIndexer;
import org.lexevs.dao.index.indexer.IndexCreator;
import org.lexevs.dao.index.indexer.IndexCreator.EntityIndexerProgressCallback;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;
import org.lexevs.dao.index.indexer.MetaData;
import org.lexevs.dao.index.indexregistry.IndexRegistry;
import org.lexevs.registry.service.Registry;
import org.lexevs.system.model.LocalCodingScheme;
import org.lexevs.system.service.SystemResourceService;

import java.util.List;
import java.util.Set;

/**
 * The Class LuceneEntityIndexService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LuceneEntityIndexService implements EntityIndexService {
	
	/** The index dao manager. */
	private IndexDaoManager indexDaoManager;
	
	/** The index creator. */
	private IndexCreator indexCreator;
	
	/** The index creator. */
	private EntityIndexer entityIndexer;
	
	private SystemResourceService systemResourceService;
	
	private MetaData metaData;
	
	private IndexRegistry indexRegistry;
	
	private Registry registry;

	@Override
	public String getIndexName(String codingSchemeUri,
			String codingSchemeVersion) {
		return indexDaoManager.getEntityDao(codingSchemeUri, codingSchemeVersion).
			getIndexName(codingSchemeUri, codingSchemeVersion);
	}

	@Override
	public void optimizeAll() {
        /*
		for(RegistryEntry entry :
			registry.getAllRegistryEntriesOfType(ResourceType.CODING_SCHEME)){
			try {
				this.optimizeIndex(entry.getResourceUri(), entry.getResourceVersion());
			} catch (Exception e) {
				LoggerFactory.getLogger().warn("Error Optimizing Index for Coding Scheme URI: " + entry.getResourceUri() + " Version: " + entry.getResourceVersion() + "." +
						" Error reported was: " + e.getMessage() + ". Skipping...");
			}
		}
		
		boolean isCommonIndexOptimized = indexRegistry.getCommonLuceneIndexTemplate().executeInIndexReader(new IndexReaderCallback<Boolean>() {

			@Override
			public Boolean doInIndexReader(IndexReader indexReader)
					throws Exception {
				return indexReader.isOptimized();
			}
		});
		
		if(!isCommonIndexOptimized) {
			indexRegistry.getCommonLuceneIndexTemplate().optimize();
		}
		*/
	}
	
	@Override
	public void optimizeIndex(final String codingSchemeUri, final String codingSchemeVersion) {
        /*
		boolean isOptimized = indexRegistry.getLuceneIndexTemplate(codingSchemeUri, codingSchemeVersion).executeInIndexReader(new IndexReaderCallback<Boolean>() {

			@Override
			public Boolean doInIndexReader(IndexReader indexReader)
					throws Exception {
				return indexReader.isOptimized();
			}
			
		});
		
		if(isOptimized) {
			LoggerFactory.getLogger().info("Index of URI: " + codingSchemeUri + " Version: " + codingSchemeVersion + " is already optimized.");
		} else {
			LoggerFactory.getLogger().info("Optimizing: " + codingSchemeUri + " Version: " + codingSchemeVersion + ".");
			indexDaoManager.getEntityDao(codingSchemeUri, codingSchemeVersion).optimizeIndex(codingSchemeUri, codingSchemeVersion);
		}
		*/
	}
	
	@Override
	public Document getDocumentById(
			String codingSchemeUri,
			String codingSchemeVersion, int id) {
		return this.getDocumentById(codingSchemeUri, codingSchemeVersion, id, null);
	}
	
	@Override
	public Document getDocumentById(
			String codingSchemeUri,
			String codingSchemeVersion, int id, Set<String> fields) {
		return indexDaoManager.getEntityDao(codingSchemeUri, codingSchemeVersion).
			getDocumentById(
					codingSchemeUri,
					codingSchemeVersion, 
					id,
                    fields);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.service.entity.EntityIndexService#createIndex(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference)
	 */
	public void createIndex(AbsoluteCodingSchemeVersionReference reference) {
		createIndex(reference, null);
	}
	
	public void createIndex(AbsoluteCodingSchemeVersionReference reference, EntityIndexerProgressCallback callback) {
		String indexName = indexCreator.index(reference, callback);

		indexRegistry.registerCodingSchemeIndex(
				reference.getCodingSchemeURN(), 
				reference.getCodingSchemeVersion(), 
				indexName);
	}
	
	public void deleteEntityFromIndex(
			String codingSchemeUri,
			String codingSchemeVersion, 
			Entity entity) {

		Term term = new Term(
					LuceneLoaderCode.CODING_SCHEME_URI_VERSION_CODE_NAMESPACE_KEY_FIELD, 
					LuceneLoaderCode.
						createCodingSchemeUriVersionCodeNamespaceKey(
							codingSchemeUri, 
							codingSchemeVersion, 
							entity.getEntityCode(), 
							entity.getEntityCodeNamespace()));
		indexDaoManager.getEntityDao(codingSchemeUri, codingSchemeVersion).
			deleteDocuments(codingSchemeUri, codingSchemeVersion, term);
	}
	
	@Override
	public void addEntityToIndex(String codingSchemeUri,
			String codingSchemeVersion, Entity entity) {

		List<Document> docs = 
			entityIndexer.indexEntity(codingSchemeUri, codingSchemeVersion, entity);
		
		indexDaoManager.getEntityDao(codingSchemeUri, codingSchemeVersion).
			addDocuments(codingSchemeUri, codingSchemeVersion, docs, entityIndexer.getAnalyzer());
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.service.entity.EntityIndexService#updateIndexForEntity(java.lang.String, java.lang.String, org.LexGrid.concepts.Entity)
	 */
	public void updateIndexForEntity(String codingSchemeUri,
			String codingSchemeVersion, Entity entity) {
		
		this.deleteEntityFromIndex(codingSchemeUri, codingSchemeVersion, entity);
		this.addEntityToIndex(codingSchemeUri, codingSchemeVersion, entity);
	}
	

	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.service.entity.EntityIndexService#getMatchAllDocsQuery(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference)
	 */
	@Override
	public Query getMatchAllDocsQuery(
			AbsoluteCodingSchemeVersionReference reference) {
		return indexDaoManager.getEntityDao(reference.getCodingSchemeURN(), reference.getCodingSchemeVersion()).
			getMatchAllDocsQuery(reference.getCodingSchemeURN(), reference.getCodingSchemeVersion());
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.service.entity.EntityIndexService#query(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference, java.util.List, java.util.List)
	 */
	public List<ScoreDoc> query(
            Set<AbsoluteCodingSchemeVersionReference> codingSchemes,
            List<? extends Query> combinedQueries, List<? extends Query> individualQueries){
        return null; //TODO:
		//return indexDaoManager.getEntityDao(codingSchemeUri, codingSchemeVersion).
		//	query(codingSchemeUri, codingSchemeVersion, combinedQueries, individualQueries);
	}
	
	@Override
	public List<ScoreDoc> query(Set<AbsoluteCodingSchemeVersionReference> codingSchemes, Query query){
		return null; //TODO:
		//return indexDaoManager.getEntityDao(codingSchemeUri, version).
		//	query(codingSchemeUri, version, query);
	}

	@Override
	public Filter getBoundaryDocsHitAsAWholeFilter(
			String codingSchemeUri,
			String version, 
			Query query) {
		return indexDaoManager.getEntityDao(codingSchemeUri, version).
			getBoundaryDocsHitAsAWholeFilter(codingSchemeUri, version, query);
	}
	
	@Override
	public Filter getCodingSchemeFilter(String uri, String version) {
		return indexDaoManager.getEntityDao(uri, version).getCodingSchemeFilter(uri, version);
	}

	/**
	 * Sets the index dao manager.
	 * 
	 * @param indexDaoManager the new index dao manager
	 */
	public void setIndexDaoManager(IndexDaoManager indexDaoManager) {
		this.indexDaoManager = indexDaoManager;
	}

	/**
	 * Gets the index dao manager.
	 * 
	 * @return the index dao manager
	 */
	public IndexDaoManager getIndexDaoManager() {
		return indexDaoManager;
	}


	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.service.entity.EntityIndexService#dropIndex(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference)
	 */
	public void dropIndex(AbsoluteCodingSchemeVersionReference reference) {
		this.doDropIndex(reference);
	
		try {
			metaData.removeIndexMetaDataValue(this.getCodingSchemeKey(reference));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected void doDropIndex(AbsoluteCodingSchemeVersionReference reference) {
		String codingSchemeUri = reference.getCodingSchemeURN();
		String codingSchemeVersion = reference.getCodingSchemeVersion();
		
		String indexName = this.getIndexName(codingSchemeUri, codingSchemeVersion);
		
		if(indexName.equals(this.indexRegistry.getCommonIndexName())) {
			Term term = new Term(
					LuceneLoaderCode.CODING_SCHEME_URI_VERSION_KEY_FIELD,
					LuceneLoaderCode.createCodingSchemeUriVersionKey(
							codingSchemeUri, codingSchemeVersion));
			
			indexDaoManager.getEntityDao(
					codingSchemeUri, 
					codingSchemeVersion).deleteDocuments(codingSchemeUri, codingSchemeVersion, term);
			
			this.indexRegistry.unRegisterCodingSchemeIndex(reference.getCodingSchemeURN(), reference.getCodingSchemeVersion());
		} else {
			this.indexRegistry.destroyIndex(indexName);
		}	
	}

	@Override
	public boolean doesIndexExist(AbsoluteCodingSchemeVersionReference reference) {
		String key = this.getCodingSchemeKey(reference);
		try {
			return StringUtils.isNotBlank(metaData.getIndexMetaDataValue(key));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected String getCodingSchemeKey(AbsoluteCodingSchemeVersionReference reference) {
		try {
			String codingSchemeName =
				systemResourceService.getInternalCodingSchemeNameForUserCodingSchemeName(
						reference.getCodingSchemeURN(), 
						reference.getCodingSchemeVersion());
			
			return this.getCodingSchemeKey(codingSchemeName, reference.getCodingSchemeVersion());
		} catch (LBParameterException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected String getCodingSchemeKey(String codingSchemeName, String version) {
		LocalCodingScheme lcs = LocalCodingScheme.getLocalCodingScheme(codingSchemeName, version);
			
		return lcs.getKey();
	}

	/**
	 * Gets the index creator.
	 * 
	 * @return the index creator
	 */
	public IndexCreator getIndexCreator() {
		return indexCreator;
	}

	/**
	 * Sets the index creator.
	 * 
	 * @param indexCreator the new index creator
	 */
	public void setIndexCreator(IndexCreator indexCreator) {
		this.indexCreator = indexCreator;
	}

	public SystemResourceService getSystemResourceService() {
		return systemResourceService;
	}

	public void setSystemResourceService(SystemResourceService systemResourceService) {
		this.systemResourceService = systemResourceService;
	}

	public void setMetaData(MetaData metaData) {
		this.metaData = metaData;
	}

	public MetaData getMetaData() {
		return metaData;
	}

	public void setEntityIndexer(EntityIndexer entityIndexer) {
		this.entityIndexer = entityIndexer;
	}

	public EntityIndexer getEntityIndexer() {
		return entityIndexer;
	}

	public IndexRegistry getIndexRegistry() {
		return indexRegistry;
	}

	public void setIndexRegistry(IndexRegistry indexRegistry) {
		this.indexRegistry = indexRegistry;
	}

	public Registry getRegistry() {
		return registry;
	}

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}
}