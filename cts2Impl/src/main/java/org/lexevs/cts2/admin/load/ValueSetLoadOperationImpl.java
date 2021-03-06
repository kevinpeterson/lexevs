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
package org.lexevs.cts2.admin.load;

import java.net.URI;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.apache.commons.lang.StringUtils;
import org.lexevs.cts2.BaseService;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;

import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

/**
 * Implementation of LexEVS CTS 2 Value Set Load Operation.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class ValueSetLoadOperationImpl extends BaseService implements ValueSetLoadOperation {
	
    private LexEVSValueSetDefinitionServices vsdService_;
  
	/* (non-Javadoc)
	 * @see org.lexevs.cts2.admin.load.ValueSetLoadOperation#load(java.net.URI, java.net.URI, java.lang.String, java.lang.Boolean)
	 */
	@Override
	public URNVersionPair[] load(URI source, URI releaseURI, String loaderName,
			Boolean stopOnErrors) throws LBException {
		return this.getLexEvsCTS2().getAdminOperation().getCodeSystemLoadOperation().load(source, null, null, loaderName, stopOnErrors, true, false, null, null);
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.admin.load.ValueSetLoadOperation#load(org.LexGrid.valueSets.ValueSetDefinition, java.net.URI, java.lang.Boolean)
	 */
	@Override
	public String load(ValueSetDefinition valueSetDefinition, URI releaseURI, Boolean stopOnErrors) throws LBException {
		if (valueSetDefinition == null)
			throw new LBException("Value Set Definition object can not be empty");
		
		if (StringUtils.isEmpty(valueSetDefinition.getValueSetDefinitionURI()))
			throw new LBException("Value Set Definition URI can not be empty");
		
		vsdService_ = LexEVSValueSetDefinitionServicesImpl.defaultInstance();
		vsdService_.loadValueSetDefinition(valueSetDefinition, releaseURI == null ? null : releaseURI.toString(), null);
		return valueSetDefinition.getValueSetDefinitionURI();
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.admin.load.ValueSetLoadOperation#getSupportedLoaderNames()
	 */
	@Override
	public List<String> getSupportedLoaderNames() throws LBException {
		return this.getLexEvsCTS2().getSupportedLoaderNames();
	}
}