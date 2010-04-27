package org.lexevs.dao.database.operation.transitivity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.lexevs.dao.database.access.association.model.Node;
import org.lexevs.dao.database.ibatis.codingscheme.IbatisCodingSchemeDao;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;
import org.lexevs.registry.utility.RegistryUtility;
import org.lexevs.system.service.LexEvsResourceManagingService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

public class DefaultTransitivityBuilderTest extends LexEvsDbUnitTestBase {

	@Resource
	private DefaultTransitivityBuilder defaultTransitivityBuilder;
	
	@Resource
	private Registry registry;
	
	@Resource
	private LexEvsResourceManagingService lexEvsResourceManagingService;
	
	@Resource
	private IbatisCodingSchemeDao ibatisCodingSchemeDao;
	
	@Resource
	private CodingSchemeService codingSchemeService;

	@Test
	public void getRegistryEntryForCodingSchemeName() throws Exception {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri", "csversion"));
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
				"values ('cs-guid', 'csname', 'csuri', 'csversion')");
		
		lexEvsResourceManagingService.refresh();
		
		template.execute("Insert into cssupportedattrib (csSuppAttribGuid, codingSchemeGuid, supportedAttributeTag, id, assnCodingScheme, assnNamespace, assnEntityCode) " +
				"values ('cssa-guid', 'cs-guid', 'Association', 'test-assoc', 'csname', 'ae-code', 'ae-codens')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
				"values ('rel-guid', 'cs-guid', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
				"('ap-guid', 'rel-guid', 'test-assoc')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, isTransitive) " +
				"values ('eguid', 'cs-guid', 'ae-code', 'ae-codens', true)");
		
		template.execute("insert into entityassnstoentity" +
				" values ('eae-guid1'," +
				" 'ap-guid'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1'," +
				" 'ai-id', null, null, null, null, null, null, null, null)");
		
		RegistryEntry entry = 
			defaultTransitivityBuilder.getRegistryEntryForCodingSchemeName("csname");
		
		assertNotNull(entry);
		
