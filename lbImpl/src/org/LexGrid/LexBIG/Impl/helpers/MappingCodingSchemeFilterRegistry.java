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
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ServiceUtility;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.relations.Relations;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.queries.TermsFilter;
import org.apache.lucene.search.CachingWrapperFilter;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.Filter;
import org.apache.lucene.util.Bits;
import org.lexevs.dao.database.access.association.model.Triple;
import org.lexevs.dao.database.operation.transitivity.DefaultTransitivityBuilder.TripleIterator;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class CodeListFilterRegistry.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MappingCodingSchemeFilterRegistry {

    /** The code list filter registry. */
    private static MappingCodingSchemeFilterRegistry mappingCodeSystemFilterRegistry;
    
    /** The filter map. */
    private Map<Integer,Filter> filterMap = new HashMap<Integer,Filter>();
    
    /**
     * Instantiates a new code list filter registry.
     */
    private MappingCodingSchemeFilterRegistry(){
        super();
    }
    
    /**
     * Default instance.
     * 
     * @return the code list filter registry
     */
    public static MappingCodingSchemeFilterRegistry defaultInstance() {
        if(mappingCodeSystemFilterRegistry == null){
            mappingCodeSystemFilterRegistry = new MappingCodingSchemeFilterRegistry();
        }
        return mappingCodeSystemFilterRegistry;
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
    public Filter getMappingCodingSchemeFilter(
            String uri, 
            String version,
            boolean proxy) {
        try {
            
            boolean isMapping = this.verifyIsMapping(uri, version);
            if(! isMapping) {
                return null;
            }
            
            int key = this.getKey(uri, version);
            if(!filterMap.containsKey(key)) {
                filterMap.put(key, this.buildFilter(uri, version));
            }
            
            if(proxy) {
                return new ProxyFilter(uri,version);
            } else {
                return filterMap.get(key);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } 
    }
    
    protected boolean verifyIsMapping(String uri, String version) throws LBParameterException, LBInvocationException {
        CodingScheme cs = 
            LexBIGServiceImpl.defaultInstance().resolveCodingScheme(uri, Constructors.createCodingSchemeVersionOrTagFromVersion(version));
        
        if(cs == null || cs.getRelations() == null || cs.getRelations().length == 0) {
            return false;
        }
        
        for(Relations relations : cs.getRelations()) {
            if(relations.isIsMapping() == null || relations.isIsMapping() == false) {
                return false;
            }
            
            String sourceLocalCodingSchemeName = relations.getSourceCodingScheme();
            String sourceLocalCodingSchemeVersion = relations.getSourceCodingSchemeVersion();
            
            String targetLocalCodingSchemeName = relations.getTargetCodingScheme();
            String targetLocalCodingSchemeVersion = relations.getTargetCodingSchemeVersion();
            
            AbsoluteCodingSchemeVersionReference sourceRef =
                ServiceUtility.resolveCodingSchemeFromLocalName(uri, version, sourceLocalCodingSchemeName, sourceLocalCodingSchemeVersion);
            AbsoluteCodingSchemeVersionReference targetRef = 
                ServiceUtility.resolveCodingSchemeFromLocalName(uri, version, targetLocalCodingSchemeName, targetLocalCodingSchemeVersion);
            
            if(sourceRef == null || targetRef == null) {
                return false;
            }
        }
        
        return true;
    }
    
    protected Filter buildFilter(
            String uri, 
            String version) throws LBParameterException {
        LoggerFactory.getLogger().info("Building mapping filter for URI: " + uri + "Version: " + version);

        String codeField = SQLTableConstants.TBLCOL_ENTITYCODE;

        ConceptReferenceList codeList = this.buildConceptReferenceList(uri, version);

        List<Term> terms = new ArrayList<Term>();

        for(ConceptReference ref : codeList.getConceptReference()){
            terms.add(new Term(codeField, ref.getCode()));
        }

        TermsFilter filter = new TermsFilter(terms);

        LoggerFactory.getLogger().info("Finished building mapping filter for URI: " + uri + "Version: " + version);
        
        return decorateFilter(filter);
    }
    
    protected Filter decorateFilter(Filter filter) {
        return new CachingWrapperFilter(filter);
    }
    
    protected ConceptReferenceList buildConceptReferenceList(String uri, String version) {
        ConceptReferenceList returnList = new ConceptReferenceList();
        
        DatabaseServiceManager databaseServiceManager = 
            LexEvsServiceLocator.getInstance().getDatabaseServiceManager();
        
        TripleIterator itr = new TripleIterator(databaseServiceManager, uri, version, null, 25000);
        
        while(itr.hasNext()) {
            Triple triple = itr.next();
            
            ConceptReferenceList list = this.tripleToConceptReferenceList(triple);
            
            for(ConceptReference ref : list.getConceptReference()) {
                returnList.addConceptReference(ref);
            }
        }
        
        return returnList;
    }
    
    public static class ProxyFilter extends Filter {

        private static final long serialVersionUID = -6871822509780400995L;

        private String uri;
        private String version;
        
        public ProxyFilter() {
            super();
        }
        
        public ProxyFilter(String uri, String version) {
            this.uri = uri;
            this.version = version;
        }

        @Override
        public DocIdSet getDocIdSet(AtomicReaderContext atomicReaderContext, Bits bits) throws IOException {
            return MappingCodingSchemeFilterRegistry.defaultInstance().
                            getMappingCodingSchemeFilter(uri, version, false).getDocIdSet(atomicReaderContext, bits);
        }
    }
    
    private ConceptReferenceList tripleToConceptReferenceList(Triple triple) {
        ConceptReferenceList returnList = new ConceptReferenceList();
        
        String sourceCode = triple.getSourceEntityCode();
        String sourceNamespace = triple.getSourceEntityNamespace();
        String targetCode = triple.getTargetEntityCode();
        String targetNamespace = triple.getTargetEntityNamespace();
        
        ConceptReference sourceCr = new ConceptReference();
        sourceCr.setCode(sourceCode);
        sourceCr.setCodeNamespace(sourceNamespace);
        
        ConceptReference targetCr = new ConceptReference();
        targetCr.setCode(targetCode);
        targetCr.setCodeNamespace(targetNamespace);
        
        returnList.addConceptReference(sourceCr);
        returnList.addConceptReference(targetCr);
        
        return returnList;
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
    private Integer getKey(String uri, String version) {
        int key = uri.hashCode() + version.hashCode();
        
        return key;
    }
}
