package com.sergeykotov.adapter.system;

import com.sergeykotov.adapter.domain.Rule;

import java.util.List;

public interface System {
    String getName();

    List<Rule> getRules();

    boolean createRuleSet(Rule rule);

    boolean deleteRuleSet(Rule rule);
}