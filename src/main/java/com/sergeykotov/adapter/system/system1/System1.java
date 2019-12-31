package com.sergeykotov.adapter.system.system1;

import com.sergeykotov.adapter.domain.Rule;
import com.sergeykotov.adapter.exception.NotFoundException;
import com.sergeykotov.adapter.system.System;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class System1 implements System {
    private static final Logger log = LoggerFactory.getLogger(System1.class);
    private static final String NAME = "System1";

    private List<Rule> rules = new ArrayList<>();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public List<Rule> getRules() {
        log.info("extracting rules from " + NAME + "...");
        log.info(rules.size() + " rules have been extracted from " + NAME);
        return rules;
    }

    @Override
    public String getRule(Rule rule) throws NotFoundException {
        log.info("extracting rule " + rule + " from " + NAME + "...");
        Rule existingRule = rules.stream().filter(r -> r.equals(rule)).findAny().orElseThrow(NotFoundException::new);
        log.info("rule " + rule + " has been extracted from " + NAME);
        return existingRule.getSystemRuleMap().get(NAME);
    }

    @Override
    public boolean createRule(Rule rule) {
        log.info("creating rule " + rule + " on " + NAME + "...");
        if (rules.contains(rule)) {
            log.error("failed to create rule " + rule + " on " + NAME);
            return false;
        }
        String json = "{}";
        rule.getSystemRuleMap().put(NAME, json);
        rules.add(rule);
        log.info("rule " + rule + " has been created on " + NAME);
        return true;
    }

    @Override
    public boolean updateRule(Rule rule) {
        log.info("updating rule " + rule + " on " + NAME + "...");
        Optional<Rule> existingRule = rules.stream().filter(r -> r.equals(rule)).findAny();
        if (!existingRule.isPresent()) {
            log.error("failed to update rule " + rule + " on " + NAME + ": not found");
            return false;
        }
        String json = rule.getSystemRuleMap().get(NAME);
        existingRule.get().getSystemRuleMap().put(NAME, json);
        log.info("rule " + rule + " has been updated on " + NAME);
        return true;
    }

    @Override
    public boolean deleteRule(Rule rule) {
        log.info("deleting rule " + rule + " on " + NAME + "...");
        boolean deleted = rules.remove(rule);
        if (!deleted) {
            log.error("failed to delete rule " + rule + " on " + NAME);
            return false;
        }
        log.info("rule " + rule + " has been deleted on " + NAME);
        return true;
    }

    @Override
    public String toString() {
        return getName();
    }
}