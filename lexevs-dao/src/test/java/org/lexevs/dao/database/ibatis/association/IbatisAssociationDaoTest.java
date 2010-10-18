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
package org.lexevs.dao.database.ibatis.association;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.annotation.Resource;

import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;
import org.junit.Test;
import org.lexevs.dao.database.access.association.model.Triple;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class IbatisAssociationDaoTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@TransactionConfiguration
public class IbatisAssociationDaoTest extends LexEvsDbUnitTestBase {

	/** The ibatis association dao. */
	@Resource
	private IbatisAssociationDao ibatisAssociationDao;

	/**
	 * Test get key for association instance id.
	 * 
	 * @throws SQLException
	 *             the SQL exception
	 */
	@Test
	@Transactional
	public void testGetKeyForAssociationInstanceId() throws SQLException {

		JdbcTemplate template = new JdbcTemplate(this.getDataSource());

		template
				.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) "
						+ "values ('1', 'csname', 'csuri', 'csversion')");

		template.execute("insert into "
				+ "relation (relationGuid, codingSchemeGuid, containerName) "
				+ "values ('1', '1', 'c-name')");
		template.execute("insert into "
				+ "associationpredicate (associationPredicateGuid,"
				+ "relationGuid, associationName) values "
				+ "('1', '1', 'apname')");
		template.execute("insert into entityassnstoentity"
				+ " values ('1'," + " '1'," + " 's-code', "
				+ " 's-ns'," + " 't-code'," + " 't-ns',"
				+ " 'ai-id', null, null, null, null, null, null, null, null)");

		String key = ibatisAssociationDao.getKeyForAssociationInstanceId(
				"1", "ai-id");
		assertEquals("1", key);
	}

	/**
	 * Test insert association qualifier.
	 * 
	 * @throws SQLException
	 *             the SQL exception
	 */
	@Test
	@Transactional
	public void testInsertAssociationQualifier() throws SQLException {
		AssociationQualification qual = new AssociationQualification();
		qual.setAssociationQualifier("qualName");
		qual.setQualifierText(DaoUtility.createText("qual text"));

		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template
				.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) "
						+ "values ('1', 'csname', 'csuri', 'csversion')");

		template.execute("insert into "
				+ "relation (relationGuid, codingSchemeGuid, containerName) "
				+ "values ('1', '1', 'c-name')");
		template.execute("insert into "
				+ "associationpredicate (associationPredicateGuid,"
				+ "relationGuid, associationName) values "
				+ "('1', '1', 'apname')");
		template.execute("insert into entityassnstoentity"
				+ " values ('1'," + " '1'," + " 's-code', "
				+ " 's-ns'," + " 't-code'," + " 't-ns',"
				+ " 'ai-id', null, null, null, null, null, null, null, null)");

		ibatisAssociationDao.insertAssociationQualifier("1", "ai-id",
				qual);

		template.queryForObject("Select * from entityassnquals",
				new RowMapper() {

					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {

						assertNotNull(rs.getString(1));
						assertEquals(rs.getString(2), "1");
						assertEquals(rs.getString(3), "qualName");
						assertEquals(rs.getString(4), "qual text");

						return true;
					}
				});
	}

	@Test
	@Transactional
	public void getAllTriplesOfCodingSchemeLimit1() throws SQLException {
		int limit = 1000;

		AssociationQualification qual = new AssociationQualification();
		qual.setAssociationQualifier("qualName");
		qual.setQualifierText(DaoUtility.createText("qual text"));

		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template
				.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) "
						+ "values ('1', 'csname', 'csuri', 'csversion')");

		template.execute("insert into "
				+ "relation (relationGuid, codingSchemeGuid, containerName) "
				+ "values ('1', '1', 'c-name')");

		template.execute("insert into "
				+ "associationpredicate (associationPredicateGuid,"
				+ "relationGuid, associationName) values "
				+ "('1', '1', 'apname')");

		for (int i = 0; i < limit; i++) {
			template
					.execute("insert into entityassnstoentity"
							+ " values ('1"
							+ String.valueOf(i)
							+ "',"
							+ " '1',"
							+ " 's"
							+ String.valueOf(i)
							+ "', "
							+ " 's-ns',"
							+ " 't"
							+ String.valueOf(i)
							+ "', "
							+ " 't-ns',"
							+ " 'ai-id', null, null, null, null, null, null, null, null)");
		}

		List<Triple> triples = ibatisAssociationDao
				.getAllTriplesOfCodingScheme("1", "1", 0, 1);
		assertEquals(1, triples.size());

		Triple triple = triples.get(0);
		assertEquals("s0", triple.getSourceEntityCode());
		assertEquals("t0", triple.getTargetEntityCode());
		assertEquals("s-ns", triple.getSourceEntityNamespace());
		assertEquals("t-ns", triple.getTargetEntityNamespace());
		assertEquals("1", triple.getAssociationPredicateId());
	}

	public void getAllTriplesOfCodingSchemeTestStart() throws SQLException {
		int limit = 1000;

		AssociationQualification qual = new AssociationQualification();
		qual.setAssociationQualifier("qualName");
		qual.setQualifierText(DaoUtility.createText("qual text"));

		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template
				.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) "
						+ "values ('1', 'csname', 'csuri', 'csversion')");

		template.execute("insert into "
				+ "relation (relationGuid, codingSchemeGuid, containerName) "
				+ "values ('1', '1', 'c-name')");

		template.execute("insert into "
				+ "associationpredicate (associationPredicateGuid,"
				+ "relationGuid) values " + "('1', '1')");

		for (int i = 0; i < limit; i++) {
			template
					.execute("insert into entityassnstoentity"
							+ " values ('1"
							+ String.valueOf(i)
							+ "',"
							+ " '1',"
							+ " 's"
							+ String.valueOf(i)
							+ "', "
							+ " 's-ns',"
							+ " 't"
							+ String.valueOf(i)
							+ "', "
							+ " 't-ns',"
							+ " 'ai-id', null, null, null, null, null, null, null, null)");
		}

		List<Triple> triples = ibatisAssociationDao
				.getAllTriplesOfCodingScheme("1", "1", 500, 1);
		assertEquals(1, triples.size());

		Triple triple = triples.get(0);
		assertEquals("s499", triple.getSourceEntityCode());
		assertEquals("t499", triple.getTargetEntityCode());
		assertEquals("s-ns", triple.getSourceEntityNamespace());
		assertEquals("t-ns", triple.getTargetEntityNamespace());
		assertEquals("1", triple.getAssociationPredicateId());
	}

	/**
	 * Test insert relations.
	 * 
	 * @throws SQLException
	 *             the SQL exception
	 */
	@Test
	@Transactional
	public void testInsertRelations() throws SQLException {

		Relations relations = new Relations();
		relations.setContainerName("container name");

		EntityDescription ed = new EntityDescription();
		ed.setContent("a description");
		relations.setEntityDescription(ed);

		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template
				.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) "
						+ "values ('1', 'csname', 'csuri', 'csversion')");

		ibatisAssociationDao.insertRelations("1", relations, true);

		template.queryForObject("Select * from relation", new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {

				assertNotNull(rs.getString(1));
				assertEquals(rs.getString(2), "1");
				assertEquals(rs.getString(3), "container name");
				assertEquals(rs.getString(10), "a description");

				return true;
			}
		});
	}

	/**
	 * Test insert association source.
	 * 
	 * @throws SQLException
	 *             the SQL exception
	 */
	@Test
	@Transactional
	public void testInsertAssociationSource() throws SQLException {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template
				.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) "
						+ "values ('1', 'csname', 'csuri', 'csversion')");
		template.execute("insert into "
				+ "relation (relationGuid, codingSchemeGuid, containerName) "
				+ "values ('1', '1', 'c-name')");
		template.execute("insert into "
				+ "associationpredicate (associationPredicateGuid,"
				+ "relationGuid, associationName) values "
				+ "('1', '1', 'apname')");

		final Timestamp effectiveDate = new Timestamp(1l);
		final Timestamp expirationDate = new Timestamp(2l);

		AssociationSource source = new AssociationSource();
		source.setSourceEntityCode("s-code");
		source.setSourceEntityCodeNamespace("s-ns");

		AssociationTarget target = new AssociationTarget();
		target.setAssociationInstanceId("aii");
		target.setTargetEntityCode("t-code");
		target.setTargetEntityCodeNamespace("t-ns");
		target.setIsDefining(true);
		target.setIsInferred(false);
		target.setIsActive(true);

		AssociationQualification qual = new AssociationQualification();
		qual.setAssociationQualifier("qualName");
		qual.setQualifierText(DaoUtility.createText("qual value"));

		target.addAssociationQualification(qual);
		target.addUsageContext("usage Context");

		source.addTarget(target);

		target.setOwner("source owner");

		target.setStatus("testing");

		target.setEffectiveDate(effectiveDate);
		target.setExpirationDate(expirationDate);

		ibatisAssociationDao.insertAssociationSource("1", "1",
				source);

		template.queryForObject("Select * from entityassnstoentity",
				new RowMapper() {

					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {

						assertNotNull(rs.getString(1));
						assertEquals(rs.getString(2), "1");
						assertEquals(rs.getString(3), "s-code");
						assertEquals(rs.getString(4), "s-ns");
						assertEquals(rs.getString(5), "t-code");
						assertEquals(rs.getString(6), "t-ns");
						assertEquals(rs.getString(7), "aii");
						assertEquals(rs.getBoolean(8), true);
						assertEquals("0",rs.getString(9));
						assertEquals("1",rs.getString(10));
						assertEquals(rs.getString(11), "source owner");
						assertEquals(rs.getString(12), "testing");
						assertEquals(rs.getTimestamp(13), effectiveDate);
						assertEquals(rs.getTimestamp(14), expirationDate);

						return true;
					}
				});

		assertEquals(2, template.queryForObject(
				"Select count(*) from entityassnquals", Integer.class));
	}

	@Test
	@Transactional
	public void testDeleteAllAssocQuals() throws SQLException {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template
				.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) "
						+ "values ('1', 'csname', 'csuri', 'csversion')");
		template.execute("insert into "
				+ "relation (relationGuid, codingSchemeGuid, containerName) "
				+ "values ('1', '1', 'c-name')");
		template.execute("insert into "
				+ "associationpredicate (associationPredicateGuid,"
				+ "relationGuid, associationName) values "
				+ "('1', '1', 'apname')");

		template.execute("insert into " + "entityassnstoentity ("
				+ "entityAssnsGuid, " + "associationPredicateGuid, "
				+ "sourceEntityCode, " + "sourceEntityCodeNamespace, "
				+ "targetEntityCode, " + "targetEntityCodeNamespace "
				+ ") values " + "('1', " + "'1'," + "'sc',"
				+ "'sns'," + "'tc'," + "'tns')");

		template.execute("insert into " + "entityassnstodata ("
				+ "entityAssnsDataGuid, " + "associationPredicateGuid, "
				+ "sourceEntityCode, " + "sourceEntityCodeNamespace) values "
				+ "('1', " + "'1'," + "'sc'," + "'sns')");

		template.execute("insert into " + "entityassnquals values ( "
				+ "'1', " + "'1'," + "'qualName1',"
				+ "'qualValue'," + "'1' )");

		template.execute("insert into " + "entityassnquals values ( "
				+ "'2', " + "'1'," + "'qualName2',"
				+ "'qualValue'," + "'1' )");

		assertEquals(2, template
				.queryForInt("select count(*) from entityassnquals"));

		ibatisAssociationDao
				.deleteAssociationQualificationsByCodingSchemeUId("1");

		assertEquals(0, template
				.queryForInt("select count(*) from entityassnquals"));
	}

	@Test
	@Transactional
	public void testGetAssociationPredicateIdsForRelationsId()
			throws SQLException {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template
				.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) "
						+ "values ('1', 'csname', 'csuri', 'csversion')");

		template.execute("insert into "
				+ "relation (relationGuid, codingSchemeGuid, containerName) "
				+ "values ('1', '1', 'c-name')");

		template
				.execute("insert into "
						+ "associationpredicate (associationPredicateGuid, relationGuid, associationName) values "
						+ "('11', '1', 'apName1')");

		template
				.execute("insert into "
						+ "associationpredicate (associationPredicateGuid, relationGuid, associationName) values "
						+ "('12', '1', 'apName2')");

		List<String> assocPredIds = ibatisAssociationDao
				.getAssociationPredicateUIdsForRelationsUId("1",
						"1");

		assertEquals(2, assocPredIds.size());

		assertTrue(assocPredIds.contains("11"));
		assertTrue(assocPredIds.contains("12"));
	}

	@Test
	@Transactional
	public void testGetRelationsIdsForCodingSchemeId() throws SQLException {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template
				.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) "
						+ "values ('1', 'csname', 'csuri', 'csversion')");

		template.execute("insert into "
				+ "relation (relationGuid, codingSchemeGuid, containerName) "
				+ "values ('11', '1', 'c-name1')");

		template.execute("insert into "
				+ "relation (relationGuid, codingSchemeGuid, containerName) "
				+ "values ('12', '1', 'c-name2')");

		List<String> relationsIds = ibatisAssociationDao
				.getRelationsUIdsForCodingSchemeUId("1");

		assertEquals(2, relationsIds.size());

		assertTrue(relationsIds.contains("11"));
		assertTrue(relationsIds.contains("12"));
	}

	/**
	 * Test insert association predicate.
	 * 
	 * @throws SQLException
	 *             the SQL exception
	 */
	@Test
	@Transactional
	public void testInsertAssociationPredicate() throws SQLException {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template
				.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) "
						+ "values ('1', 'csname', 'csuri', 'csversion')");
		template.execute("insert into "
				+ "relation (relationGuid, codingSchemeGuid, containerName) "
				+ "values ('1', '1', 'c-name')");

		AssociationPredicate predicate = new AssociationPredicate();
		predicate.setAssociationName("assoc-name");

		ibatisAssociationDao.insertAssociationPredicate("1", "1",
				predicate, true);

		template.queryForObject("Select * from associationpredicate",
				new RowMapper() {

					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {

						assertNotNull(rs.getString(1));
						assertEquals(rs.getString(2), "1");
						assertEquals(rs.getString(3), "assoc-name");

						return true;
					}
				});
	}

	/**
	 * Test insert transitive closure.
	 * 
	 * @throws SQLException
	 *             the SQL exception
	 */
	@Test
	@Transactional
	public void testInsertTransitiveClosure() throws SQLException {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template
				.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) "
						+ "values ('1', 'csname', 'csuri', 'csversion')");
		template.execute("insert into "
				+ "relation (relationGuid, codingSchemeGuid, containerName) "
				+ "values ('1', '1', 'c-name')");
		template.execute("insert into "
				+ "associationpredicate (associationPredicateGuid,"
				+ "relationGuid, associationName) values "
				+ "('1', '1', 'apname')");

		ibatisAssociationDao.insertIntoTransitiveClosure("1", "1",
				"sc", "sns", "tc", "tns", "path|,path->path|,path");

		template.queryForObject("Select * from entityassnstoentitytr",
				new RowMapper() {

					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {

						assertNotNull(rs.getString(1));
						assertEquals("1", rs.getString(2));
						assertEquals("sc", rs.getString(3));
						assertEquals("sns", rs.getString(4));
						assertEquals("tc", rs.getString(5));
						assertEquals("tns", rs.getString(6));
						assertEquals("path|,path->path|,path", rs.getString(7));
						return true;
					}
				});
	}

	@Test
	public void testGetNodesPath() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) "
						+ "values ('1', 'csname', 'csuri', 'csversion')");
		template.execute("insert into "
				+ "relation (relationGuid, codingSchemeGuid, containerName) "
				+ "values ('1', '1', 'c-name')");
		template.execute("insert into "
				+ "associationpredicate (associationPredicateGuid,"
				+ "relationGuid, associationName) values "
				+ "('1', '1', 'apname')");
		template.execute("Insert into entityassnstoentitytr (entityAssnsTrGuid, associationPredicateGuid, sourceEntityCode, sourceEntityCodeNamespace, targetEntityCode, targetEntityCodeNamespace, path)"
				+ "values ('1', '1', 'src-code', 'src-ns', 'tgt-code', 'tgt-ns', 'path')");
		String path = ibatisAssociationDao.getNodesPath("1","src-code", "src-ns",
				"tgt-code", "tgt-ns", "1");
		assertEquals("path", path);
		// AssociationPredicate id is null
		path = ibatisAssociationDao.getNodesPath("1", "src-code", "src-ns",
				"tgt-code", "tgt-ns", null);
		assertEquals("path", path);
	}
}