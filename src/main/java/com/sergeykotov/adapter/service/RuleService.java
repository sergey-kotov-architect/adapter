package com.sergeykotov.adapter.service;

import com.sergeykotov.adapter.dao.RuleDao;
import com.sergeykotov.adapter.domain.Rule;
import com.sergeykotov.adapter.exception.DatabaseException;
import com.sergeykotov.adapter.exception.NotFoundException;
import com.sergeykotov.adapter.system.System;
import com.sergeykotov.adapter.system.system1.System1;
import com.sergeykotov.adapter.system.system2.System2;
import com.sergeykotov.adapter.task.TaskResult;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class RuleService {
    private static final Logger log = Logger.getLogger(RuleService.class);
    private final RuleDao ruleDao;
    private final List<System> systems;

    @Autowired
    public RuleService(RuleDao ruleDao, System1 system1, System2 system2) {
        this.ruleDao = ruleDao;
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
        List<Rule> rules;
        try {
            rules = ruleDao.extract();
        } catch (SQLException e) {
            log.error("failed to extract rules from the database", e);
            throw new DatabaseException();
        }
        log.info(rules.size() + " rules have been extracted from the database");
        for (System system : systems) {
            List<Rule> systemRules = system.getRules();
            String systemName = system.getName();
            for (Rule systemRule : systemRules) {
                String json = systemRule.getSystemRuleMap().get(systemName);
                rules.stream()
                        .filter(r -> r.equals(systemRule))
                        .map(Rule::getSystemRuleMap)
                        .forEach(m -> m.put(systemName, json));
            }
        }
        log.info("rules details have been extracted from the systems");
        return rules;
    }

    public Rule getRule(long id) throws NotFoundException {
        log.info("extracting rule by ID " + id + "...");
        Rule rule;
        try {
            rule = ruleDao.extractById(id);
        } catch (SQLException e) {
            log.error("failed to extract rule from the database by ID " + id, e);
            throw new DatabaseException();
        }
        log.info("rule has been extracted from the database by ID " + id);
        for (System system : systems) {
            Rule systemRule = system.getRule(id);
            String systemName = system.getName();
            String json = systemRule.getSystemRuleMap().get(systemName);
            rule.getSystemRuleMap().put(system.getName(), json);
        }
        log.info("details have been extracted from the systems for the rule ID " + id);
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
        rule.setCreationTime(LocalDateTime.now());
        boolean created;
        try {
            created = ruleDao.create(rule);
        } catch (SQLException e) {
            String note = "failed to save rule " + rule + " to the database";
            log.error(note, e);
            taskResult.setSucceeded(false);
            taskResult.setNote(note + ": " + e.getMessage());
            return taskResult;
        }
        if (!created) {
            String note = "failed to save rule " + rule + " to the database";
            log.error(note);
            taskResult.setSucceeded(false);
            taskResult.setNote(note);
            return taskResult;
        }
        log.info("rule " + rule + " has been created");
        taskResult.setSucceeded(true);
        return taskResult;
    }

    public TaskResult update(long id, Rule rule) {
        TaskResult taskResult = new TaskResult();
        log.info("updating rule by ID " + id + "...");
        List<System> affectedSystems = new ArrayList<>(systems.size());
        Rule existingRule = null;
        try {
            existingRule = getRule(id);
        } catch (Exception e) {
            log.error("failed to extract from the database and the systems previous state of rule ID " + id);
        }
        for (System system : systems) {
            boolean updated = system.updateRule(rule);
            if (updated) {
                affectedSystems.add(system);
                continue;
            }
            List<String> notes = new ArrayList<>();
            String note = "failed to update rule ID " + id + " on system " + system;
            log.error(note);
            notes.add(note);
            for (System affectedSystem : affectedSystems) {
                boolean restored = existingRule != null && affectedSystem.updateRule(existingRule);
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
        rule.setLastUpdateTime(LocalDateTime.now());
        boolean updated;
        try {
            updated = ruleDao.update(rule);
        } catch (SQLException e) {
            String note = "failed to update rule in the database by ID " + id;
            log.error(note, e);
            taskResult.setSucceeded(false);
            taskResult.setNote(note + ": " + e.getMessage());
            return taskResult;
        }
        if (!updated) {
            String note = "failed to update rule in the database by ID " + id;
            log.error(note);
            taskResult.setSucceeded(false);
            taskResult.setNote(note);
        }
        log.info("rule has been updated by ID " + id);
        taskResult.setSucceeded(true);
        return taskResult;
    }

    public TaskResult delete(long id) {
        TaskResult taskResult = new TaskResult();
        log.info("deleting rule ID " + id + "...");
        Rule rule;
        try {
            rule = getRule(id);
        } catch (Exception e) {
            String note = "failed to extract rule ID " + id + " from the database and systems";
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
        boolean deleted;
        try {
            deleted = ruleDao.deleteById(id);
        } catch (SQLException e) {
            String note = "failed to delete rule from the database by ID " + id;
            log.error(note, e);
            taskResult.setSucceeded(false);
            taskResult.setNote(note + ": " + e.getMessage());
            return taskResult;
        }
        if (!deleted) {
            String note = "failed to delete rule from the database by ID " + id;
            log.error(note);
            taskResult.setSucceeded(false);
            taskResult.setNote(note);
        }
        log.info("rule has been deleted by ID " + id);
        taskResult.setSucceeded(true);
        return taskResult;
    }
}