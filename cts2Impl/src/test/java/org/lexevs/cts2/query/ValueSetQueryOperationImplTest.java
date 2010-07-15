/**
 * 
 */
package org.lexevs.cts2.query;

import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.junit.Test;
import org.lexevs.cts2.LexEvsCTS2Impl;

/**
 * @author m004181
 *
 */
public class ValueSetQueryOperationImplTest {

	/**
	 * Test method for {@link org.lexevs.cts2.query.ValueSetQueryOperationImpl#ValueSetQueryOperationImpl(org.lexevs.cts2.LexEvsCTS2)}.
	 */
	@Test
	public void testValueSetQueryOperationImpl() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.lexevs.cts2.query.ValueSetQueryOperationImpl#checkConceptValueSetMembership(java.lang.String, java.net.URI, org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCheckConceptValueSetMembership() {
		ValueSetQueryOperation vsQueryop = LexEvsCTS2Impl.defaultInstance().getQueryOperation().getValueSetQueryOperation();
		
		AbsoluteCodingSchemeVersionReference acsvr = new AbsoluteCodingSchemeVersionReference();
		acsvr.setCodingSchemeURN("urn:oid:11.11.0.1");
		acsvr.setCodingSchemeVersion("1.0");
		
		try {
			boolean member = vsQueryop.checkConceptValueSetMembership("Chevy", new URI("Automobiles"), acsvr, "SRITEST:AUTO:GM", "R104", null);
			System.out.println("member ? : " + member);
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link org.lexevs.cts2.query.ValueSetQueryOperationImpl#checkValueSetSubsumption(java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList, java.lang.String)}.
	 */
	@Test
	public void testCheckValueSetSubsumption() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.lexevs.cts2.query.ValueSetQueryOperationImpl#getValueSetDetails(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGetValueSetDetails() {
		ValueSetQueryOperation vsQueryop = LexEvsCTS2Impl.defaultInstance().getQueryOperation().getValueSetQueryOperation();
		try {
			ValueSetDefinition vsd = vsQueryop.getValueSetDetails("SRITEST:AUTO:GMTEST", "R206");
			
			System.out.println(vsd.getValueSetDefinitionURI());
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link org.lexevs.cts2.query.ValueSetQueryOperationImpl#listValueSetContents(java.lang.String, java.lang.String, org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList, java.lang.String, org.LexGrid.LexBIG.DataModel.InterfaceElements.SortOption)}.
	 */
	@Test
	public void testListValueSetContents() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.lexevs.cts2.query.ValueSetQueryOperationImpl#listValueSets(java.lang.String, java.lang.String, java.lang.String, org.LexGrid.LexBIG.DataModel.InterfaceElements.SortOption)}.
	 */
	@Test
	public void testListValueSets() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.lexevs.cts2.query.ValueSetQueryOperationImpl#listAllValueSets(org.LexGrid.LexBIG.DataModel.InterfaceElements.SortOption)}.
	 */
	@Test
	public void testListAllValueSets() {
		fail("Not yet implemented");
	}

}