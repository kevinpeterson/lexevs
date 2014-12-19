package org.lexevs.dao.index.service.entity;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.concepts.Entity;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.ScoreDoc;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/lexevsDao-all-in-memory-test.xml"})
public class LuceneEntityIndexServiceTest {

    @Autowired
    private LuceneEntityIndexService service;

    @Test
    public void testInit() {
        assertNotNull(this.service);
    }

    @Test
    public void testAddEntity() {
        Entity entity = new Entity();
        this.service.addEntityToIndex("someuri", "v1", entity);

        Set<AbsoluteCodingSchemeVersionReference> cs = new HashSet<AbsoluteCodingSchemeVersionReference>();
        cs.add(Constructors.createAbsoluteCodingSchemeVersionReference("someuri", "v1"));

        List<ScoreDoc> docs = this.service.query(cs, new MatchAllDocsQuery());

        assertEquals(1, docs.size());
    }


}
