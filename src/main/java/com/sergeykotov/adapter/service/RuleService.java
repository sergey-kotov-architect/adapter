package com.sergeykotov.adapter.service;

import com.sergeykotov.adapter.domain.Rule;
import com.sergeykotov.adapter.exception.NotFoundException;
import com.sergeykotov.adapter.system.System;
import com.sergeykotov.adapter.system.system1.System1;
import com.sergeykotov.adapter.system.system2.System2;
import com.sergeykotov.adapter.task.TaskResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class RuleService {
    private static final Logger log = LoggerFactory.getLogger(RuleService.class);
    private final List<System> systems;

    @Autowired
    public RuleService(System1 system1, System2 system2) {
        List<System> systems = new ArrayList<>();
        systems.add(system1);
        systems.add(system2);
        this.systems = Collections.unmodifiableList(systems);
    }

    public List<System> getSystems() {
        return systems;
    }

    public List<Rule> getRules() {
        log.info("extracting rules from the systems...");
        List<Rule> rules = new ArrayList<>();
        for (System system : systems) {
            List<Rule> systemRules = system.getRules();
            for (Rule systemRule : systemRules) {
                if (!rules.contains(systemRule)) {
                    rules.add(systemRule);
                } else {
                    String json = systemRule.getSystemRuleMap().get(system.getName());
                    rules.stream()
                            .filter(r -> r.equals(systemRule))
                            .map(Rule::getSystemRuleMap)
                            .forEach(m -> m.put(system.getName(), json));
                }
            }
        }
        log.info("rules have been extracted from the systems");
        return rules;
    }

    public Rule getRule(Rule rule) throws NotFoundException {
        log.info("extracting rule " + rule + " from the systems...");
        for (System system : systems) {
            String json = system.getRule(rule);
            rule.getSystemRuleMap().put(system.getName(), json);
        }
        log.info("rule " + rule + " has been extracted from the systems");
        return rule;
    }

    public TaskResult create(Rule rule) {
        log.info("creating rule " + rule + "...");
        TaskResult taskResult = new TaskResult();
        List<System> affectedSystems = new ArrayList<>(systems.size());
        for (System system : systems) {
            boolean created = system.createRule(rule);
            if (created) {
                affectedSystems.add(system);
                continue;
            }
            List<String> notes = new ArrayList<>();
            String note = "failed to create rule " + rule + " on system " + system;
            log.error(note);
            notes.add(note);
            for (System affectedSystem : affectedSystems) {
                boolean deleted = affectedSystem.deleteRule(rule);
                if (!deleted) {
                    String format = "integrity has been violated: system %s must not contain rule %s";
                    String message = String.format(format, affectedSystem, rule);
                    log.error(message);
                    notes.add(message);
                }
            }
            taskResult.setSucceeded(false);
            taskResult.setNote(String.join(", ", notes));
            return taskResult;
        }
        taskResult.setSucceeded(true);
        log.info("rule " + rule + " has been created");
        return taskResult;
    }

    public TaskResult update(Rule newRule) {
        log.info("updating rule " + newRule + "...");
        TaskResult taskResult = new TaskResult();
        List<System> affectedSystems = new ArrayList<>(systems.size());
        Rule currentRule = null;
        try {
            currentRule = getRule(newRule);
        } catch (Exception e) {
            log.error("failed to extract from the systems current state of rule " + newRule, e);
        }
        for (System system : systems) {
            boolean updated = system.updateRule(newRule);
            if (updated) {
                affectedSystems.add(system);
                continue;
            }
            List<String> notes = new ArrayList<>();
            String note = "failed to update rule " + newRule + " on system " + system;
            log.error(note);
            notes.add(note);
            for (System affectedSystem : affectedSystems) {
                boolean restored = currentRule != null && affectedSystem.updateRule(currentRule);
                if (!restored) {
                    String format = "integrity has been violated: system %s has rule %s in invalid state";
                    String message = String.format(format, affectedSystem, newRule);
                    log.error(message);
                    notes.add(message);
                }
            }
            taskResult.setSucceeded(false);
            taskResult.setNote(String.join(", ", notes));
            return taskResult;
        }
        taskResult.setSucceeded(true);
        log.info("rule " + newRule + " has been updated");
        return taskResult;
    }

    public TaskResult delete(Rule rule) {
        log.info("deleting rule " + rule + "...");
        TaskResult taskResult = new TaskResult();
        List<System> affectedSystems = new ArrayList<>(systems.size());
        for (System system : systems) {
            boolean deleted = system.deleteRule(rule);
            if (deleted) {
                affectedSystems.add(system);
                continue;
            }
            List<String> notes = new ArrayList<>();
            String note = "failed to delete rule " + rule + " on system " + system;
            log.error(note);
            notes.add(note);
            for (System affectedSystem : affectedSystems) {
                boolean created = affectedSystem.createRule(rule);
                if (!created) {
                    String format = "integrity has been violated: system %s must contain rule %s";
                    String message = String.format(format, affectedSystem, rule);
                    log.error(message);
                    notes.add(message);
                }
            }
            taskResult.setSucceeded(false);
            taskResult.setNote(String.join(", ", notes));
            return taskResult;
        }
        taskResult.setSucceeded(true);
        log.info("rule " + rule + " has been deleted");
        return taskResult;
    }
}