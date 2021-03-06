/*
 * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
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
package org.cts2.profile.query;

import java.util.List;

import org.cts2.codesystemversion.CodeSystemVersionDirectory;
import org.cts2.codesystemversion.CodeSystemVersionList;
import org.cts2.core.EntityReference;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;
import org.cts2.service.core.types.ActiveOrAll;
import org.cts2.service.core.types.RestrictionType;
import org.cts2.uri.CodeSystemVersionDirectoryURI;

/**
 * The Interface CodeSystemVersionQuery.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface CodeSystemVersionQueryService extends BaseQueryService<CodeSystemVersionDirectoryURI>{
	
	//TODO: 'entities aren't supposed to be a List, I'm pretty sure -- but there is on 'EntityReferenceList' in the model... ask Harold.
	/**
	 * Restrict to entities.
	 *
	 * @param codeSystemQueryURI the code system query uri
	 * @param entities the entities
	 * @param allOrSome the all or some
	 * @param active the active
	 * @return the code system version directory uri
	 */
	public CodeSystemVersionDirectoryURI restrictToEntities(CodeSystemVersionDirectoryURI codeSystemQueryURI, List<EntityReference> entities, RestrictionType allOrSome, ActiveOrAll active);
	
	/**
	 * Resolve.
	 *
	 * @param codeSystemQueryURI the code system query uri
	 * @param queryControl the query control
	 * @param readContext the read context
	 * @return the code system version directory
	 */
	public CodeSystemVersionDirectory resolve(CodeSystemVersionDirectoryURI codeSystemQueryURI, QueryControl queryControl, ReadContext readContext);
	
	/**
	 * Resolve as list.
	 *
	 * @param codeSystemQueryURI the code system query uri
	 * @param queryControl the query control
	 * @param readContext the read context
	 * @return the code system version list
	 */
	public CodeSystemVersionList resolveAsList(CodeSystemVersionDirectoryURI codeSystemQueryURI, QueryControl queryControl, ReadContext readContext);
	
	/**
	 * Gets the code system versions.
	 *
	 * @return the code system versions
	 */
	public CodeSystemVersionDirectoryURI getCodeSystemVersions();
}
