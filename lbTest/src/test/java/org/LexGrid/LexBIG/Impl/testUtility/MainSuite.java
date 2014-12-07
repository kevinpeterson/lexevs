package org.LexGrid.LexBIG.Impl.testUtility;

import edu.mayo.informatics.lexgrid.convert.indexer.LuceneLoaderCodeTest;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.LexGrid.LexBIG.Impl.CodedNodeGraphImplTest;
import org.LexGrid.LexBIG.Impl.CodedNodeSetImplTest;
import org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.LexBIGServiceConvenienceMethodsImplTest;
import org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.MappingExtensionImplTest;
import org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.SearchExtensionImplTest;
import org.LexGrid.LexBIG.Impl.Extensions.Search.AbstractSearchTest;
import org.LexGrid.LexBIG.Impl.Extensions.Sort.AbstractSortTest;
import org.LexGrid.LexBIG.Impl.History.NCIThesaurusHistoryServiceTest;
import org.LexGrid.LexBIG.Impl.History.UMLSHistoryServiceTest;
import org.LexGrid.LexBIG.Impl.ServiceManagerTest;
import org.LexGrid.LexBIG.Impl.bugs.*;
import org.LexGrid.LexBIG.Impl.dataAccess.*;
import org.LexGrid.LexBIG.Impl.featureRequests.AddNamespaceToIndex;
import org.LexGrid.LexBIG.Impl.featureRequests.ChangeConfigFileName;
import org.LexGrid.LexBIG.Impl.featureRequests.GForge17019;
import org.LexGrid.LexBIG.Impl.function.codednodeset.*;
import org.LexGrid.LexBIG.Impl.function.history.TestProductionTags;
import org.LexGrid.LexBIG.Impl.function.mapping.MappingToNodeListTest;
import org.LexGrid.LexBIG.Impl.function.metadata.TestMetaDataSearch;
import org.LexGrid.LexBIG.Impl.function.metadata.TestNCIThesMetadata;
import org.LexGrid.LexBIG.Impl.function.query.*;
import org.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms.*;
import org.LexGrid.LexBIG.Impl.helpers.AbstractListBackedResolvedConceptReferencesIteratorTest;
import org.LexGrid.LexBIG.Impl.helpers.CodeToReturnTest;
import org.LexGrid.LexBIG.Impl.helpers.ConfigureTest;
import org.LexGrid.LexBIG.Impl.helpers.ResolvedConceptReferencesIteratorImplTest;
import org.LexGrid.LexBIG.Impl.helpers.comparator.ResultComparatorTest;
import org.LexGrid.LexBIG.Impl.helpers.lazyloading.LazyLoadableCodeToReturnTest;
import org.LexGrid.LexBIG.Impl.load.meta.*;
import org.lexevs.dao.database.service.listener.DuplicatePropertyIdListenerTest;

