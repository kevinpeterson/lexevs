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
package org.LexGrid.LexBIG.Impl.codednodeset;

import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.CodedNodeSetImpl;
import org.LexGrid.LexBIG.Impl.helpers.CodeHolder;
import org.LexGrid.LexBIG.Impl.helpers.DefaultCodeHolder;

/**
 * Implementation of the CodedNodeSet Interface.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:rokickik@mail.nih.gov">Konrad Rokicki</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class IntersectSingleLuceneIndexCodedNodeSet extends AbstractMultiSingleLuceneIndexCodedNodeSet {
    
    private static final long serialVersionUID = -5959522938971242708L;

    public IntersectSingleLuceneIndexCodedNodeSet(){
        super();
    }

    public IntersectSingleLuceneIndexCodedNodeSet(CodedNodeSetImpl cns1, CodedNodeSetImpl cns2) { 
        super(cns1,cns2);
    }

    @Override
    protected CodeHolder handleToNodeListCodes(CodeHolder toNodeListCodes1, CodeHolder toNodeListCodes2) {
        CodeHolder newCodeHolder = new DefaultCodeHolder();
        newCodeHolder.union(toNodeListCodes1);
        newCodeHolder.intersect(toNodeListCodes2);
        
        return newCodeHolder;
    }

    @Override
    protected void buildCodeHolder() throws LBParameterException, LBInvocationException {
        this.codesToInclude_ = new DefaultCodeHolder();
        this.codesToInclude_.union(this.getCns1().getCodeHolder());
        this.codesToInclude_.intersect(this.getCns2().getCodeHolder());
    } 
}