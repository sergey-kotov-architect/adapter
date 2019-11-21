package com.sergeykotov.adapter.system;

import com.sergeykotov.adapter.domain.Rule;
import com.sergeykotov.adapter.exception.NotFoundException;

import java.util.List;

public interface System {
    String getName();

    List<Rule> getRules();

    String getRule(Rule rule) throws NotFoundException;

    boolean createRule(Rule rule);

    boolean updateRule(Rule rule);

    boolean deleteRule(Rule rule);
}