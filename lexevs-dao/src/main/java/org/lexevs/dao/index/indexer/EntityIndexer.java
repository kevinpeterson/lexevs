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
package org.lexevs.dao.index.indexer;

import org.apache.lucene.document.Document;
import org.lexevs.dao.index.model.IndexableEntity;

import java.util.Arrays;
import java.util.List;

public class EntityIndexer extends AbstractIndexer<IndexableEntity> {

    private static String CS_URI_VERSION_KEY_FIELD = "csUriVersionKey";

    private static String CS_URI_VERSION_CODE_NAMESPACE_KEY_FIELD = "csUriVersionCodeNamespaceKey";

    private static String ENTITY_UID_FIELD = "entityUid";

    private static String ENTITY_CODE_FIELD = "entityCode";
    private static String ENTITY_NAMESPACE_FIELD = "entityCode";

    @Override
    public List<Document> index(IndexableEntity indexable) {
        LuceneDocumentBuilder builder = new LuceneDocumentBuilder();

        Document doc = builder.

                withUntokenizedField(
                        CS_URI_VERSION_CODE_NAMESPACE_KEY_FIELD, false,
                        this.toKey(
                                indexable.getCodingSchemeUri(),
                                indexable.getCodingSchemeVersion(),
                                indexable.getEntity().getEntityCode(),
                                indexable.getEntity().getEntityCodeNamespace())).

                withUntokenizedField(
                        CS_URI_VERSION_KEY_FIELD, false,
                        this.toKey(indexable.getCodingSchemeUri(), indexable.getCodingSchemeVersion())).

                withUntokenizedField(
                        ENTITY_CODE_FIELD, true,
                        indexable.getEntity().getEntityCode()).

                withUntokenizedField(
                        ENTITY_NAMESPACE_FIELD, true,
                        indexable.getEntity().getEntityCodeNamespace()).

                withUnindexedField(
                        ENTITY_UID_FIELD,
                        indexable.getEntity().getId()).

                build();

        return Arrays.asList(doc);
    }
}