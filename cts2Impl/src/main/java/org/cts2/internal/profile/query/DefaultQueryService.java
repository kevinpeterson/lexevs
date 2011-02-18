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

import org.cts2.profile.query.AssociationQuery;
import org.cts2.profile.query.CodeSystemQuery;
import org.cts2.profile.query.QueryService;

/**
 * The Class DefaultQueryService.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultQueryService implements QueryService{
	
	/** The code system query. */
	private CodeSystemQuery codeSystemQuery;
	
	/** The association query. */
	private AssociationQuery associationQuery;


	/* (non-Javadoc)
	 * @see org.cts2.profile.query.QueryService#getCodeSystemQuery()
	 */
	public CodeSystemQuery getCodeSystemQuery() {
		return codeSystemQuery;
	}

	/**
	 * Sets the code system query.
	 *
	 * @param codeSystemQuery the new code system query
	 */
	public void setCodeSystemQuery(CodeSystemQuery codeSystemQuery) {
		this.codeSystemQuery = codeSystemQuery;
	}

	@Override
	public AssociationQuery getAssociationQuery() {
		// TODO Auto-generated method stub
		return associationQuery;
	}
	
	
	/**
	 * Sets the association query
	 * 
	 * @param associationQuery is the new association query
	 */
	public void setAssociationQuery(AssociationQuery associationQuery) {
		this.associationQuery = associationQuery;
	}
}
