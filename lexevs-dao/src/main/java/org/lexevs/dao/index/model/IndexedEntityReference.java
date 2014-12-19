package org.lexevs.dao.index.model;


public class IndexedEntityReference {

    private float score;
    private int id;

    public IndexedEntityReference( int id, float score) {
        this.id = id;
        this.score = score;
    }

    public float getScore() {
        return score;
    }

    public int getId() {
        return id;
    }
}
