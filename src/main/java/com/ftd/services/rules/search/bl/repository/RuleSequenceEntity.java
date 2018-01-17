package com.ftd.services.rules.search.bl.repository;

import java.io.Serializable;

public class RuleSequenceEntity implements Serializable {
    private static final long serialVersionUID = -3462427577309415456L;

    private String            id;

    private int               seq;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUpdateSequence() {
        return seq;
    }

    public void setUpdateSequence(int seq) {
        this.seq = seq;
    }

    @Override
    public String toString() {
        return "RuleSequenceEntity{" +
                "id='" + id + '\'' +
                ", seq='" + seq +
                '}';
    }

}
