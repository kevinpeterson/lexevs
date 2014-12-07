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
package org.lexevs.dao.index.indexer;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.concepts.Entity;
import org.apache.lucene.analysis.Analyzer;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.dao.index.access.IndexDaoManager;
import org.lexevs.dao.index.access.entity.EntityDao;
import org.lexevs.dao.index.factory.IndexLocationFactory;
import org.lexevs.dao.index.model.IndexableEntity;
import org.lexevs.dao.index.model.IndexedEntity;
import org.lexevs.dao.index.model.IndexedProperty;
import org.lexevs.system.constants.SystemVariables;
import org.lexevs.system.service.SystemResourceService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The Class EntityBatchingIndexCreator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityBatchingIndexCreator implements IndexCreator {
	
	/** The batch size. */
	private int batchSize = 1000;
	
	/** The entity service. */
	private EntityService entityService;
	
	/** The system resource service. */
	private SystemResourceService systemResourceService;
	
	/** The system variables. */
	private SystemVariables systemVariables;

	private IndexDaoManager indexDaoManager;
	
	private MetaData metaData;

	private Analyzer analyzer = LuceneLoaderCode.getAnaylzer();
	
	private Indexer<IndexedEntity> entityIndexer;

    private Indexer<IndexedProperty> propertyIndexer;
	
	//private EntityIndexer searchIndexer;
	
	private LgLoggerIF logger;
	
	@Override
	public String index(AbsoluteCodingSchemeVersionReference reference) {
		return this.index(reference, null, IndexOption.BOTH);
	}

	@Override
	public String index(AbsoluteCodingSchemeVersionReference reference, 
			EntityIndexerProgressCallback callback, boolean onlyRegister) {
		return this.index(reference, callback, onlyRegister, IndexOption.BOTH);
	}

	@Override
	public String index(AbsoluteCodingSchemeVersionReference reference, EntityIndexerProgressCallback callback) {	
		return this.index(reference, callback, IndexOption.BOTH);
	}

	@Override
	public String index(AbsoluteCodingSchemeVersionReference reference, IndexOption option) {
		return this.index(reference, null, option);
	}

	@Override
	public String index(AbsoluteCodingSchemeVersionReference reference,
			EntityIndexerProgressCallback callback, IndexOption option) {
		return this.index(reference, callback, false, option);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.indexer.IndexCreator#index(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference)
	 */
	public String index(AbsoluteCodingSchemeVersionReference reference, EntityIndexerProgressCallback callback, boolean onlyRegister, IndexOption option) {	
		String indexName = this.getIndexName(reference);
		
		//addEntityIndexMetadata(reference, indexName, entityIndexer.getIndexerFormatVersion().getModelFormatVersion());
		//addSearchIndexMetadata(reference, this.getSearchIndexName(), searchIndexer.getIndexerFormatVersion().getModelFormatVersion());

		if(!onlyRegister) {

			EntityDao entityIndexService = indexDaoManager.getEntityDao(reference.getCodingSchemeURN(), reference.getCodingSchemeVersion());

			int totalIndexedEntities = 0;

			int position = 0;
			for(
					List<? extends Entity> entities = 
						entityService.getEntities(reference.getCodingSchemeURN(), reference.getCodingSchemeVersion(), position, batchSize);
					entities.size() > 0; 
					entities = entityService.getEntities(reference.getCodingSchemeURN(), reference.getCodingSchemeVersion(), position += batchSize, batchSize)) {

				List<IndexableEntity> blockIndexables = new ArrayList<IndexableEntity>();

				for(Entity entity : entities) {
                    blockIndexables.add(new IndexableEntity(entity, this.entityIndexer, this.propertyIndexer));
					totalIndexedEntities++;

					if(callback != null) {
						callback.onEntityIndex(entity);
					}

					if(totalIndexedEntities % 1000 == 0) {
						this.getLogger().info("Indexed: " + totalIndexedEntities + " Entities.");
					}
				}

				this.getLogger().info("Flusing " + blockIndexables.size() + " Documents to the Index.");

				entityIndexService.addDocuments(
						reference.getCodingSchemeURN(), 
						reference.getCodingSchemeVersion(),
                        blockIndexables, analyzer);
			}

			this.getLogger().info("Indexing Complete. Indexed: " + totalIndexedEntities + " Entities.");
		}
		
		return indexName;
	}

	/**
	 * Adds the index metadata.
	 * 
	 * @param reference the reference
	 * @param indexName the index name
	 * @param indexVersion the index version
	 */
	protected void addEntityIndexMetadata(
			AbsoluteCodingSchemeVersionReference reference, String indexName, String indexVersion) {
		try {	  
			String codingSchemeName = 
				systemResourceService.getInternalCodingSchemeNameForUserCodingSchemeName(reference.getCodingSchemeURN(), reference.getCodingSchemeVersion());

			metaData.setIndexMetaDataValue(codingSchemeName + "[:]" + reference.getCodingSchemeVersion(), indexName);

			metaData.setIndexMetaDataValue(indexName, "lgModel", indexVersion);
			metaData.setIndexMetaDataValue(indexName, "has 'Norm' fields", false + "");
			metaData.setIndexMetaDataValue(indexName, "has 'Double Metaphone' fields", true + "");
			metaData.setIndexMetaDataValue(indexName, "indexing started", "");
			metaData.setIndexMetaDataValue(indexName, "indexing finished", "");

			metaData.rereadFile(true);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected void addSearchIndexMetadata(
			AbsoluteCodingSchemeVersionReference reference, String indexName, String indexVersion) {
		try {	  
			metaData.setIndexMetaDataValue(indexName, "lgModel", indexVersion);

			metaData.rereadFile(true);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected String getIndexName(AbsoluteCodingSchemeVersionReference reference) {
		if(systemVariables.getIsSingleIndex()){
			return IndexLocationFactory.DEFAULT_SINGLE_INDEX_NAME;
		} else {
			return UUID.randomUUID().toString();
		}
	}
	
	protected String getSearchIndexName() {
		return "SearchIndex";
	}

	/**
	 * Sets the entity service.
	 * 
	 * @param entityService the new entity service
	 */
	public void setEntityService(EntityService entityService) {
		this.entityService = entityService;
	}

	/**
	 * Gets the entity service.
	 * 
	 * @return the entity service
	 */
	public EntityService getEntityService() {
		return entityService;
	}

	/**
	 * Gets the batch size.
	 * 
	 * @return the batch size
	 */
	public int getBatchSize() {
		return batchSize;
	}

	/**
	 * Sets the batch size.
	 * 
	 * @param batchSize the new batch size
	 */
	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}

	/**
	 * Sets the system resource service.
	 * 
	 * @param systemResourceService the new system resource service
	 */
	public void setSystemResourceService(SystemResourceService systemResourceService) {
		this.systemResourceService = systemResourceService;
	}

	/**
	 * Gets the system resource service.
	 * 
	 * @return the system resource service
	 */
	public SystemResourceService getSystemResourceService() {
		return systemResourceService;
	}

	/**
	 * Gets the system variables.
	 * 
	 * @return the system variables
	 */
	public SystemVariables getSystemVariables() {
		return systemVariables;
	}

	/**
	 * Sets the system variables.
	 * 
	 * @param systemVariables the new system variables
	 */
	public void setSystemVariables(SystemVariables systemVariables) {
		this.systemVariables = systemVariables;
	}

	

	public Analyzer getAnalyzer() {
		return analyzer;
	}

	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	public void setLogger(LgLoggerIF logger) {
		this.logger = logger;
	}

	public LgLoggerIF getLogger() {
		return logger;
	}

	public void setEntityIndexer(EntityIndexer entityIndexer) {
		this.entityIndexer = entityIndexer;
	}

	public void setMetaData(MetaData metaData) {
		this.metaData = metaData;
	}

	public MetaData getMetaData() {
		return metaData;
	}

	public void setIndexDaoManager(IndexDaoManager indexDaoManager) {
		this.indexDaoManager = indexDaoManager;
	}

	public IndexDaoManager getIndexDaoManager() {
		return indexDaoManager;
	}
}