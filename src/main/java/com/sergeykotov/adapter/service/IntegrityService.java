package com.sergeykotov.adapter.service;

import com.sergeykotov.adapter.domain.IntegrityDto;
import com.sergeykotov.adapter.domain.Rule;
import com.sergeykotov.adapter.system.System;
import com.sergeykotov.adapter.task.TaskResult;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class IntegrityService {
    private static final Logger log = Logger.getLogger(IntegrityService.class);
    private final RuleService ruleService;

    @Autowired
    public IntegrityService(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    public IntegrityDto verify() {
        String startTime = LocalDateTime.now().toString();

        log.info("verifying integrity...");
        List<System> systems = ruleService.getSystems();
        List<Rule> rules = ruleService.getRules();
        List<String> notes = verify(systems, rules);
        boolean verified = notes.isEmpty();
        if (verified) {
            log.info("integrity has been verified");
        } else {
            log.error("integrity has been violated: " + notes);
        }

        IntegrityDto integrityDto = new IntegrityDto();
        integrityDto.setVerified(verified);
        integrityDto.setVerificationStartTime(startTime);
        integrityDto.setNotes(notes);
        return integrityDto;
    }

    public TaskResult restore() {
        TaskResult taskResult = new TaskResult();
        log.info("restoring integrity...");
        //TODO: implement integrity restoration
        try {
            Thread.sleep(15_000L); //latency simulation
        } catch (InterruptedException e) {
            log.error("integrity restoration has been interrupted");
            taskResult.setSucceeded(false);
            return taskResult;
        }
        log.info("integrity has been restored");

        taskResult.setSucceeded(true);
        return taskResult;
    }

    private List<String> verify(List<System> systems, List<Rule> rules) {
        List<String> notes = new ArrayList<>(systems.size() * rules.size());
        for (System system : systems) {
            for (Rule rule : rules) {
                if (!rule.getSystemRuleMap().containsKey(system.getName())) {
                    String note = "system " + system + " does not contain rule " + rule;
                    notes.add(note);
                }
            }
        }
        return notes;
    }
}