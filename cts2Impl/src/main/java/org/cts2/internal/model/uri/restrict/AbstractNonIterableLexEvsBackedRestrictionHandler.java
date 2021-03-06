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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.cts2.core.Filter;
import org.cts2.core.FilterComponent;
import org.cts2.core.NameOrURI;
import org.cts2.core.PredicateReference;
import org.cts2.core.types.SetOperator;
import org.cts2.internal.match.OperationExecutingModelAttributeReference;
import org.cts2.uri.DirectoryURI;
import org.cts2.uri.restriction.RestrictionState;

/**
 * The Class AbstractNonIterableLexEvsBackedRestrictionHandler.
 *
 * @param <T> the
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractNonIterableLexEvsBackedRestrictionHandler<T,D extends DirectoryURI> 
	extends AbstractRestrictionHandler implements NonIterableBasedResolvingRestrictionHandler<T,D> {

	/** The operation executing model attribute reference. */
	private List<OperationExecutingModelAttributeReference<T>> operationExecutingModelAttributeReferences;
	
	public AbstractNonIterableLexEvsBackedRestrictionHandler() {
		super();
		this.operationExecutingModelAttributeReferences = this.registerSupportedModelAttributeReferences();
	}

	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.restrict.NonIterableBasedResolvingRestrictionHandler#restrict(java.lang.Object, org.cts2.core.Filter)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Restriction<T> compile(D directoryURI, OriginalStateProvider<T> originalStateProvider) {
		
		RestrictionState<? extends DirectoryURI> restriction = directoryURI.getRestrictionState();

		final List<Restriction<T>> returnList = new ArrayList<Restriction<T>>();

		for(final Filter filter : restriction.getFilters()){

			returnList.add(new Restriction<T>(){

				@Override
				public T processRestriction(T state) {
					List<FilterComponent> filterComponents = Arrays.asList(filter.getComponent());

					for (FilterComponent filterComponent : filterComponents) {
						OperationExecutingModelAttributeReference<T> operation = 
							getOperationExecutingModelAttributeReference(filterComponent.getFilterComponent().getReferenceTarget());

						state = operation.executeOperation(
								state, 
								filterComponent.getMatchValue(),
								filterComponent.getMatchAlgorithm());
					}	

					return state;
				}
			});
		}
		
		List<Restriction<T>> otherRestrictions = this.processOtherRestictions(directoryURI);
		if(CollectionUtils.isNotEmpty(otherRestrictions)){
			returnList.addAll(otherRestrictions);
		}
		
		if(restriction.getSetComposite() != null){
			returnList.add(this.processSetOperation(
					(D)restriction.getSetComposite().getDirectoryUri1(), 
					(D)restriction.getSetComposite().getDirectoryUri2(),
					restriction.getSetComposite().getSetOperator(),
					originalStateProvider));
		}
		
		return RestrictionUtils.combineRestrictions(returnList);
	}
	
	public T apply(Restriction<T> restriction, T state){
		return restriction.processRestriction(state);
	}
	
	protected abstract List<Restriction<T>> processOtherRestictions(D directoryURI);

	public Restriction<T> processSetOperation(final D directoryUri1, final D directoryUri2, final SetOperator setOperator, final OriginalStateProvider<T> originalStateProvider) {
		return new Restriction<T>(){

			@Override
			public T processRestriction(T state) {
		
				switch (setOperator){
					case UNION : {
						return doUnion(directoryUri1, directoryUri2, originalStateProvider);
					}
					case INTERSECT : {
						return doIntersect(directoryUri1, directoryUri2, originalStateProvider);
					}
					case SUBTRACT : {
						return doDifference(directoryUri1, directoryUri2, originalStateProvider);
					}
				}
				
				throw new IllegalStateException();
			}
		};
	}
	
	protected abstract T doUnion(final D directoryUri1, final D directoryUri2, OriginalStateProvider<T> originalStateProvider);
	
	protected abstract T doIntersect(final D directoryUri1, final D directoryUri2, OriginalStateProvider<T> originalStateProvider);
	
	protected abstract T doDifference(final D directoryUri1, final D directoryUri2, OriginalStateProvider<T> originalStateProvider);

	/**
	 * Register supported model attribute references.
	 *
	 * @return the list
	 */
	public abstract List<OperationExecutingModelAttributeReference<T>> registerSupportedModelAttributeReferences();
	
	/**
	 * Gets the operation executing model attribute reference.
	 *
	 * @param nameOrUri the name or uri
	 * @return the operation executing model attribute reference
	 */
	private OperationExecutingModelAttributeReference<T> getOperationExecutingModelAttributeReference(NameOrURI nameOrUri){
		for(OperationExecutingModelAttributeReference<T> modelAttribute : this.operationExecutingModelAttributeReferences){
			if(modelAttribute.getContent().equals(nameOrUri.getName())){
				return modelAttribute;
			}
			
			if(modelAttribute.getMeaning().equals(nameOrUri.getUri())){
				return modelAttribute;
			}
		}
		
		//TODO: validate this instead of returning null
		return null;
	}
	
	
	
	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.restrict.RestrictionHandler#getSupportedPredicateReferences()
	 */
	@Override
	public List<PredicateReference> getSupportedPredicateReferences() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.restrict.ResolvingRestrictionHandler#getSupportedModelAttributes()
	 */
	@Override
	public List<OperationExecutingModelAttributeReference<T>> getSupportedModelAttributeReferences() {
		return this.operationExecutingModelAttributeReferences;
	}
}
