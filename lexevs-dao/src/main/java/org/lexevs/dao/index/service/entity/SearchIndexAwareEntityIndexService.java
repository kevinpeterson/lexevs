package org.lexevs.dao.index.service.entity;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.concepts.Entity;
import org.lexevs.dao.index.service.search.SearchIndexService;

public class SearchIndexAwareEntityIndexService extends LuceneEntityIndexService {

	private SearchIndexService searchIndexService;
	
	@Override
	public void optimizeAll() {
		super.optimizeAll();
		this.searchIndexService.optimize();
	}

	@Override
	public void optimizeIndex(String codingSchemeUri, String codingSchemeVersion) {
		super.optimizeIndex(codingSchemeUri, codingSchemeVersion);
		this.searchIndexService.optimize();
	}

	@Override
	public void updateIndexForEntity(String codingSchemeUri,
			String codingSchemeVersion, Entity entity) {
		super.updateIndexForEntity(codingSchemeUri, codingSchemeVersion, entity);
		this.searchIndexService.updateIndexForEntity(codingSchemeUri, codingSchemeVersion, entity);
	}

	@Override
	protected void doDropIndex(AbsoluteCodingSchemeVersionReference reference) {
		super.doDropIndex(reference);
		this.searchIndexService.dropIndex(reference);
	}

	public SearchIndexService getSearchIndexService() {
		return searchIndexService;
	}

	public void setSearchIndexService(SearchIndexService searchIndexService) {
		this.searchIndexService = searchIndexService;
	}

}

