package com.sergeykotov.adapter.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Rule {
    @Min(1)
    private long id;

    @NotEmpty
    private String name;

    private String note;

    //key - system name
    //value - json about the rule on the system
    private Map<String, String> systemRuleMap = new HashMap<>();

    public Rule() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @JsonProperty("system_rule_map")
    public Map<String, String> getSystemRuleMap() {
        return systemRuleMap;
    }

    public void setSystemRuleMap(Map<String, String> systemRuleMap) {
        this.systemRuleMap = systemRuleMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;
        return Objects.equals(getName(), rule.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public String toString() {
        return getName();
    }
}