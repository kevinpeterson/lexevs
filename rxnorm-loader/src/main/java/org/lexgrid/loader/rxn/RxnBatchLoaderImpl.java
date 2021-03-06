/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
package org.lexgrid.loader.rxn;

import java.io.File;
import java.net.URI;
import java.util.Properties;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Extensions.Load.RxNormBatchLoader;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
//import org.LexGrid.LexBIG.Impl.dataAccess.ResourceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.lexevs.dao.database.spring.DynamicPropertyApplicationContext;
import org.lexgrid.loader.AbstractSpringBatchLoader;
import org.lexgrid.loader.data.codingScheme.CodingSchemeIdSetter;
import org.lexgrid.loader.properties.ConnectionPropertiesFactory;
import org.lexgrid.loader.properties.impl.DefaultLexEVSPropertiesFactory;
import org.lexgrid.loader.setup.JobRepositoryManager;
import org.lexgrid.loader.staging.StagingManager;
import org.springframework.context.ApplicationContext;

import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

/**
 * The Class RxnBatchLoaderImpl.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class RxnBatchLoaderImpl extends AbstractSpringBatchLoader  {

/** The connection properties factory. */
private ConnectionPropertiesFactory connectionPropertiesFactory = new DefaultLexEVSPropertiesFactory();
      public static String SAB_OPTION = "SAB";
	/** The UML s_ loade r_ config. */
	private String RXN_LOADER_CONFIG = "rxnLoader.xml";

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.rxn.RxnBatchLoader#loadRxn(java.lang.String, java.lang.String)
	 */
	public void loadRxn(URI rrfDir, String sab) throws Exception {
		Properties connectionProps = connectionPropertiesFactory.getPropertiesForNewLoad();	
		connectionProps.put("sab", sab);
		connectionProps.put("rrfDir", rrfDir.toString());
		connectionProps.put("retry", "false");
		launchJob(connectionProps, RXN_LOADER_CONFIG, "rxnJob");
	}	
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.rxn.RxnBatchLoader#resumeRxn(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void resumeRxn(URI rrfDir, String sab, String uri, String version) throws Exception {
		Properties connectionProps = connectionPropertiesFactory.getPropertiesForExistingLoad(uri, version);
		connectionProps.put("sab", sab);
		connectionProps.put("rrfDir", rrfDir.toString());
		connectionProps.put("retry", "true");
		launchJob(connectionProps, RXN_LOADER_CONFIG, "rxnJob");
	}
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.rxn.RxnBatchLoader#removeLoad(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void removeLoad(URI rrfDir, String sab, String uri, String version) throws Exception {
		Properties connectionProps = connectionPropertiesFactory.getPropertiesForExistingLoad(uri, version);
		connectionProps.put("sab", sab);
		connectionProps.put("rrfDir", rrfDir.toString());
		connectionProps.put("retry", "true");
		
		DynamicPropertyApplicationContext ctx = new DynamicPropertyApplicationContext("rxnLoader.xml", connectionProps);
		
		JobRepositoryManager jobRepositoryManager = (JobRepositoryManager)ctx.getBean("jobRepositoryManager");
		jobRepositoryManager.dropJobRepositoryDatabases();
		
		StagingManager stagingManager = (StagingManager)ctx.getBean("rxnStagingManager");
		stagingManager.dropAllStagingDatabases();		
		
	//	ResourceManager.instance().removeCodeSystem(
	//			Constructors.createAbsoluteCodingSchemeVersionReference(uri, version));	
	}
    
	@Override
	protected URNVersionPair[] doLoad() {
		try {
			Properties connectionProps = connectionPropertiesFactory.getPropertiesForNewLoad();	
			connectionProps.put("sab", "RXNORM");
			connectionProps.put("rrfDir", this.getResourceUri().toString());
			connectionProps.put("retry", "false");
			launchJob(connectionProps, RXN_LOADER_CONFIG, "rxnJob");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return this.getLoadedCodingSchemes();
	}
	
	@Override
	protected ExtensionDescription buildExtensionDescription() {
		ExtensionDescription meta = new ExtensionDescription();
		meta.setExtensionBaseClass(RxNormBatchLoader.class.getName());
		meta.setExtensionClass("org.lexgrid.loader.rxn.RxNormBatchLoaderImpl");
		meta.setDescription(RxNormBatchLoader.DESCRIPTION);
		meta.setName(RxNormBatchLoader.NAME);
		meta.setVersion(RxNormBatchLoader.VERSION);
		return meta;
	}
    public static void main(String[] args) throws Exception { 
    	RxnBatchLoaderImpl ubl = new RxnBatchLoaderImpl();
		 ubl.loadRxn(new File("S:/ontologies/rxnorm/01032011/rrf").toURI(), "RXNORM");
		// mbl.loadMeta("/home/LargeStorage/ontologies/rrf/LNC/LNC226");
	 }

	@Override
	protected URNVersionPair[] getLoadedCodingSchemes(ApplicationContext context) {
		CodingSchemeIdSetter codingSchemeIdSetter = (CodingSchemeIdSetter)context.getBean("rxnCodingSchemeNameSetter");
		
		URNVersionPair scheme = new URNVersionPair(
				codingSchemeIdSetter.getCodingSchemeUri(), 
				codingSchemeIdSetter.getCodingSchemeVersion());
		return new URNVersionPair[]{scheme};
	}

	@Override
	protected OptionHolder declareAllowedOptions(OptionHolder holder) {
		holder.setIsResourceUriFolder(true);
		return holder;
	}
}
