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
package org.lexevs.dao.database.service.relation;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.relations.Relations;

public interface RelationService {
	public static final String INSERT_RELATION_ERROR = "INSERT-RELATION-ERROR";
	public static final String UPDATE_RELATION_ERROR = "UPDATE-RELATION-ERROR";
	public static final String REMOVE_RELATION_ERROR = "REMOVE-RELATION-ERROR";
	public static final String INSERT_RELATION_DEPENDENT_CHANGES_ERROR = "INSERT-RELATION-DEPENDENT-CHANGES-ERROR";
	public static final String INSERT_RELATION_VERSIONABLE_CHANGES_ERROR = "INSERT-RELATION-VERSIONABLE-CHANGES-ERROR";

/**
	 * Insert relation.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param relation the relation
	 */
public void insertRelation(String codingSchemeUri, String version, Relations relation);
	
	public void updateRelation(String codingSchemeUri, String version, Relations relation) throws LBException;
	
	public void removeRelation(String codingSchemeUri, String version, Relations relation);
	
	/**
	 * version API to revise relations.
	 * 
	 * @param codingSchemeUri
	 * @param version
	 * @param relation
	 * @throws LBException
	 */
	public void revise(String codingSchemeUri, String version,
			Relations relation) throws LBException;

	public Relations resolveRelationsByRevision(
			String codingSchemeURI,
			String version, 
			String relationsName, 
			String revisionId)
			throws LBRevisionException;
}