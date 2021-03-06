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
package edu.mayo.informatics.indexer.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;

import edu.mayo.informatics.indexer.api.exceptions.InternalIndexerErrorException;

/**
 * Defines the methods that an IndexWriter must implement.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 */
public interface LuceneIndexWriterInterface {

    public void setMaxFieldLength(int i);

    public void setMaxMergeDocs(int i);

    public void setMergeFactor(int i);

    public void setMaxBufferedDocs(int i);

    public void setUseCompoundFile(boolean bool);

    public void addDocument(Document document) throws InternalIndexerErrorException;

    public void addDocument(Document document, Analyzer analyzer) throws InternalIndexerErrorException;

    public void optimize() throws InternalIndexerErrorException;

    public void close() throws InternalIndexerErrorException;

    public void setDocsPerTempIndex(int i);

}