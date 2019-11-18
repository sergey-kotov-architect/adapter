package com.sergeykotov.adapter.service;

import com.sergeykotov.adapter.domain.Rule;
import com.sergeykotov.adapter.system.System;
import com.sergeykotov.adapter.system.system1.System1;
import com.sergeykotov.adapter.system.system2.System2;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RuleService {
    private static final Logger log = Logger.getLogger(RuleService.class);
    private final List<System> systems;

    @Autowired
    public RuleService(System1 system1, System2 system2) {
        List<System> systems = new ArrayList<>();
        systems.add(system1);
        systems.add(system2);
        this.systems = Collections.unmodifiableList(systems);
    }

    public List<Rule> getRules() {
        log.info("extracting rules...");
        List<Rule> rules = new ArrayList<>();
        for (System system : systems) {
            List<Rule> systemRules = system.getRules();
            if (rules.isEmpty()) {
                rules = systemRules;
            }
            String systemName = system.getName();
            for (Rule systemRule : systemRules) {
                String json = systemRule.getSystemRuleMap().get(systemName);
                rules.stream()
                        .filter(r -> r.equals(systemRule))
                        .map(Rule::getSystemRuleMap)
                        .forEach(m -> m.put(systemName, json));
            }
        }
        log.info(rules.size() + " rules have been extracted");
        return rules;
    }

    public Rule getRule(long id) {
        log.info("extracting rule by ID " + id + "...");
        Rule rule = null;
        for (System system : systems) {
            Rule systemRule = system.getRule(id);
            if (rule == null) {
                rule = systemRule;
            }
            String systemName = system.getName();
            String json = systemRule.getSystemRuleMap().get(systemName);
            rule.getSystemRuleMap().put(system.getName(), json);
        }
        log.info("rule has been extracted by ID " + id);
        return rule;
    }

    public void create(Rule rule) {
        log.info("creating rule " + rule + "...");
        List<System> affectedSystems = new ArrayList<>(systems.size());
        for (System system : systems) {
            boolean created = system.createRule(rule);
            if (created) {
                affectedSystems.add(system);
                continue;
            }
            log.error("failed to create rule " + rule + " on system " + system);
            for (System affectedSystem : affectedSystems) {
                boolean deleted = affectedSystem.deleteRule(rule);
                if (!deleted) {
                    String message = "integrity has been violated: system %s must not contain rule %s";
                    log.error(String.format(message, affectedSystem, rule));
                }
            }
            return;
        }
        log.info("rule " + rule + " has been created");
    }

    public void update(Rule rule) {
        log.info("updating rule " + rule + "...");
        Map<System, Rule> affectedSystems = new HashMap<>(systems.size() * 2);
        for (System system : systems) {
            boolean updated = system.updateRule(rule);
            if (updated) {
                Rule previousRule = getRule(rule.getId());
                affectedSystems.put(system, previousRule);
                continue;
            }
            log.error("failed to update rule " + rule + " on system " + system);
            for (Map.Entry<System, Rule> entry : affectedSystems.entrySet()) {
                System affectedSystem = entry.getKey();
                Rule previousRule = entry.getValue();
                boolean restored = affectedSystem.updateRule(previousRule);
                if (!restored) {
                    String message = "integrity has been violated: system %s has rule %s in invalid state";
                    log.error(String.format(message, affectedSystem, rule));
                }
            }
            return;
        }
        log.info("rule " + rule + " has been updated");
    }

    public void delete(Rule rule) {
        log.info("deleting rule " + rule + "...");
        List<System> affectedSystems = new ArrayList<>(systems.size());
        for (System system : systems) {
            boolean deleted = system.deleteRule(rule);
            if (deleted) {
                affectedSystems.add(system);
                continue;
            }
            log.error("failed to delete rule " + rule + " on system " + system);
            for (System affectedSystem : affectedSystems) {
                boolean created = affectedSystem.createRule(rule);
                if (!created) {
                    String message = "integrity has been violated: system %s must contain rule %s";
                    log.error(String.format(message, affectedSystem, rule));
                }
            }
            return;
        }
        log.info("rule " + rule + " has been deleted");
    }
}