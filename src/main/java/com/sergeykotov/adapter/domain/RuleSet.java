package com.sergeykotov.adapter.domain;

import javax.validation.constraints.NotEmpty;

public class RuleSet {
    @NotEmpty
    private String name;

    public RuleSet() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }
}