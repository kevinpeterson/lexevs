/*
 * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
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
package org.cts2.internal.model.uri.restrict;

import java.util.List;

import org.cts2.core.MatchAlgorithmReference;
import org.cts2.core.ModelAttributeReference;
import org.cts2.core.PredicateReference;

/**
 * The Interface RestrictionHandler.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface RestrictionHandler {

	/**
	 * Gets the supported predicate references.
	 *
	 * @return the supported predicate references
	 */
	public List<? extends PredicateReference> getSupportedPredicateReferences();
	
	/**
	 * Gets the supported model attributes.
	 *
	 * @return the supported model attributes
	 */
	public List<? extends ModelAttributeReference> getSupportedModelAttributeReferences();
	
	/**
	 * Gets the supported match algorithm references.
	 *
	 * @return the supported match algorithm references
	 */
	public List<? extends MatchAlgorithmReference> getSupportedMatchAlgorithmReferences();
}
