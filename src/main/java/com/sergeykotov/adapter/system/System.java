package com.sergeykotov.adapter.system;

import com.sergeykotov.adapter.domain.Rule;

import java.util.List;

public interface System {
    String getName();

    List<Rule> getRules();

    Rule getRule(long id);

    boolean createRule(Rule rule);

    boolean updateRule(Rule rule);

    boolean deleteRule(Rule rule);
}