public class MainSuite {
	  public static Test suite() throws Exception {
	        TestSuite mainSuite = new TestSuite("LexBIG validation tests");
	        ServiceHolder.configureForSingleConfig();

	        mainSuite.addTestSuite(ConfigureTest.class);
//	        mainSuite.addTestSuite(LoadTestDataTest.class);

	        mainSuite.addTestSuite(CodeToReturnTest.class);
	        mainSuite.addTestSuite(NCIThesaurusHistoryServiceTest.class);
	        mainSuite.addTestSuite(UMLSHistoryServiceTest.class);
	        mainSuite.addTestSuite(LexBIGServiceConvenienceMethodsImplTest.class);
	        mainSuite.addTestSuite(CodedNodeGraphImplTest.class);
	        mainSuite.addTestSuite(CodedNodeSetImplTest.class);
	        mainSuite.addTestSuite(TestMetaDataSearch.class);
	        mainSuite.addTestSuite(ServiceManagerTest.class);
	        mainSuite.addTestSuite(RegistryTest.class);
	        mainSuite.addTestSuite(TestNCIThesMetadata.class);
	        mainSuite.addTestSuite(ResourceManagerTest.class); 
	        mainSuite.addTestSuite(SQLImplementedMethodsTest.class);  
	        mainSuite.addTestSuite(ResolvedConceptReferencesIteratorImplTest.class);
	        mainSuite.addTestSuite(AbstractListBackedResolvedConceptReferencesIteratorTest.class);
	        
	        mainSuite.addTestSuite(AbstractSortTest.class);
	        mainSuite.addTestSuite(AbstractSearchTest.class);
	        
	        TestSuite metaLoaderSuite = new TestSuite("MetaLoader Tests");
	        metaLoaderSuite.addTestSuite(DefinitionPropertyDataTestIT.class);
	        metaLoaderSuite.addTestSuite(DefinitionQualifiersDataTestIT.class);
	        metaLoaderSuite.addTestSuite(EntityAssnsToEntityDataTestIT.class);
	        metaLoaderSuite.addTestSuite(EntityAssnsToEntityQualsDataTestIT.class);
	        metaLoaderSuite.addTestSuite(EntityDataTestIT.class);
	        metaLoaderSuite.addTestSuite(MetadataLoadTestIT.class);
	        metaLoaderSuite.addTestSuite(MrstyPropertyDataTestIT.class);
	        metaLoaderSuite.addTestSuite(MrhierAssocQualifierTestIT.class);
	        metaLoaderSuite.addTestSuite(MrhierPropertyQualifierTestIT.class);
	        metaLoaderSuite.addTestSuite(MrrankQualifierDataTestIT.class);
	        metaLoaderSuite.addTestSuite(PresentationPropertyDataTestIT.class);
	        metaLoaderSuite.addTestSuite(PresentationQualifiersDataTestIT.class);
	        metaLoaderSuite.addTestSuite(MetaVersionTestIT.class);
	        metaLoaderSuite.addTestSuite(GenericPropertySourceQualifierTestIT.class);

	        mainSuite.addTest(metaLoaderSuite);
	        
	        TestSuite umlsLoaderSuite = new TestSuite("UmlsLoader Tests");
	        umlsLoaderSuite.addTestSuite(org.LexGrid.LexBIG.Impl.load.umls.EntityAssnsToEntityDataTestIT.class);
	        umlsLoaderSuite.addTestSuite(org.LexGrid.LexBIG.Impl.load.umls.EntityAssnsToEntityQualsDataTestIT.class);
	        umlsLoaderSuite.addTestSuite(org.LexGrid.LexBIG.Impl.load.umls.HierarchyRootsTestIT.class);
	        umlsLoaderSuite.addTestSuite(org.LexGrid.LexBIG.Impl.load.umls.PresentationPropertyDataTestIT.class);
	        umlsLoaderSuite.addTestSuite(org.LexGrid.LexBIG.Impl.load.umls.ReverseAssocDirectionalityTestIT.class);
	        umlsLoaderSuite.addTestSuite(org.LexGrid.LexBIG.Impl.load.umls.SameCodeDifferentCuiTestIT.class); 
	        umlsLoaderSuite.addTestSuite(org.LexGrid.LexBIG.Impl.load.umls.TuiPropertyDataTestIT.class);
	        umlsLoaderSuite.addTestSuite(org.LexGrid.LexBIG.Impl.load.umls.IsoMapTest.class);
	        mainSuite.addTest(umlsLoaderSuite);
	        
	        TestSuite hl7MifVocabularyLoaderSuite = new TestSuite("HL7 MIF Vocabulary Loader Tests");
	        hl7MifVocabularyLoaderSuite.addTestSuite(edu.mayo.informatics.lexgrid.convert.directConversions.hl7.mif.vocabulary.MifVocabularyHierarchyRootsTestIT.class);
	        hl7MifVocabularyLoaderSuite.addTestSuite(edu.mayo.informatics.lexgrid.convert.directConversions.hl7.mif.vocabulary.DefinitionPropertyDataTestIT.class);
	        hl7MifVocabularyLoaderSuite.addTestSuite(edu.mayo.informatics.lexgrid.convert.directConversions.hl7.mif.vocabulary.EntityAssnsToEntityDataTestIT.class);
	        hl7MifVocabularyLoaderSuite.addTestSuite(edu.mayo.informatics.lexgrid.convert.directConversions.hl7.mif.vocabulary.PresentationPropertyDataTestIT.class);
	        mainSuite.addTest(hl7MifVocabularyLoaderSuite);
	        
	        TestSuite luceneSuite = new TestSuite("Lucene Tests");
	        luceneSuite.addTestSuite(LuceneLoaderCodeTest.class);
	        mainSuite.addTest(luceneSuite);    
	        
	        TestSuite luceneSearchSuite = new TestSuite("Lucene Search Tests");
	        luceneSearchSuite.addTestSuite(TestContains.class);
	        luceneSearchSuite.addTestSuite(TestContainsLiteralContains.class);
	        luceneSearchSuite.addTestSuite(TestDoubleMetaphone.class);      
	        luceneSearchSuite.addTestSuite(TestExactMatch.class);
	        luceneSearchSuite.addTestSuite(TestLeadingAndTrailingWildcard.class);
	        luceneSearchSuite.addTestSuite(TestLiteral.class); 
	        luceneSearchSuite.addTestSuite(TestLiteralContains.class); 
	        luceneSearchSuite.addTestSuite(TestLiteralLiteralContains.class); 
	        luceneSearchSuite.addTestSuite(TestLiteralSpellingErrorTolerantSubString.class); 
	        luceneSearchSuite.addTestSuite(TestLiteralSubString.class); 
	        luceneSearchSuite.addTestSuite(TestPhrase.class);
	        luceneSearchSuite.addTestSuite(TestRegExp.class);
	        luceneSearchSuite.addTestSuite(TestSearchByPreferred.class);
	        luceneSearchSuite.addTestSuite(TestSpellingErrorTolerantSubString.class); 
	        luceneSearchSuite.addTestSuite(TestStartsWith.class);
	        luceneSearchSuite.addTestSuite(TestStemming.class);
	        luceneSearchSuite.addTestSuite(TestSubString.class); 
	        luceneSearchSuite.addTestSuite(TestSubStringLiteralSubString.class);
	        luceneSearchSuite.addTestSuite(TestWeightedDoubleMetaphone.class);
	        luceneSearchSuite.addTestSuite(TestSubStringNonLeadingWildcardLiteralSubString.class);
	        mainSuite.addTest(luceneSearchSuite);
	        
	        TestSuite lazyLoadingSuite = new TestSuite("Lazy Loading Tests");
	        lazyLoadingSuite.addTestSuite(LazyLoadableCodeToReturnTest.class);
	        mainSuite.addTest(lazyLoadingSuite);
	        
	        TestSuite comparatorSuite = new TestSuite("Comparator Tests");
	        comparatorSuite.addTestSuite(ResultComparatorTest.class);
	        mainSuite.addTest(comparatorSuite);
	        
	        TestSuite codedNodeSetSuite = new TestSuite("CodedNodeSet Tests");
	        codedNodeSetSuite.addTestSuite(ResolveTest.class);
	        codedNodeSetSuite.addTestSuite(ResolveToListTest.class);
	        codedNodeSetSuite.addTestSuite(DifferenceTest.class);
	        codedNodeSetSuite.addTestSuite(CodedNodeSetOperationsTest.class);
	        codedNodeSetSuite.addTestSuite(DifferenceTest.class);
	        codedNodeSetSuite.addTestSuite(IntersectionTest.class);
	        codedNodeSetSuite.addTestSuite(UnionTest.class);
	        codedNodeSetSuite.addTestSuite(RestrictToMatchingDesignationsTest.class);
	        codedNodeSetSuite.addTestSuite(RestrictToMatchingPropertiesTest.class);
	        codedNodeSetSuite.addTestSuite(RestrictToPropertiesTest.class);
	        codedNodeSetSuite.addTestSuite(MultipeRestrictionsTest.class);
	        codedNodeSetSuite.addTestSuite(ResolveMappingCodedNodeSetTest.class);
	        codedNodeSetSuite.addTestSuite(ExtensionCodedNodeSetTest.class);
	        mainSuite.addTest(codedNodeSetSuite);
	        
	        TestSuite codedNodeGraphSuite = new TestSuite("CodedNodeGraph Tests");
	        codedNodeGraphSuite.addTestSuite(org.LexGrid.LexBIG.Impl.function.codednodegraph.RestrictToAssociationsTest.class);
	        codedNodeGraphSuite.addTestSuite(org.LexGrid.LexBIG.Impl.function.codednodegraph.RestrictToDirectionalNamesTest.class);
	        codedNodeGraphSuite.addTestSuite(org.LexGrid.LexBIG.Impl.function.codednodegraph.RestrictToSourceCodesTest.class);
	        codedNodeGraphSuite.addTestSuite(org.LexGrid.LexBIG.Impl.function.codednodegraph.RestrictToTargetCodesTest.class);
	        codedNodeGraphSuite.addTestSuite(org.LexGrid.LexBIG.Impl.function.codednodegraph.ResolveToListTest.class);
	        codedNodeGraphSuite.addTestSuite(org.LexGrid.LexBIG.Impl.function.codednodegraph.SortGraphTest.class);
	        codedNodeGraphSuite.addTestSuite(org.LexGrid.LexBIG.Impl.function.codednodegraph.ToNodeListTest.class);
	        codedNodeGraphSuite.addTestSuite(org.LexGrid.LexBIG.Impl.function.codednodegraph.IntersectionTest.class);
	        codedNodeGraphSuite.addTestSuite(org.LexGrid.LexBIG.Impl.function.codednodegraph.FilterTest.class);
	        codedNodeGraphSuite.addTestSuite(org.LexGrid.LexBIG.Impl.function.codednodegraph.RootsTest.class);
	        codedNodeGraphSuite.addTestSuite(org.LexGrid.LexBIG.Impl.function.codednodegraph.FocusTest.class);
	        codedNodeGraphSuite.addTestSuite(org.LexGrid.LexBIG.Impl.function.codednodegraph.UnionTest.class);
	        codedNodeGraphSuite.addTestSuite(org.LexGrid.LexBIG.Impl.function.codednodegraph.CrossOntologyResolveTest.class);
	        codedNodeGraphSuite.addTestSuite(org.LexGrid.LexBIG.Impl.function.codednodegraph.RestrictToAnonymousTest.class);
	        codedNodeGraphSuite.addTestSuite(org.LexGrid.LexBIG.Impl.function.codednodegraph.RestrictToEntityTypesTest.class);
	        codedNodeGraphSuite.addTestSuite(org.LexGrid.LexBIG.Impl.function.codednodegraph.CodingSchemeExtensionResolveTest.class);
	        mainSuite.addTest(codedNodeGraphSuite);

	        TestSuite functionalTests = new TestSuite("Functional Tests");
	        functionalTests.addTestSuite(TestProductionTags.class);
	        functionalTests.addTestSuite(TestApproximateStringMatch.class);
	        functionalTests.addTestSuite(TestAttributePresenceMatch.class);
	        functionalTests.addTestSuite(TestAttributeValueMatch.class);
	        functionalTests.addTestSuite(TestContentExtraction.class);
	        functionalTests.addTestSuite(TestDAGWalking.class);
	        functionalTests.addTestSuite(TestDescribeSearchTechniques.class);
	        functionalTests.addTestSuite(TestDescribeSupportedSearchCriteria.class);
	        functionalTests.addTestSuite(TestDiscoverAvailableVocabulariesandVersions.class);
	        functionalTests.addTestSuite(TestEnumerateAllConcepts.class);
	        functionalTests.addTestSuite(TestEnumerateConceptsbyRelationship.class);
	        functionalTests.addTestSuite(TestEnumerateProperties.class);
	        functionalTests.addTestSuite(TestEnumerateRelationsbyRange.class);
	        functionalTests.addTestSuite(TestEnumerateRelationships.class);
	        functionalTests.addTestSuite(TestEnumerateSourceConceptsforRelationandTarget.class);
	        functionalTests.addTestSuite(TestforCurrentOrObsoleteConcept.class);
	        functionalTests.addTestSuite(TestGenerateDAG.class);
	        functionalTests.addTestSuite(TestHierarchyAPI.class);
	        functionalTests.addTestSuite(TestLexicalMatchingTechniques.class);
	        functionalTests.addTestSuite(TestLimitReturnedValues.class);
	        functionalTests.addTestSuite(TestMapAttributestoTypes.class);
	        functionalTests.addTestSuite(TestMapSynonymtoPreferredNames.class);
	        functionalTests.addTestSuite(TestMembershipinVocabulary.class);
//	        functionalTests.addTestSuite(TestOrphanedConcept.class);
	        functionalTests.addTestSuite(TestOtherMatchingTechniques.class);
	        functionalTests.addTestSuite(TestPagedReturns.class);
	        functionalTests.addTestSuite(TestQuerybyRelationshipDomain.class);
	        functionalTests.addTestSuite(TestRelationshipInquiry.class);
	        functionalTests.addTestSuite(TestRetrieveConceptandAttributesbyCode.class);
	        functionalTests.addTestSuite(TestRetrieveConceptandAttributesbyPreferredName.class);
	        functionalTests.addTestSuite(TestRetrieveMostRecentVersionofConcept.class);
	        functionalTests.addTestSuite(TestRetrieveRelationsforConcept.class);
	        functionalTests.addTestSuite(TestRestrictToDirectionalNames.class);
	        functionalTests.addTestSuite(TestSearchbyStatus.class);
	        functionalTests.addTestSuite(TestSetofVocabulariesforSearch.class);
	        functionalTests.addTestSuite(TestSpecifyReturnOrder.class);
	        functionalTests.addTestSuite(TestSubsetExtraction.class);
	        functionalTests.addTestSuite(TestTransitiveClosure.class);
	        functionalTests.addTestSuite(TestTraverseGraphviaRoleLinks.class);
	        functionalTests.addTestSuite(TestVersionChanges.class);
	        functionalTests.addTestSuite(TestVersioningandAuthorityEnumeration.class);
	        functionalTests.addTestSuite(TestCodingSchemesWithSupportedAssociation.class);
	        functionalTests.addTestSuite(TestEnumerateAssociationNames.class);
	        functionalTests.addTestSuite(TestChildIndicator.class);
	        functionalTests.addTestSuite(TestPreLoadManifest.class);
	        functionalTests.addTestSuite(TestMRRANK.class);
	        functionalTests.addTestSuite(TestLoaderPreferences.class);
	        functionalTests.addTestSuite(TestOWLLoaderPreferences.class);
	        functionalTests.addTestSuite(TestSameCodeDifferentNamespace.class);
	        functionalTests.addTestSuite(TestPasswordEncryption.class);

	        mainSuite.addTest(functionalTests);

	        TestSuite bugTests = new TestSuite("Bug Regression Tests");
	        bugTests.addTestSuite(TestBugFixes.class);
	        bugTests.addTestSuite(GForge19650.class);
	        bugTests.addTestSuite(GForge19492.class);
	        bugTests.addTestSuite(GForge19573.class);
	        bugTests.addTestSuite(GForge19628.class);
	        bugTests.addTestSuite(GForge19629.class);
	        bugTests.addTestSuite(GForge19702.class);
	        bugTests.addTestSuite(GForge19741.class);
	        bugTests.addTestSuite(GForge19716.class);
	        bugTests.addTestSuite(GForge15976.class); 
	        bugTests.addTestSuite(GForge20525.class); 
	        bugTests.addTestSuite(GForge20651.class); 
	        bugTests.addTestSuite(GForge21211.class); 
	        bugTests.addTestSuite(GForge21567.class);
	        bugTests.addTestSuite(GForge21935.class);
	        bugTests.addTestSuite(GForge21923.class);
	        bugTests.addTestSuite(GForge22826.class);
	        bugTests.addTestSuite(GForge20875.class);
	        bugTests.addTestSuite(GForge23103.class);
	        //No longer supporting the MSAccess load
	        //bugTests.addTestSuite(GForge25067.class);
	        bugTests.addTestSuite(GForge26741.class);
	        bugTests.addTestSuite(GForge29772.class);
	        bugTests.addTestSuite(GForge29839.class);
	        bugTests.addTestSuite(GForge29840.class);
	        bugTests.addTestSuite(GForge29841.class);
	        bugTests.addTestSuite(GForge29842.class);
	        bugTests.addTestSuite(GForge29860.class);
	        bugTests.addTestSuite(GForge29924.class);
	        bugTests.addTestSuite(GForge29940.class);
	        bugTests.addTestSuite(GForge27457.class);
	        
	        bugTests.addTestSuite(DuplicatePropertyIdListenerTest.class);
	        
	        mainSuite.addTest(bugTests);
	        
	        TestSuite featureRequestTests = new TestSuite("Feature Request Tests");
	        featureRequestTests.addTestSuite(AddNamespaceToIndex.class);
	        featureRequestTests.addTestSuite(ChangeConfigFileName.class);  
	        featureRequestTests.addTestSuite(GForge17019.class);
	        //No longer supporting the MSAccess Load
	        //featureRequestTests.addTestSuite(GForge24191.class);
	        mainSuite.addTest(featureRequestTests);
	        
	        //Mapping tests
	        TestSuite mappingTests = new TestSuite("Mapping Tests");
	        mappingTests.addTestSuite(MappingToNodeListTest.class);
	        mainSuite.addTest(mappingTests);
	      
	        //Mapping Extension tests
	        mainSuite.addTestSuite(MappingExtensionImplTest.class);
	        
	        //Search Extesion tests
	        mainSuite.addTestSuite(SearchExtensionImplTest.class);
	        
	        //Association Authoring Mapping tests
	        mainSuite.addTest(org.LexGrid.LexBIG.mapping.MappingAllTests.suite());
	        
	        //Clean up the data base
	        mainSuite.addTestSuite(CleanUpTest.class);
	        return mainSuite;
	  }
}
