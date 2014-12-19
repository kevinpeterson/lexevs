package org.lexevs.dao.index.lucene.entity;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lexevs.dao.database.ibatis.entity.model.IdableEntity;
import org.lexevs.dao.index.model.IndexableEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/lexevsDao-all-in-memory-test.xml"})
public class LuceneEntityDaoTest {

    @Autowired
    private LuceneEntityDao dao;

    @Test
    public void testSetup() {
        assertNotNull(this.dao);
    }

    @Test
    public void testInsertAndQuery() {
        IdableEntity entity = new IdableEntity();
        entity.setId("id");
        entity.setEntityCode("111");
        entity.setEntityCodeNamespace("ns");
        IndexableEntity doc = new IndexableEntity(entity, "test", "v1");

        this.dao.addDocuments("test", "v1", Arrays.asList(doc), new StandardAnalyzer());

        assertEquals(1, this.dao.query("test", "v1", new MatchAllDocsQuery()).size());
    }

}
