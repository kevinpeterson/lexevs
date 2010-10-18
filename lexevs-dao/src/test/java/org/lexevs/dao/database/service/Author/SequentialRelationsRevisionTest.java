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
package org.lexevs.dao.database.service.Author;

import java.io.File;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.Relations;
import org.LexGrid.versions.SystemRelease;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.service.event.registry.ExtensionLoadingListenerRegistry;
import org.lexevs.dao.database.service.relation.VersionableEventRelationService;
import org.lexevs.dao.database.service.version.VersionableEventAuthoringService;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;

public class SequentialRelationsRevisionTest extends LexEvsDbUnitTestBase {

	@Resource(name = "authoringService")
	private VersionableEventAuthoringService service;
	
	@Resource
	private VersionableEventRelationService relationsService;
	
	@Resource
	private ExtensionLoadingListenerRegistry extensionLoadingListenerRegistry;
	
	@Before
	public void loadSystemRelease() throws Exception {
		extensionLoadingListenerRegistry.setEnableListeners(true);
		
		URI sourceURI = new File(
		"src/test/resources/csRevision/relationsRevisionTest.xml")
		.toURI();

		org.exolab.castor.xml.Unmarshaller um = new org.exolab.castor.xml.Unmarshaller(
				SystemRelease.class);
		SystemRelease systemRelease = (SystemRelease) um
		.unmarshal(new InputStreamReader(sourceURI.toURL()
				.openConnection().getInputStream()));

		service.loadSystemRelease(systemRelease, true);
	}
	
	@Test
	public void testGetRevision1Relations() throws Exception {

		Relations relations = relationsService.resolveRelationsByRevision(
				"testUri", "1.0", "testRelations",
				"1");

		assertNotNull(relations);
		
		assertEquals("Revision 1 Description", relations.getEntityDescription().getContent());
		
		assertEquals("1", relations.getEntryState().getContainingRevision());
		assertEquals(1, relations.getAssociationPredicateCount());
	}
	
	@Test
	public void testGetRevision2Relations() throws Exception {

		Relations relations = relationsService.resolveRelationsByRevision(
				"testUri", "1.0", "testRelations",
				"2");

		assertNotNull(relations);
		
		assertEquals("Revision 2 Description", relations.getEntityDescription().getContent());
		
		assertEquals("2", relations.getEntryState().getContainingRevision());
		assertEquals(1, relations.getAssociationPredicateCount());
	}
	
	@Test
	public void testGetRevision3Relations() throws Exception {

		Relations relations = relationsService.resolveRelationsByRevision(
				"testUri", "1.0", "testRelations",
				"3");

		assertNotNull(relations);
		
		assertEquals("Revision 2 Description", relations.getEntityDescription().getContent());
		
		assertEquals("3", relations.getEntryState().getContainingRevision());
		assertEquals(1, relations.getAssociationPredicateCount());
	}
	
	@Test
	public void testGetRevision4Relations() throws Exception {

		Relations relations = relationsService.resolveRelationsByRevision(
				"testUri", "1.0", "testRelations",
				"4");

		assertNotNull(relations);
		
		assertEquals("Revision 4 Description", relations.getEntityDescription().getContent());
		
		assertEquals("4", relations.getEntryState().getContainingRevision());
		
		assertEquals(1, relations.getAssociationPredicateCount());
	}
	
	@Test
	public void testGetRevision5Relations() throws Exception {

		Relations relations = relationsService.resolveRelationsByRevision(
				"testUri", "1.0", "testRelations",
				"5");

		assertNotNull(relations);
		
		assertEquals(2, relations.getAssociationPredicateCount());
		
		List<String> predicateNames = new ArrayList<String>();
		for(AssociationPredicate pred : relations.getAssociationPredicate()) {
			predicateNames.add(pred.getAssociationName());
		}
		
		assertTrue(predicateNames.contains("testPredicate"));
		assertTrue(predicateNames.contains("testPredicate2"));
	}
}