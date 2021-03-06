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
package org.lexevs.cts2.admin.export;

import static junit.framework.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.LBConstants.MatchAlgorithms;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.relations.AssociationPredicate;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lexevs.cts2.LexEvsCTS2Impl;
import org.lexevs.cts2.admin.load.CodeSystemLoadOperation;
import org.lexevs.cts2.test.Cts2TestConstants;

import edu.mayo.informatics.lexgrid.convert.formats.Option;

/**
 * @author m004181
 *
 */
public class CodeSystemExportOperationImplTest {

	private CodeSystemExportOperationImpl operation = new CodeSystemExportOperationImpl();
	
	private File exportFile ;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		CodeSystemLoadOperation csLoadOp = LexEvsCTS2Impl.defaultInstance().getAdminOperation().getCodeSystemLoadOperation();
		
		try {
			csLoadOp.load(new File("resources/testData/cts2/Cts2Automobiles.xml").toURI(), null, null, "LexGrid_Loader", true, true, true, "DEV", true);
		} catch (LBException e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		AbsoluteCodingSchemeVersionReference ref = 
			Constructors.createAbsoluteCodingSchemeVersionReference(Cts2TestConstants.CTS2_AUTOMOBILES_URI, Cts2TestConstants.CTS2_AUTOMOBILES_VERSION);
		
		lbs.getServiceManager(null).deactivateCodingSchemeVersion(ref, null);
		
		lbs.getServiceManager(null).removeCodingSchemeVersion(ref);
	}
	
	@Before
	public void createFile() {
		exportFile = new File(System.getProperty("java.io.tmpdir") + File.separator + 
				Cts2TestConstants.CTS2_AUTOMOBILES_NAME + "_" + Cts2TestConstants.CTS2_AUTOMOBILES_VERSION + ".xml");
	}
	
	@After
	public void deleteFile() {
		assertTrue(exportFile.delete());
	}
	
	/**
	 * Test method for {@link org.lexevs.cts2.admin.export.CodeSystemExportOperationImpl#exportCodeSystemContent(java.lang.String, java.lang.String, java.net.URI, java.lang.String)}.
	 * @throws URISyntaxException 
	 * @throws LBException 
	 * @throws FileNotFoundException 
	 * @throws ValidationException 
	 * @throws MarshalException 
	 */
	@Test
	public void testExportCodeSystemContent() throws LBException, URISyntaxException, MarshalException, ValidationException, FileNotFoundException {
		
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		
		org.LexGrid.LexBIG.Impl.exporters.LexGridExport exporter;
        try {
            exporter = (org.LexGrid.LexBIG.Impl.exporters.LexGridExport)lbs.getServiceManager(null).getExporter(org.LexGrid.LexBIG.Impl.exporters.LexGridExport.name);
        } catch (LBException e) {
            throw new RuntimeException(e);
        }
        
		org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph cng = lbs
				.getNodeGraph(Cts2TestConstants.CTS2_AUTOMOBILES_URI, Constructors
						.createCodingSchemeVersionOrTagFromVersion(Cts2TestConstants.CTS2_AUTOMOBILES_VERSION), null);

		org.LexGrid.LexBIG.LexBIGService.CodedNodeSet cns = lbs.getNodeSet(
				Cts2TestConstants.CTS2_AUTOMOBILES_URI, Constructors
						.createCodingSchemeVersionOrTagFromVersion(Cts2TestConstants.CTS2_AUTOMOBILES_VERSION), null);

		exporter.setCng(cng);
		exporter.setCns(cns);

		exporter.getOptions().getBooleanOption("Async Load").setOptionValue(false);
		exporter.getOptions().getBooleanOption(Option.getNameForType(Option.FAIL_ON_ERROR)).setOptionValue(true);
		exporter.getOptions().getBooleanOption("force").setOptionValue(true);
		
		
		operation.exportCodeSystemContent(Cts2TestConstants.CTS2_AUTOMOBILES_NAME, Cts2TestConstants.CTS2_AUTOMOBILES_VERSION, 
				new File(System.getProperty("java.io.tmpdir")).toURI(), exporter);
		
		CodingScheme cs = CodingScheme.unmarshalCodingScheme(new FileReader(exportFile));
		
		assertTrue(cs.getCodingSchemeName().equals(cs.getCodingSchemeName()));
		assertTrue(cs.getCodingSchemeURI().equals(cs.getCodingSchemeURI()));
		assertTrue(cs.getDefaultLanguage().equals("en"));
		assertTrue(cs.getApproxNumConcepts() >= 5);
		assertTrue(cs.getEntities().getEntityCount() >= 15);
		assertTrue(cs.getRelationsCount() >= 1);
	}

