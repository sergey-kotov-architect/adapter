package com.sergeykotov.adapter.domain;

import javax.validation.constraints.NotNull;

public class RuleSet {
    @NotNull
    private String name;
    private boolean deployed;

    public RuleSet() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDeployed() {
        return deployed;
    }

    public void setDeployed(boolean deployed) {
        this.deployed = deployed;
    }

    @Override
    public String toString() {
        return getName();
    }
}