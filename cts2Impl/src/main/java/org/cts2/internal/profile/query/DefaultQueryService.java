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

import org.cts2.profile.query.AssociationQueryService;
import org.cts2.profile.query.CodeSystemQueryService;
import org.cts2.profile.query.QueryService;
import org.cts2.profile.query.ValueSetDefinitionQueryService;

/**
 * The Class DefaultQueryService.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultQueryService implements QueryService{
	
	/** The code system query. */
	private CodeSystemQueryService codeSystemQueryService;
	
	/** The association query. */
	private AssociationQueryService associationQueryService;
	
	/** The value set definition query. */
	private ValueSetDefinitionQueryService valueSetDefinitionQueryService;

	public CodeSystemQueryService getCodeSystemQueryService() {
		return codeSystemQueryService;
	}

	public void setCodeSystemQueryService(
			CodeSystemQueryService codeSystemQueryService) {
		this.codeSystemQueryService = codeSystemQueryService;
	}

	public AssociationQueryService getAssociationQueryService() {
		return associationQueryService;
	}

	public void setAssociationQueryService(
			AssociationQueryService associationQueryService) {
		this.associationQueryService = associationQueryService;
	}

	public ValueSetDefinitionQueryService getValueSetDefinitionQueryService() {
		return valueSetDefinitionQueryService;
	}

	public void setValueSetDefinitionQueryService(
			ValueSetDefinitionQueryService valueSetDefinitionQueryService) {
		this.valueSetDefinitionQueryService = valueSetDefinitionQueryService;
	}
}
