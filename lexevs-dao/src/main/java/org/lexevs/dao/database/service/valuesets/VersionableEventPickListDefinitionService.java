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
package org.lexevs.dao.database.service.valuesets;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.naming.Mappings;
import org.LexGrid.valueSets.PickListDefinition;
import org.LexGrid.valueSets.PickListDefinitions;
import org.LexGrid.valueSets.PickListEntryNode;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.lexevs.dao.database.access.revision.RevisionDao;
import org.lexevs.dao.database.access.valuesets.PickListDao;
import org.lexevs.dao.database.access.valuesets.VSEntryStateDao;
import org.lexevs.dao.database.access.valuesets.VSPropertyDao.ReferenceType;
import org.lexevs.dao.database.service.AbstractDatabaseService;
import org.lexevs.dao.database.service.version.VersionableEventAuthoringService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.system.service.SystemResourceService;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class VersionableEventPickListDefinitionService.
 * 
 * @author <a href="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</a>
 *
 */
public class VersionableEventPickListDefinitionService extends AbstractDatabaseService implements
		PickListDefinitionService {

	VSPropertyService vsPropertyService = null;
	
	PickListEntryNodeService pickListEntryNodeService = null;
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.PickListDefinitionService#getPickListDefinitionByPickListId(java.lang.String)
	 */
	@Override
	public PickListDefinition getPickListDefinitionByPickListId(
			String pickListId) {
		return this.getDaoManager().getCurrentPickListDefinitionDao().getPickListDefinitionById(pickListId);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.PickListDefinitionService#getPickListDefinitionIdForValueSetDefinitionUri(java.lang.String)
	 */
	@Override
	public List<String> getPickListDefinitionIdForValueSetDefinitionUri(
			String valueSetDefURI) {
		return this.getDaoManager().getCurrentPickListDefinitionDao().getPickListDefinitionIdForValueSetDefinitionURI(valueSetDefURI);
	}

	@Override
	public List<String> getPickListDefinitionIdForEntityReference(
			String entityCode, String entityCodeNameSpace, String propertyId) {
		return this.getDaoManager().getCurrentPickListDefinitionDao().getPickListDefinitionIdForEntityReference(entityCode, entityCodeNameSpace, propertyId);
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.PickListDefinitionService#insertPickListDefinition(org.LexGrid.valueSets.PickListDefinition)
	 */
	@Transactional
//	@DatabaseErrorIdentifier(errorCode=INSERT_PICKLIST_ERROR)
	public void insertPickListDefinition(PickListDefinition definition, String systemReleaseUri, Mappings mappings) throws LBParameterException, LBException {
		SystemResourceService service = LexEvsServiceLocator.getInstance().getSystemResourceService();
		
		String pickListId = definition.getPickListId();
		if (service.containsPickListDefinitionResource(pickListId, null))
		{
			throw new LBException("Pick List definition with ID : " + pickListId + " ALREADY LOADED.");
		}
		
		// Register picklist id
		service.addPickListDefinitionResourceToSystem(definition.getPickListId(), null);
		
		PickListDao plDao = this.getDaoManager().getCurrentPickListDefinitionDao();
		// load pick list definition
		plDao.insertPickListDefinition(definition, systemReleaseUri, mappings);
		
//		this.fireCodingSchemeInsertEvent(definition);
	}
	
	@Transactional
//	@DatabaseErrorIdentifier(errorCode=INSERT_PICKLIST_ERROR)
	public void insertPickListDefinitions(PickListDefinitions definitions, String systemReleaseUri) {
		PickListDao plDao = this.getDaoManager().getCurrentPickListDefinitionDao();
		
		Mappings mappings = definitions.getMappings();
		
		for (PickListDefinition definition : definitions.getPickListDefinitionAsReference())
		{
			plDao.insertPickListDefinition(definition, systemReleaseUri, mappings);
		}
		
//		this.fireCodingSchemeInsertEvent(definition);
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.PickListDefinitionService#listPickListIds()
	 */
	@Override
	public List<String> listPickListIds() {
		return this.getDaoManager().getCurrentPickListDefinitionDao().getPickListIds();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.PickListDefinitionService#removePickListDefinitionByPickListId(java.lang.String)
	 */
	@Override
	public void removePickListDefinitionByPickListId(String pickListId) {
		PickListDao plDao = this.getDaoManager().getCurrentPickListDefinitionDao();
		
		plDao.removePickListDefinitionByPickListId(pickListId);
	}

	@Override
	public List<String> getPickListDefinitionIdForSupportedTagAndValue(
			String supportedTag, String value) {
		return this.getDaoManager().getCurrentPickListDefinitionDao()
				.getPickListDefinitionIdForSupportedTagAndValue(supportedTag,
						value);
	}

	@Override
	public void removePickListDefinition(PickListDefinition definition) {

		removePickListDefinitionByPickListId(definition.getPickListId());
	}

	@Override
	public void updatePickListDefinition(PickListDefinition definition) throws LBException {

		String pickListId = definition.getPickListId();
		
		PickListDao pickListDefDao = this.getDaoManager().getCurrentPickListDefinitionDao();
		
		String pickListDefUId = pickListDefDao.getPickListGuidFromPickListId(pickListId);
		
		String prevEntryStateUId = pickListDefDao.insertHistoryPickListDefinition(pickListDefUId, pickListId);
		
		String entryStateUId = pickListDefDao.updatePickListDefinition(pickListDefUId, definition);
		
		this.getDaoManager().getCurrentVsEntryStateDao().insertEntryState(
				entryStateUId, pickListDefUId, ReferenceType.PICKLISTDEFINITION.name(),
				prevEntryStateUId, definition.getEntryState());
		
		this.insertDependentChanges(definition);
	}

	@Override
	public void updateVersionableAttributes(PickListDefinition definition) throws LBException {

		String pickListId = definition.getPickListId();
		
		PickListDao pickListDefDao = this.getDaoManager().getCurrentPickListDefinitionDao();
		
		String pickListDefUId = pickListDefDao.getPickListGuidFromPickListId(pickListId);
		
		String prevEntryStateUId = pickListDefDao.insertHistoryPickListDefinition(pickListDefUId, pickListId);
		
		String entryStateUId = pickListDefDao.updateVersionableAttributes(pickListDefUId, definition);
		
		this.getDaoManager().getCurrentVsEntryStateDao().insertEntryState(
				entryStateUId, pickListDefUId, ReferenceType.PICKLISTDEFINITION.name(),
				prevEntryStateUId, definition.getEntryState());
		
		this.insertDependentChanges(definition);
	}

	@Override
	public void insertDependentChanges(PickListDefinition definition) throws LBException {

		String pickListId = definition.getPickListId();
		
		/* 1. Insert EntryState entry.*/
		
		if (definition.getEntryState().getChangeType() == ChangeType.DEPENDENT) {

			doAddPickListDefinitionDependentEntry(definition);
		}
		
		/* 2. Revise dependent pickList Entry nodes.*/
		
		PickListEntryNode[] pickListNode = definition.getPickListEntryNode();
		
		for (int i = 0; i < pickListNode.length; i++) {
			
			pickListEntryNodeService.revise(pickListId, pickListNode[i]);
		}
		
		/* 3. Revise dependent pickList definition properties.*/
		
		if (definition.getProperties() != null) {

			Property[] propertyList = definition.getProperties().getProperty();

			for (int i = 0; i < propertyList.length; i++) {
				vsPropertyService.revisePickListDefinitionProperty(pickListId,
						propertyList[i]);
			}
		}
	}

	@Override
	public void revise(PickListDefinition pickListDefinition, Mappings mapping,
			String releaseURI) throws LBException {
	
		if (validRevision(pickListDefinition)) {
			ChangeType changeType = pickListDefinition.getEntryState()
					.getChangeType();

			if (changeType == ChangeType.NEW) {

				this.insertPickListDefinition(pickListDefinition, releaseURI,
						mapping);
			} else if (changeType == ChangeType.REMOVE) {
				LexEvsServiceLocator.getInstance().getSystemResourceService().
					removePickListDefinitionResourceFromSystem(pickListDefinition.getPickListId(), null);
			} else if (changeType == ChangeType.MODIFY) {

				this.updatePickListDefinition(pickListDefinition);
			} else if (changeType == ChangeType.DEPENDENT) {

				this.insertDependentChanges(pickListDefinition);
			} else if (changeType == ChangeType.VERSIONABLE) {

				this.updateVersionableAttributes(pickListDefinition);
			}
		}
	}

	/**
	 * @return the vsPropertyService
	 */
	public VSPropertyService getVsPropertyService() {
		return vsPropertyService;
	}

	/**
	 * @param vsPropertyService the vsPropertyService to set
	 */
	public void setVsPropertyService(VSPropertyService vsPropertyService) {
		this.vsPropertyService = vsPropertyService;
	}

	/**
	 * @return the pickListEntryNodeService
	 */
	public PickListEntryNodeService getPickListEntryNodeService() {
		return pickListEntryNodeService;
	}

	/**
	 * @param pickListEntryNodeService the pickListEntryNodeService to set
	 */
	public void setPickListEntryNodeService(
			PickListEntryNodeService pickListEntryNodeService) {
		this.pickListEntryNodeService = pickListEntryNodeService;
	}

	private boolean validRevision(PickListDefinition pickListDefinition) throws LBException {
		
		if(  pickListDefinition == null) 
			throw new LBParameterException("pickListDefinition is null.");
		
		EntryState entryState = pickListDefinition.getEntryState();
	
		if (entryState == null) {
			throw new LBRevisionException("EntryState can't be null.");
		}
	
		ChangeType changeType = entryState.getChangeType();
	
		PickListDao pickListDao = this.getDaoManager()
				.getCurrentPickListDefinitionDao();

		String pickListDefUId = pickListDao
				.getPickListGuidFromPickListId(pickListDefinition
						.getPickListId());
		
		if (changeType == ChangeType.NEW) {
			if (entryState.getPrevRevision() != null) {
				throw new LBRevisionException(
						"Changes of type NEW are not allowed to have previous revisions.");
			}

			if (pickListDefUId != null) {
				throw new LBRevisionException(
						"The picklist definition being added already exist.");
			}

		} else {

			if (pickListDefUId == null) {
				throw new LBRevisionException(
						"The picklist definition being revised doesn't exist.");
			}

			String pickListDefLatestRevisionId = pickListDao
					.getLatestRevision(pickListDefUId);

			String currentRevision = entryState.getContainingRevision();
			String prevRevision = entryState.getPrevRevision();
			
			if (entryState.getPrevRevision() == null
					&& pickListDefLatestRevisionId != null
					&& !pickListDefLatestRevisionId.equals(currentRevision)
					&& !pickListDefLatestRevisionId
					.startsWith(VersionableEventAuthoringService.LEXGRID_GENERATED_REVISION)) {
				throw new LBRevisionException(
						"All changes of type other than NEW should have previous revisions.");
			} else if (pickListDefLatestRevisionId != null
					&& !pickListDefLatestRevisionId.equals(currentRevision)
					&& !pickListDefLatestRevisionId.equals(prevRevision)
					&& !pickListDefLatestRevisionId
					.startsWith(VersionableEventAuthoringService.LEXGRID_GENERATED_REVISION)) {
				throw new LBRevisionException(
						"Revision source is not in sync with the database revisions. "
								+ "Previous revision id does not match with the latest revision id of the picklist definition."
								+ "Please update the authoring instance with all the revisions and regenerate the source.");
			}
		}
			
		return true;
	}

	private void doAddPickListDefinitionDependentEntry(
			PickListDefinition definition) {
	
		String pickListId = definition.getPickListId();
		
		PickListDao pickListDefDao = this.getDaoManager()
				.getCurrentPickListDefinitionDao();
		
		VSEntryStateDao vsEntryStateDao = this.getDaoManager()
				.getCurrentVsEntryStateDao();
		
		String pickListDefUId = pickListDefDao
				.getPickListGuidFromPickListId(pickListId);
	
		String prevEntryStateUId = pickListDefDao
				.getPickListEntryStateUId(pickListDefUId);

		if (!pickListDefDao.entryStateExists(prevEntryStateUId)) {
			
			EntryState entryState = new EntryState();
	
			entryState.setChangeType(ChangeType.NEW);
			entryState.setRelativeOrder(0L);
	
			vsEntryStateDao.insertEntryState(prevEntryStateUId, pickListDefUId,
					ReferenceType.VALUESETDEFINITION.name(), null, entryState);
		}
		
		EntryState currentPLDEntryState = vsEntryStateDao.getEntryStateByUId(prevEntryStateUId);
		// if the exiting PLD entry change type is non-dependent, move to history table
		if (!currentPLDEntryState.getChangeType().equals(ChangeType.DEPENDENT))
		{
			prevEntryStateUId = pickListDefDao
					.insertHistoryPickListDefinition(pickListDefUId, pickListId);
		}
		
		String entryStateUId = this.getDaoManager().getCurrentVsEntryStateDao()
				.insertEntryState(pickListDefUId,
						ReferenceType.PICKLISTDEFINITION.name(),
						prevEntryStateUId, definition.getEntryState());
	
		pickListDefDao.updateEntryStateUId(pickListDefUId, entryStateUId);
	}

	@Override
	public PickListDefinition resolvePickListDefinitionByRevision(String pickListId,
			String revisionId, Integer sortType) throws LBRevisionException {
		PickListDao pickListDefDao = this.getDaoManager()
				.getCurrentPickListDefinitionDao();

		return pickListDefDao.resolvePickListByRevision(pickListId, revisionId,
				sortType);
	}

	@Override
	public PickListDefinition resolvePickListDefinitionByDate(String pickListId,
			Date date, Integer sortType) throws LBRevisionException {
		
		RevisionDao revisionDao = getDaoManager().getRevisionDao();

		String revisionId = revisionDao.getRevisionIdForDate(new Timestamp(date.getTime()));
		
		if( revisionId == null )
			return null;
		
		PickListDao pickListDefDao = this.getDaoManager()
				.getCurrentPickListDefinitionDao();
		
		return pickListDefDao.resolvePickListByRevision(pickListId, revisionId,
				sortType);
	}
}