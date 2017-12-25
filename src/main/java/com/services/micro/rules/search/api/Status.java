package com.services.micro.rules.search.api;

import java.io.Serializable;

public enum Status implements Serializable {
    ACTIVE("active"),
    INACTIVE("inactive"),
    DEFAULT("inactive");

    private String name;

    Status(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Status getStatus(String name) {
        for (Status status: Status.values()) {
            if (status.getName().equals(name)) {
                return status;
            }
        }
        return DEFAULT;
    }

    @Override
    public String toString() {
        return "Status{" +
                "name='" + name + '\'' +
                '}';
    }
}