		assertEquals("csuri", entry.getResourceUri());
		assertEquals("csversion", entry.getResourceVersion());
	}
	
	@Test
	public void getDistinctSourceTriples() throws Exception {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri", "csversion"));
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
				"values ('cs-guid', 'csname', 'csuri', 'csversion')");
		
		lexEvsResourceManagingService.refresh();
		
		template.execute("Insert into cssupportedattrib (csSuppAttribGuid, codingSchemeGuid, supportedAttributeTag, id, assnCodingScheme, assnNamespace, assnEntityCode) " +
				"values ('cssa-guid', 'cs-guid', 'Association', 'test-assoc', 'csname', 'ae-code', 'ae-codens')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
				"values ('rel-guid', 'cs-guid', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
				"('ap-guid', 'rel-guid', 'test-assoc')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, isTransitive) " +
				"values ('eguid', 'cs-guid', 'ae-code', 'ae-codens', true)");
		
		template.execute("insert into entityassnstoentity" +
				" values ('eae-guid1'," +
				" 'ap-guid'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1'," +
				" 'ai-id', null, null, null, null, null, null, null, null)");
		
		List<Node> nodes = 
			defaultTransitivityBuilder.getDistinctSourceTriples("csuri", "csversion", "ap-guid");
		
		assertEquals(1, nodes.size());
	}

	@Test
	@Transactional
	public void getTransitiveAssociationPredicateIds() throws Exception {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri", "csversion"));
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
				"values ('cs-guid', 'csname', 'csuri', 'csversion')");
		
		lexEvsResourceManagingService.refresh();
		
		template.execute("Insert into cssupportedattrib (csSuppAttribGuid, codingSchemeGuid, supportedAttributeTag, id, assnCodingScheme, assnEntityCode, assnNamespace) " +
				"values ('cssa-guid', 'cs-guid', 'Association', 'test-assoc', 'csname', 'ae-code', 'ae-codens')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
				"values ('rel-guid', 'cs-guid', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
				"('ap-guid', 'rel-guid', 'test-assoc')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, isTransitive) " +
				"values ('eguid', 'cs-guid', 'ae-code', 'ae-codens', true)");
		
		template.execute("Insert into entitytype (entityGuid, entityType) " +
				"values ('eguid', 'association')");

		List<String> transitiveAssocs = 
			defaultTransitivityBuilder.getTransitiveAssociationPredicateIds("csuri", "csversion");
		
		assertEquals(1, transitiveAssocs.size());
		assertEquals("ap-guid", transitiveAssocs.get(0));
	}
	
	@Test
	public void isTransitive() throws Exception {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri", "csversion"));
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
				"values ('cs-guid', 'csname', 'csuri', 'csversion')");
		
		lexEvsResourceManagingService.refresh();
		
		template.execute("Insert into cssupportedattrib (csSuppAttribGuid, codingSchemeGuid, supportedAttributeTag, id, assnCodingScheme, assnNamespace, assnEntityCode) " +
				"values ('cssa-guid', 'cs-guid', 'Association', 'test-assoc', 'csname', 'ae-code', 'ae-codens')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
				"values ('rel-guid', 'cs-guid', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
				"('ap-guid', 'rel-guid', 'test-assoc')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, isTransitive) " +
				"values ('eguid', 'cs-guid', 'ae-code', 'ae-codens', true)");
		
		template.execute("Insert into entitytype (entityGuid, entityType) " +
				"values ('eguid', 'association')");
		
		boolean isTransitive =
			defaultTransitivityBuilder.isTransitive("csuri", "csversion", "ae-code", "ae-codens");
		
		assertTrue(isTransitive);
	}
	
	@Test
	public void isNotTransitive() throws Exception {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri", "csversion"));
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
				"values ('cs-guid', 'csname', 'csuri', 'csversion')");
		
		lexEvsResourceManagingService.refresh();
		
		template.execute("Insert into cssupportedattrib (csSuppAttribGuid, codingSchemeGuid, supportedAttributeTag, id, assnCodingScheme, assnNamespace, assnEntityCode) " +
				"values ('cssa-guid', 'cs-guid', 'Association', 'test-assoc', 'csname', 'ae-code', 'ae-codens')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
				"values ('rel-guid', 'cs-guid', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
				"('ap-guid', 'rel-guid', 'test-assoc')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, isTransitive) " +
				"values ('eguid', 'cs-guid', 'ae-code', 'ae-codens', false)");
		
		template.execute("Insert into entitytype (entityGuid, entityType) " +
				"values ('eguid', 'association')");
		
		boolean isTransitive =
			defaultTransitivityBuilder.isTransitive("csuri", "csversion", "ae-code", "ae-codens");
		
		assertFalse(isTransitive);
	}
	
	@Test
	public void testOneLevel() throws Exception {

		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri", "csversion"));

		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
				"values ('cs-guid', 'csname', 'csuri', 'csversion')");
		
		lexEvsResourceManagingService.refresh();
		
		template.execute("Insert into cssupportedattrib (csSuppAttribGuid, codingSchemeGuid, supportedAttributeTag, id, assnCodingScheme, assnEntityCode, assnNamespace) " +
				"values ('cssa-guid', 'cs-guid', 'Association', 'test-assoc', 'csname', 'ae-code', 'ae-codens')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
				"values ('rel-guid', 'cs-guid', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
				"('ap-guid', 'rel-guid', 'test-assoc')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, isNavigable, isTransitive) " +
				"values ('eguid', 'cs-guid', 'ae-code', 'ae-codens', true, true)");
		
		template.execute("Insert into entitytype (entityGuid, entityType) " +
				"values ('eguid', 'association')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('eae-guid1'," +
				" 'ap-guid'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1'," +
				" 'ai-id', null, null, null, null, null, null, null, null)");

		defaultTransitivityBuilder.computeTransitivityTable("csuri", "csversion");
		
		int count = template.queryForInt("select count(*) from entityassnstoentitytr");
		
		assertEquals(1, count);
		
		assertTrue((Boolean)template.queryForObject("Select * from entityassnstoentitytr", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				
				assertNotNull(rs.getString(1));
				assertEquals(rs.getString(2), "ap-guid");
				assertEquals(rs.getString(3), "s-code");
				assertEquals(rs.getString(4), "s-ns");
				assertEquals(rs.getString(5), "t-code1");
				assertEquals(rs.getString(6), "t-ns1");

				return true;
			}
		}));
	}
	
	@Test
	public void testTwoLevel() throws Exception {

		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri", "csversion"));

		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
				"values ('cs-guid', 'csname', 'csuri', 'csversion')");
		
		lexEvsResourceManagingService.refresh();
		
		template.execute("Insert into cssupportedattrib (csSuppAttribGuid, codingSchemeGuid, supportedAttributeTag, id, assnCodingScheme, assnEntityCode, assnNamespace) " +
				"values ('cssa-guid', 'cs-guid', 'Association', 'test-assoc', 'csname', 'ae-code', 'ae-codens')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
				"values ('rel-guid', 'cs-guid', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
				"('ap-guid', 'rel-guid', 'test-assoc')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, isNavigable, isTransitive) " +
				"values ('eguid', 'cs-guid', 'ae-code', 'ae-codens', true, true)");
		
		template.execute("Insert into entitytype (entityGuid, entityType) " +
				"values ('eguid', 'association')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('eae-guid1'," +
				" 'ap-guid'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1'," +
				" 'ai-id1', null, null, null, null, null, null, null, null)");
		
		template.execute("insert into entityassnstoentity" +
				" values ('eae-guid2'," +
				" 'ap-guid'," +
				" 't-code1', " +
				" 't-ns1'," +
				" 't-code2'," +
				" 't-ns2'," +
				" 'ai-id2', null, null, null, null, null, null, null, null)");

		defaultTransitivityBuilder.computeTransitivityTable("csuri", "csversion");
		
		int count = template.queryForInt("select count(*) from entityassnstoentitytr");
		
		assertEquals(3, count);
	}
}
