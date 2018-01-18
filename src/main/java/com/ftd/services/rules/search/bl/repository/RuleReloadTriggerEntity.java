package com.ftd.services.rules.search.bl.repository;

import java.io.Serializable;

public class RuleReloadTriggerEntity implements Serializable {
    private static final long serialVersionUID = -3462427577309415456L;

    private String            id;

    private Long              lastUpdatedMS;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "RuleReloadTriggerEntity{" +
                "id='" + id + '\'' +
                ", lastUpdateMS='" + lastUpdatedMS.longValue() +
                '}';
    }

    public Long getLastUpdatedMS() {
        return lastUpdatedMS;
    }

    public void setLastUpdatedMS(Long lastUpdatedMS) {
        this.lastUpdatedMS = lastUpdatedMS;
    }

}
