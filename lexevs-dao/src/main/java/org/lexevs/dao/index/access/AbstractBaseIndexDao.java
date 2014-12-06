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
package org.lexevs.dao.index.access;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.lexevs.dao.index.lucenesupport.LuceneIndexTemplate;
import org.lexevs.dao.index.version.LexEvsIndexFormatVersion;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * The Class AbstractBaseIndexDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractBaseIndexDao implements LexEvsIndexFormatVersionAwareDao {

	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.access.LexEvsIndexFormatVersionAwareDao#supportsLexEvsIndexFormatVersion(org.lexevs.dao.index.version.LexEvsIndexFormatVersion)
	 */
	public boolean supportsLexEvsIndexFormatVersion(LexEvsIndexFormatVersion version) {
		for(LexEvsIndexFormatVersion supportedVersion : doGetSupportedLexEvsIndexFormatVersions()){
			if(version.isEqualVersion(supportedVersion)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Do get supported lex evs index format versions.
	 * 
	 * @return the list< lex evs index format version>
	 */
	public abstract List<LexEvsIndexFormatVersion> doGetSupportedLexEvsIndexFormatVersions();

    public LuceneIndexTemplate getTemplate(String codingSchemeUri, String codingSchemeVersion) {
        return this.getTemplate(Constructors.createAbsoluteCodingSchemeVersionReference(codingSchemeUri, codingSchemeVersion));
    }

    public LuceneIndexTemplate getTemplate(AbsoluteCodingSchemeVersionReference codingScheme) {
        return this.getTemplate(Arrays.asList(codingScheme));
    }

    public LuceneIndexTemplate getTemplate(Collection<AbsoluteCodingSchemeVersionReference> codingSchemes) {
        return null;
    }
}