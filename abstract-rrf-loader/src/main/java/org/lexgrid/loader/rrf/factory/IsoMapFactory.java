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
package org.lexgrid.loader.rrf.factory;

import org.lexevs.locator.LexEvsServiceLocator;
import org.lexgrid.loader.logging.LoggingBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

/**
 * A factory for creating IsoMap objects.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@SuppressWarnings("deprecation")
public class IsoMapFactory extends LoggingBean implements FactoryBean {
	
	/** The system variables. */
	private LexEvsServiceLocator lexEvsServiceLocator;
	
	/** The IS o_ ma p_ fil e_ name. */
	public static String ISO_MAP_FILE_NAME = "UMLS_SAB_ISO_Map.txt";
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object getObject() throws Exception {
		Map<String,String> isoMap = getIsoMap();
		
		String path = this.getPath();
		
		if(path == null){
			this.getLogger().warn("Cound not determine path to user defined UMLS SAB->ISO mappings, using defaults.");
			return isoMap;
		}
		
		Resource resource = new FileSystemResource(
				
				this.getPath()
				
					);
		
		if(!resource.exists()){
			this.getLogger().warn("No user defined UMLS SAB->ISO mappings, using defaults.");
			return isoMap;
		} else {
			this.getLogger().warn("User defined UMLS SAB->ISO mappings found at: " + resource.getFile().getPath());
			
			Properties props = new Properties();
			props.load(resource.getInputStream());
			
			for(Object key : props.keySet()) {
				String sab = (String)key;
				String iso = (String)props.get(key);
				Object oldValue = 
					isoMap.put(sab, iso);
				
				if(oldValue != null) {
					this.getLogger().warn("Old value SAB: " + sab + " ISO: " + oldValue +
						" was replaced with SAB: " + sab + " ISO: " + iso + ".");
				} else {
					this.getLogger().warn("User defined entry SAB: " + sab + " ISO: " + iso +
						" was added.");
				}
			}
			
			return isoMap;
		}
	}
	
	protected String getPath() {
		String configPath = 
			lexEvsServiceLocator.getSystemResourceService().
				getSystemVariables().getConfigFileLocation();
		
		if(configPath == null){
			return null;
		}
		
		return configPath.substring(0, configPath.lastIndexOf(File.separator) + 1) + ISO_MAP_FILE_NAME;
	}

    public Hashtable getIsoMap() throws Exception {
        String fileName = "/" + ISO_MAP_FILE_NAME;
        Hashtable result = new Hashtable();
        BufferedReader in;
        int lineNo = 1;
        in = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(fileName)));
        String line = in.readLine();
        while (line != null) {
            if (!line.startsWith("#") && line.length() > 0) {
                String[] foo = line.split("=");
                if (foo.length == 2) {
                    result.put(foo[0], foo[1]);
                } else {
                    throw new Exception("Invalid format on line " + lineNo);
                }
            }
            line = in.readLine();
            lineNo++;
        }
        in.close();
        return result;
    }

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	@Override
	public Class<?> getObjectType() {
		return Map.class;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	@Override
	public boolean isSingleton() {
		return true;
	}

	public LexEvsServiceLocator getLexEvsServiceLocator() {
		return lexEvsServiceLocator;
	}

	public void setLexEvsServiceLocator(LexEvsServiceLocator lexEvsServiceLocator) {
		this.lexEvsServiceLocator = lexEvsServiceLocator;
	}
}
