package com.sergeykotov.adapter.service;

import com.sergeykotov.adapter.domain.Rule;
import com.sergeykotov.adapter.exception.NotFoundException;
import com.sergeykotov.adapter.system.System;
import com.sergeykotov.adapter.system.system1.System1;
import com.sergeykotov.adapter.system.system2.System2;
import com.sergeykotov.adapter.task.TaskResult;
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

    public List<System> getSystems() {
        return systems;
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

    public Rule getRule(long id) throws NotFoundException {
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

    public TaskResult create(Rule rule) {
        TaskResult taskResult = new TaskResult();
        log.info("creating rule " + rule + "...");
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
        log.info("rule " + rule + " has been created");
        taskResult.setSucceeded(true);
        return taskResult;
    }

    public TaskResult update(long id, Rule rule) {
        TaskResult taskResult = new TaskResult();
        log.info("updating rule by ID " + id + "...");
        Map<System, Rule> affectedSystems = new HashMap<>(systems.size() * 2);
        for (System system : systems) {
            Rule existingRule = null;
            try {
                existingRule = getRule(id);
            } catch (NotFoundException e) {
                log.error("failed to get previous state of rule ID " + id + " on system " + system);
            }
            boolean updated = system.updateRule(rule);
            if (updated) {
                affectedSystems.put(system, existingRule);
                continue;
            }
            List<String> notes = new ArrayList<>();
            String note = "failed to update rule ID " + id + " on system " + system;
            log.error(note);
            notes.add(note);
            for (Map.Entry<System, Rule> entry : affectedSystems.entrySet()) {
                System affectedSystem = entry.getKey();
                Rule previousRule = entry.getValue();
                boolean restored = affectedSystem.updateRule(previousRule);
                if (!restored) {
                    String format = "integrity has been violated: system %s has rule ID %d in invalid state";
                    String message = String.format(format, affectedSystem, id);
                    log.error(message);
                    notes.add(message);
                }
            }
            taskResult.setSucceeded(false);
            taskResult.setNote(String.join(", ", notes));
            return taskResult;
        }
        log.info("rule has been updated by ID " + id);
        taskResult.setSucceeded(true);
        return taskResult;
    }

    public TaskResult delete(long id) {
        TaskResult taskResult = new TaskResult();
        log.info("deleting rule by ID " + id + "...");
        Rule rule;
        try {
            rule = getRule(id);
        } catch (NotFoundException e) {
            String note = "failed to delete rule by ID " + id + ": rule not found";
            log.error(note);
            taskResult.setSucceeded(false);
            taskResult.setNote(note);
            return taskResult;
        }
        List<System> affectedSystems = new ArrayList<>(systems.size());
        for (System system : systems) {
            boolean deleted = system.deleteRule(rule);
            if (deleted) {
                affectedSystems.add(system);
                continue;
            }
            List<String> notes = new ArrayList<>();
            String note = "failed to delete rule ID " + id + " on system " + system;
            log.error(note);
            notes.add(note);
            for (System affectedSystem : affectedSystems) {
                boolean created = affectedSystem.createRule(rule);
                if (!created) {
                    String format = "integrity has been violated: system %s must contain rule ID %d";
                    String message = String.format(format, affectedSystem, id);
                    log.error(message);
                    notes.add(message);
                }
            }
            taskResult.setSucceeded(false);
            taskResult.setNote(String.join(", ", notes));
            return taskResult;
        }
        log.info("rule has been deleted by ID " + id);
        taskResult.setSucceeded(true);
        return taskResult;
    }
}