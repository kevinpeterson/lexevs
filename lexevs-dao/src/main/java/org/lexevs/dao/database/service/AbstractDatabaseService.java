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
package org.lexevs.dao.database.service;

import org.LexGrid.commonTypes.Versionable;
import org.LexGrid.versions.types.ChangeType;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.service.event.DatabaseServiceEventSupport;
import org.lexevs.logging.Logger;

/**
 * The Class AbstractDatabaseService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class AbstractDatabaseService extends DatabaseServiceEventSupport {

	/** The dao manager. */
	private DaoManager daoManager;
	
	private Logger logger;

	/**
	 * Gets the coding scheme id.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 * 
	 * @return the coding scheme id
	 */
	protected String getCodingSchemeUId(String codingSchemeUri, String codingSchemeVersion){
		return daoManager.
			getCodingSchemeDao(codingSchemeUri, codingSchemeVersion).
			getCodingSchemeUIdByUriAndVersion(codingSchemeUri, codingSchemeVersion);
	}
	
	
	protected boolean isChangeTypeDependent(Versionable versionable) {
		return versionable.getEntryState() != null 
				&&
				versionable.getEntryState().getChangeType().equals(ChangeType.DEPENDENT);
	}
	
	protected boolean isChangeTypeRemove(Versionable versionable) {
		return versionable.getEntryState() != null 
				&&
				versionable.getEntryState().getChangeType().equals(ChangeType.REMOVE);
	}
	
	/**
	 * Gets the dao manager.
	 * 
	 * @return the dao manager
	 */
	public DaoManager getDaoManager() {
		return daoManager;
	}
	
	/**
	 * Sets the dao manager.
	 * 
	 * @param daoManager the new dao manager
	 */
	public void setDaoManager(DaoManager daoManager) {
		this.daoManager = daoManager;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public Logger getLogger() {
		return logger;
	}
}