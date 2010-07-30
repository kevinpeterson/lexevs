package edu.mayo.informatics.lexgrid.convert.directConversions.mrmap;

import java.util.List;

import org.LexGrid.commonTypes.Text;
import org.LexGrid.relations.AssociationQualification;

import edu.mayo.informatics.lexgrid.convert.directConversions.mrmap.MRMAP2LexGrid;
import edu.mayo.informatics.lexgrid.convert.directConversions.mrmap.MrMap;
import junit.framework.TestCase;

public class GetAssociationQualifiersTest extends TestCase {

	public void testGetAssociationQualifiers() throws Exception
{

		MrMap map = new MrMap();
		map.setAtn("asdfa");
		map.setAtv("dghd");
		map.setFromexpr("jgkg");
		map.setFromid("jkl;;j");
		map.setFromres("ruyr");
		map.setFromrule("qer");
		map.setFromsid("afa");
		map.setFromtype("fjhf");
		map.setMapid("ruyty");
		map.setMaprank("dghd");
		map.setMapres("xbv");
		map.setMaprule("vmnv");
		map.setMapsab("wrt");
		map.setMapsetcui("afa");
		map.setMapsid("q15r");
		map.setMaprank("u5i");
		map.setMapres("hjf");
		map.setMapsubsetid("qer");
		map.setMaptype("adf");
		map.setToexpr("afda");
		map.setToid("adf");
		map.setTores("rqer");
		map.setTorule("adf");
		map.setTosid("afa");
		map.setTotype("afd");
		map.setRel("df");
		map.setRela("ew");
		
		MRMAP2LexGrid mapping = new MRMAP2LexGrid(false, null, null, null);
		List<AssociationQualification> qualifiers = mapping.getAssociationQualifiers(map);
		assertTrue(qualifiers.size() == 23);
		
        for(AssociationQualification q : qualifiers){
        	

            assertTrue(q.getAssociationQualifier() != null);
            assertTrue(q.getQualifierText().getContent() != null);

   
        } 
		
        
		MrMap map1 = new MrMap();
		map1.setAtn(null);
		map1.setAtv(null);
		map1.setFromexpr(null);
		map1.setFromid("jkl;;j");
		map1.setFromres("ruyr");
		map1.setFromrule("qer");
		map1.setFromsid("afa");
		map1.setFromtype("fjhf");
		map1.setMapid("ruyty");
		map1.setMaprank("dghd");
		map1.setMapres("xbv");
		map1.setMaprule("vmnv");
		map1.setMapsab("wrt");
		map1.setMapsetcui("afa");
		map1.setMapsid("q15r");
		map1.setMaprank("u5i");
		map1.setMapres("hjf");
		map1.setMapsubsetid("qer");
		map1.setMaptype("adf");
		map1.setToexpr("afda");
		map1.setToid("adf");
		map1.setTores("rqer");
		map1.setTorule("adf");
		map1.setTosid("afa");
		map1.setTotype("afd");
		map1.setRel("df");
		map1.setRela("ew");
		
		MRMAP2LexGrid mapping1 = new MRMAP2LexGrid(false, null, null, null);
		List<AssociationQualification> qualifiers1 = mapping1.getAssociationQualifiers(map1);
		assertTrue(qualifiers1.size() == 20);
        for(AssociationQualification q : qualifiers1){
            assertTrue(q.getAssociationQualifier() != null);
            assertTrue(q.getQualifierText().getContent() != null);
        } 

	
		}
}