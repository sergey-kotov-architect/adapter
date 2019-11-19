package com.sergeykotov.adapter.system.system1;

import com.sergeykotov.adapter.domain.Rule;
import com.sergeykotov.adapter.exception.ExtractionException;
import com.sergeykotov.adapter.exception.NotFoundException;
import com.sergeykotov.adapter.system.System;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class System1 implements System {
    private static final Logger log = Logger.getLogger(System1.class);
    private static final String NAME = "System1";

    private List<Rule> rules = new ArrayList<>();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public List<Rule> getRules() {
        log.info("extracting rules...");
        try {
            Thread.sleep(2_000L); //latency simulation
        } catch (InterruptedException e) {
            log.error("rules extraction has been interrupted");
            throw new ExtractionException();
        }
        log.info(rules.size() + " rules have been extracted");
        return rules;
    }

    @Override
    public Rule getRule(long id) {
        log.info("extracting rule by ID " + id + "...");
        Rule rule = rules.stream().filter(r -> r.getId() == id).findAny().orElseThrow(NotFoundException::new);
        log.info("rule has been extracted by ID " + id);
        return rule;
    }

    @Override
    public boolean createRule(Rule rule) {
        log.info("creating rule " + rule + "...");
        try {
            Thread.sleep(15_000L); //latency simulation
        } catch (InterruptedException e) {
            log.error("rule creation has been interrupted");
            return false;
        }
        if (rules.contains(rule)) {
            log.error("failed to create rule " + rule);
            return false;
        }
        String json = "{}";
        rule.getSystemRuleMap().put(getName(), json);
        rules.add(rule);
        log.info("rule " + rule + " has been created");
        return true;
    }

    @Override
    public boolean updateRule(Rule rule) {
        log.info("updating rule " + rule + "...");
        try {
            Thread.sleep(15_000L); //latency simulation
        } catch (InterruptedException e) {
            log.error("rule update has been interrupted");
            return false;
        }
        Rule existingRule = getRule(rule.getId());
        existingRule.setNote(rule.getNote());
        log.info("rule " + rule + " has been updated");
        return true;
    }

    @Override
    public boolean deleteRule(Rule rule) {
        log.info("deleting rule " + rule + "...");
        try {
            Thread.sleep(10_000L); //latency simulation
        } catch (InterruptedException e) {
            log.error("rule deletion has been interrupted");
            return false;
        }
        boolean deleted = rules.remove(rule);
        if (!deleted) {
            log.error("failed to delete rule " + rule);
            return false;
        }
        log.info("rule " + rule + " has been deleted");
        return true;
    }

    @Override
    public String toString() {
        return getName();
    }
}