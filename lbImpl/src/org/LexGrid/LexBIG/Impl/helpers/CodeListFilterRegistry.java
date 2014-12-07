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
package org.LexGrid.LexBIG.Impl.helpers;

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.commons.collections.map.LRUMap;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.TermsFilter;
import org.apache.lucene.search.CachingWrapperFilter;
import org.apache.lucene.search.Filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The Class CodeListFilterRegistry.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CodeListFilterRegistry {

    /** The code list filter registry. */
    private static CodeListFilterRegistry codeListFilterRegistry;
    
    /** The filter map. */
    private Map<Integer,Filter> filterMap = new LRUMap(2);
    
    /**
     * Instantiates a new code list filter registry.
     */
    private CodeListFilterRegistry(){
        super();
    }
    
    /**
     * Default instance.
     * 
     * @return the code list filter registry
     */
    public static CodeListFilterRegistry defaultInstance() {
        if(codeListFilterRegistry == null){
            codeListFilterRegistry = new CodeListFilterRegistry();
        }
        return codeListFilterRegistry;
    }
    
    /**
     * Gets the concept reference list filter.
     * 
     * @param uri the uri
     * @param version the version
     * @param list the list
     * 
     * @return the concept reference list filter
     */
    public Filter getConceptReferenceListFilter(String uri, String version, ConceptReferenceList list) {
        try {
            int key = this.getKey(uri, version, list);
            if(!filterMap.containsKey(key)) {
                String codeField = SQLTableConstants.TBLCOL_ENTITYCODE;

                List<Term> terms = new ArrayList<Term>();

                for(ConceptReference ref : list.getConceptReference()){
                    terms.add(new Term(codeField, ref.getCode()));
                }

                TermsFilter filter = new TermsFilter(terms);

                this.filterMap.put(key, new CachingWrapperFilter(filter));
            }
            
            return this.filterMap.get(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } 
    }
    
    /**
     * Gets the key.
     * 
     * @param uri the uri
     * @param version the version
     * @param list the list
     * 
     * @return the key
     */
    private Integer getKey(String uri, String version, ConceptReferenceList list) {
        int key = uri.hashCode() + version.hashCode();
        
        for(ConceptReference ref : list.getConceptReference()) {
            key += ref.getCode().hashCode();
            key += ref.getCodeNamespace().hashCode();
        }
        
        return key;
    } 
}