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

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.CodedNodeSetImpl;
import org.LexGrid.LexBIG.Impl.helpers.CodeHolder;
import org.LexGrid.LexBIG.Impl.helpers.DefaultCodeHolder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.FilteredQuery;
import org.apache.lucene.search.Query;
import org.compass.core.lucene.support.ChainedFilter;

/**
 * Implementation of the CodedNodeSet Interface.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:rokickik@mail.nih.gov">Konrad Rokicki</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class UnionSingleLuceneIndexCodedNodeSet extends AbstractMultiSingleLuceneIndexCodedNodeSet {

    private static final long serialVersionUID = -5959522938971242708L;

    public UnionSingleLuceneIndexCodedNodeSet() {
        super();
    }

    public UnionSingleLuceneIndexCodedNodeSet(CodedNodeSetImpl cns1, CodedNodeSetImpl cns2) {
        super(cns1, cns2);
    }

    private Query combineQueries(Query query1, Query query2) {
        BooleanQuery query = new BooleanQuery();
        query.add(query1, Occur.SHOULD);
        query.add(query2, Occur.SHOULD);

        return query;
    }

    @Override
    protected CodeHolder handleToNodeListCodes(CodeHolder toNodeListCodes1, CodeHolder toNodeListCodes2) {
        CodeHolder newCodeHolder = new DefaultCodeHolder();
        newCodeHolder.union(toNodeListCodes1);
        newCodeHolder.union(toNodeListCodes2);

        return newCodeHolder;
    }

    @Override
    protected CodeHolder handleOneNullToNodeListCodes(CodeHolder toNodeListCodes1, CodeHolder toNodeListCodes2) {
        CodeHolder codeHolder = new DefaultCodeHolder();
        if (toNodeListCodes1 != null) {
            codeHolder.union(toNodeListCodes1);
        }
        if (toNodeListCodes2 != null) {
            codeHolder.union(toNodeListCodes2);
        }

        return codeHolder;
    }

    @Override
    public void runPendingOps() throws LBInvocationException, LBParameterException {
        super.runPendingOps();

        this.getQueries().add(
                combineQueries(combineQueriesAndFilters(this.getCns1()), combineQueriesAndFilters(this.getCns2())));
    }

    protected void buildCodeHolder() throws LBInvocationException, LBParameterException {
        codesToInclude_ = codeHolderFactory.buildCodeHolder(
                new ArrayList<AbsoluteCodingSchemeVersionReference>(this.getCodingSchemeReferences()),
                combineQueriesAndFilters(this));
    }

    protected Query combineQueriesAndFilters(CodedNodeSetImpl cns) {
        List<Filter> filters = cns.getFilters();
        List<Query> queries = cns.getQueries();

        Filter chainedFilter = new ChainedFilter(filters.toArray(new Filter[filters.size()]), ChainedFilter.AND);

        BooleanQuery combinedQuery = new BooleanQuery();
        for (Query query : queries) {
            combinedQuery.add(query, Occur.MUST);
        }

        Query query;
        if (CollectionUtils.isNotEmpty(filters)) {
            query = new FilteredQuery(combinedQuery, chainedFilter);
        } else {
            query = combinedQuery;
        }

        return query;
    }
}