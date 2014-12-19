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
import org.lexevs.dao.index.lucenesupport.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The Class AbstractBaseIndexDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractBaseIndexDao implements LexEvsIndexFormatVersionAwareDao {

    private EntityIndexRegistry registry = new EntityIndexRegistry();

    public LuceneIndexTemplate getTemplate(String codingSchemeUri, String codingSchemeVersion) {
        return this.getTemplate(Constructors.createAbsoluteCodingSchemeVersionReference(codingSchemeUri, codingSchemeVersion));
    }

    public LuceneIndexTemplate getTemplate(AbsoluteCodingSchemeVersionReference codingScheme) {
        return new BaseLuceneIndexTemplate(this.registry.getAllNamedDirectory(codingScheme));
    }

    public LuceneIndexTemplate getTemplate(Collection<AbsoluteCodingSchemeVersionReference> codingSchemes) {
        List<LuceneDirectoryFactory.NamedDirectory> directories = new ArrayList<LuceneDirectoryFactory.NamedDirectory>();

        for(AbsoluteCodingSchemeVersionReference reference : codingSchemes) {
            directories.add(this.registry.getAllNamedDirectory(reference));
        }

        return new MultiBaseLuceneIndexTemplate(directories);
    }

    public LuceneIndexTemplate getTemplate() {
        return null;
    }
}