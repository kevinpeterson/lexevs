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
package org.lexevs.cts2.query;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.SortOption;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.dto.ResolvedValueSetDefinition;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;

/**
 * LexEVS Implementation of CTS2 Value Set Query Operation.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class ValueSetQueryOperationImpl implements ValueSetQueryOperation {
	private LexEVSValueSetDefinitionServices lexEVSValueSetService_;
	private ValueSetDefinitionService vsdDBService_ = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getValueSetDefinitionService();
	private DatabaseServiceManager databaseServiceManager_ = LexEvsServiceLocator.getInstance().getDatabaseServiceManager();

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.query.ValueSetQueryOperation#checkConceptValueSetMembership(java.lang.String, java.net.URI, org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean checkConceptValueSetMembership(String conceptCode, URI entityCodeNamespace,
			AbsoluteCodingSchemeVersionReference codeSystemAndVersion,
			String valueSetId, String valueSetVersion, String versionTag) throws LBException {
		if (StringUtils.isEmpty(conceptCode) || StringUtils.isEmpty(valueSetId) || codeSystemAndVersion == null)
		{
			throw new LBException("Invalid parametes. ConceptCode, codeSystemAndVersion or ValueSetId can not be empty");
		}
		
		if (!validateCodeSystem(codeSystemAndVersion.getCodingSchemeURN(), codeSystemAndVersion.getCodingSchemeVersion()))
		{
			throw new LBException("No code system found with id : '" + codeSystemAndVersion.getCodingSchemeURN() + 
						"' and version : '" + codeSystemAndVersion.getCodingSchemeVersion() + "'");
		}
		
		if (!validateValueSet(valueSetId, valueSetVersion))
		{
			throw new LBException("No Value Set found with id : '" + valueSetId + 
					"' and version : '" + valueSetVersion + "'");
		}
		
		AbsoluteCodingSchemeVersionReferenceList csVersionList = new AbsoluteCodingSchemeVersionReferenceList();
		csVersionList.addAbsoluteCodingSchemeVersionReference(codeSystemAndVersion);
		
		try {
			AbsoluteCodingSchemeVersionReference csVersion = getValueSetService().isEntityInValueSet(conceptCode, entityCodeNamespace, new URI(valueSetId), valueSetVersion, csVersionList, versionTag);
			if (csVersion != null && csVersion.getCodingSchemeURN() != null)
				return true;
		} catch (URISyntaxException e) {
			throw new LBException("Problem processing Value Set Query Operation : ", e);
		}
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.query.ValueSetQueryOperation#listValueSetsWithConceptCode(java.lang.String, java.net.URI, org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList, java.lang.String)
	 */
	public List<String> listValueSetsWithConceptCode(String conceptCode, URI entityCodeNamespace,
			AbsoluteCodingSchemeVersionReferenceList csVersionList, String versionTag) throws LBException {
		if (StringUtils.isEmpty(conceptCode))
		{
			throw new LBException("Invalid parametes. ConceptCode can not be empty");
		}
		
		return getValueSetService().listValueSetsWithEntityCode(conceptCode, entityCodeNamespace, csVersionList, versionTag);
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.query.ValueSetQueryOperation#checkValueSetSubsumption(java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList, java.lang.String)
	 */
	@Override
	public boolean checkValueSetSubsumption(String childValueSetId,
			String childValueSetVersion, String parentValueSetId,
			String parentValueSetVersion, AbsoluteCodingSchemeVersionReferenceList csVersionList, String versionTag) throws LBException {
		
		if (StringUtils.isEmpty(childValueSetId))
			throw new LBException("Invalid parameters. ChildValueSetId can not be empty");
		
		if (StringUtils.isEmpty(parentValueSetId))
			throw new LBException("Invalid parameters. ParentValueSetId can not be empty");
		
		boolean isSubset = false;
		try {
			isSubset = getValueSetService().isSubSet(new URI(childValueSetId), new URI(parentValueSetId), csVersionList, versionTag);
		} catch (URISyntaxException e) {
			throw new LBException("Problem processing Value Set Query Operation : ", e);
		}
		return isSubset;
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.query.ValueSetQueryOperation#getValueSetDetails(java.lang.String, java.lang.String)
	 */
	@Override
	public ValueSetDefinition getValueSetDetails(String valueSetId,
			String valueSetVersion) throws LBException {
		if (StringUtils.isEmpty(valueSetId))
			throw new LBException("Invalid parameters. valueSetId can not be empty");
		ValueSetDefinition vsd = null;
		try {
			vsd = getValueSetService().getValueSetDefinition(new URI(valueSetId), valueSetVersion);
		} catch (URISyntaxException e) {
			throw new LBException("Problem processing Value Set Query Operation : ", e);
		}
		return vsd;
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.query.ValueSetQueryOperation#listValueSetContents(java.lang.String, java.lang.String, org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList, java.lang.String, org.LexGrid.LexBIG.DataModel.InterfaceElements.SortOption)
	 */
	@Override
	public ResolvedValueSetDefinition listValueSetContents(String valueSetId,
			String valueSetVersion,AbsoluteCodingSchemeVersionReferenceList csVersionList, String versionTag, 
			SortOption sortOption) throws LBException {
		if (StringUtils.isEmpty(valueSetId))
			throw new LBException("Invalid parameters. valueSetId can not be empty");
		
		ResolvedValueSetDefinition vsContents = null;
		SortOptionList sortOptionList = null;
		
		if (sortOption != null)
		{
			sortOptionList = new SortOptionList();
			sortOptionList.addEntry(sortOption);
		}
		try {
			vsContents = getValueSetService().resolveValueSetDefinition(new URI(valueSetId), valueSetVersion, csVersionList, versionTag, sortOptionList);
		} catch (URISyntaxException e) {
			throw new LBException("Problem processing Value Set Query Operation : ", e);
		}
		return vsContents;
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.query.ValueSetQueryOperation#listValueSets(java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.LexGrid.LexBIG.DataModel.InterfaceElements.SortOption)
	 */
	@Override
	public List<String> listValueSets(String codeSystemId,
			String conceptDomainId, String usageContextId, String codeSystemURI, SortOption sortOption)
			throws LBException {
		List<String> finalList = getValueSetService().listValueSetDefinitionURIs();
		
		if (StringUtils.isNotEmpty(codeSystemId))
		{
			finalList.retainAll(getValueSetService().getValueSetDefinitionURIsWithCodingScheme(codeSystemId, codeSystemURI));
		}
		
		if (StringUtils.isNotEmpty(conceptDomainId))
		{
			finalList.retainAll(getValueSetService().getValueSetDefinitionURIsWithConceptDomain(conceptDomainId, codeSystemURI));
		}
		
		if (StringUtils.isNotEmpty(usageContextId))
		{
			finalList.retainAll(getValueSetService().getValueSetDefinitionURIsWithConceptDomain(usageContextId, codeSystemURI));
		}
		
		if (sortOption != null && finalList != null)
		{
			if (sortOption.isAscending())
				Collections.sort(finalList, String.CASE_INSENSITIVE_ORDER);
			else
				Collections.sort(finalList, Collections.reverseOrder());
		}
		return finalList;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.query.ValueSetQueryOperation#listAllValueSets(org.LexGrid.LexBIG.DataModel.InterfaceElements.SortOption)
	 */
	@Override
	public List<String> listAllValueSets(SortOption sortOption)
			throws LBException {
		List<String> vsdURIs = getValueSetService().listValueSetDefinitionURIs();
		
		if (sortOption != null)
		{
			if (sortOption.isAscending())
				Collections.sort(vsdURIs, String.CASE_INSENSITIVE_ORDER);
			else
				Collections.sort(vsdURIs, Collections.reverseOrder());
		}
		return vsdURIs;
	}	
	
	private boolean validateCodeSystem(String csURI, String csVersion){
		CodingSchemeSummary csSummary = databaseServiceManager_.getCodingSchemeService().
			getCodingSchemeSummaryByUriAndVersion(csURI, csVersion);
		
		if (csSummary == null)
			return false;
		
		return true;
	}
	
	private boolean validateValueSet(String vsID, String vsVersion) throws LBRevisionException{
		ValueSetDefinition vsd = null;
		try {
			if (StringUtils.isNotEmpty(vsVersion))
				vsd = vsdDBService_.getValueSetDefinitionByRevision(vsID, vsVersion);
			else
				vsd = vsdDBService_.getValueSetDefinitionByUri(new URI(vsID));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		if (vsd == null)
			return false;
		
		return true;
	}
	
	private LexEVSValueSetDefinitionServices getValueSetService(){
		if (lexEVSValueSetService_ == null)
			lexEVSValueSetService_ = LexEVSValueSetDefinitionServicesImpl.defaultInstance();
		
		return lexEVSValueSetService_;
	}	
}