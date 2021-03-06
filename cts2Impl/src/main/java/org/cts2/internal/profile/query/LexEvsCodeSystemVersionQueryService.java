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
package org.cts2.internal.profile.query;

import java.util.List;

import org.cts2.codesystemversion.CodeSystemVersionDirectory;
import org.cts2.codesystemversion.CodeSystemVersionList;
import org.cts2.core.EntityReference;
import org.cts2.profile.query.CodeSystemVersionQueryService;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;
import org.cts2.service.core.types.ActiveOrAll;
import org.cts2.service.core.types.RestrictionType;
import org.cts2.uri.CodeSystemVersionDirectoryURI;

/**
 * The Class LexEvsCodeSystemVersionQuery.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LexEvsCodeSystemVersionQueryService extends AbstractBaseQueryService<CodeSystemVersionDirectoryURI> 
	implements CodeSystemVersionQueryService {
	
	/* (non-Javadoc)
	 * @see org.cts2.profile.query.CodeSystemVersionQueryService#restrictToEntities(org.cts2.uri.CodeSystemVersionDirectoryURI, java.util.List, org.cts2.service.core.types.RestrictionType, org.cts2.service.core.types.ActiveOrAll)
	 */
	@Override
	public CodeSystemVersionDirectoryURI restrictToEntities(
			CodeSystemVersionDirectoryURI codeSystemVersionDirectoryURI,
			List<EntityReference> entities, 
			RestrictionType allOrSome,
			ActiveOrAll active) {
		return codeSystemVersionDirectoryURI.restrictToEntities(entities, allOrSome, active);
	}

	/* (non-Javadoc)
	 * @see org.cts2.profile.query.CodeSystemVersionQueryService#resolve(org.cts2.uri.CodeSystemVersionDirectoryURI, org.cts2.service.core.QueryControl, org.cts2.service.core.ReadContext)
	 */
	@Override
	public CodeSystemVersionDirectory resolve(
			CodeSystemVersionDirectoryURI codeSystemVersionDirectoryURI,
			QueryControl queryControl, 
			ReadContext readContext) {
		return codeSystemVersionDirectoryURI.get(queryControl, readContext, CodeSystemVersionDirectory.class);
	}

	/* (non-Javadoc)
	 * @see org.cts2.profile.query.CodeSystemVersionQueryService#resolveAsList(org.cts2.uri.CodeSystemVersionDirectoryURI, org.cts2.service.core.QueryControl, org.cts2.service.core.ReadContext)
	 */
	@Override
	public CodeSystemVersionList resolveAsList(
			CodeSystemVersionDirectoryURI codeSystemVersionDirectoryURI,
			QueryControl queryControl, 
			ReadContext readContext) {
		return codeSystemVersionDirectoryURI.get(queryControl, readContext, CodeSystemVersionList.class);	
	}

	/* (non-Javadoc)
	 * @see org.cts2.profile.query.CodeSystemVersionQueryService#getCodeSystemVersions()
	 */
	@Override
	public CodeSystemVersionDirectoryURI getCodeSystemVersions() {
		return this.getDirectoryURIFactory().getDirectoryURI();
	}
}