	/**
	 * Test method for {@link org.lexevs.cts2.admin.export.CodeSystemExportOperationImpl#exportCodedNodeGraph(java.lang.String, java.lang.String, org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph, java.net.URI, boolean, boolean, boolean)}.
	 * @throws LBException 
	 * @throws URISyntaxException 
	 * @throws FileNotFoundException 
	 * @throws ValidationException 
	 * @throws MarshalException 
	 */
	@Test
	public void testExportCodedNodeGraph() throws LBException, URISyntaxException, MarshalException, ValidationException, FileNotFoundException {
		// populate coded node graph
		CodedNodeGraph cng = operation.getCodeSystemCodedNodeGraph(Cts2TestConstants.CTS2_AUTOMOBILES_URI, Cts2TestConstants.CTS2_AUTOMOBILES_VERSION);
		cng.restrictToAssociations(Constructors.createNameAndValueList("uses"), null);
		
		operation.exportCodedNodeGraph(Cts2TestConstants.CTS2_AUTOMOBILES_URI, Cts2TestConstants.CTS2_AUTOMOBILES_VERSION, cng, 
				new File(System.getProperty("java.io.tmpdir")).toURI(), true, true, true);
		
		CodingScheme cs = CodingScheme.unmarshalCodingScheme(new FileReader(exportFile));
		
		assertTrue(cs.getCodingSchemeName().equals(cs.getCodingSchemeName()));
		assertTrue(cs.getCodingSchemeURI().equals(cs.getCodingSchemeURI()));
		assertTrue(cs.getDefaultLanguage().equals("en"));
		assertTrue(cs.getRelationsCount() == 1);
		
		for (AssociationPredicate assn : cs.getRelations(0).getAssociationPredicateAsReference())
		{
			if (assn.getAssociationName().equals("uses"))
			{
				assertTrue(assn.getSourceCount() >= 4);
			}
		}
	}

	/**
	 * Test method for {@link org.lexevs.cts2.admin.export.CodeSystemExportOperationImpl#exportCodedNodeSet(java.lang.String, java.lang.String, org.LexGrid.LexBIG.LexBIGService.CodedNodeSet, java.net.URI, boolean, boolean, boolean)}.
	 * @throws FileNotFoundException 
	 * @throws ValidationException 
	 * @throws MarshalException 
	 */
	@Test
	public void testExportCodedNodeSet() throws LBException, URISyntaxException, MarshalException, ValidationException, FileNotFoundException {
		// populate coded node set
		CodedNodeSet cns = operation.getCodeSystemCodedNodeSet(Cts2TestConstants.CTS2_AUTOMOBILES_URI, Cts2TestConstants.CTS2_AUTOMOBILES_VERSION);
		cns.restrictToMatchingDesignations("Ford", null, MatchAlgorithms.LuceneQuery.name(), null);
		
		URI uri = operation.exportCodedNodeSet(Cts2TestConstants.CTS2_AUTOMOBILES_URI, Cts2TestConstants.CTS2_AUTOMOBILES_VERSION, cns, new File(System.getProperty("java.io.tmpdir")).toURI(), true, true, true);
		System.out.println("destination : " + uri.toString());
		
		CodingScheme cs = CodingScheme.unmarshalCodingScheme(new FileReader(exportFile));
		
		assertTrue(cs.getCodingSchemeName().equals(cs.getCodingSchemeName()));
		assertTrue(cs.getCodingSchemeURI().equals(cs.getCodingSchemeURI()));
		assertTrue(cs.getDefaultLanguage().equals("en"));
		assertTrue(cs.getEntities().getEntityCount() >= 1);
		assertTrue(cs.getRelationsCount() >= 1);
	}	
}