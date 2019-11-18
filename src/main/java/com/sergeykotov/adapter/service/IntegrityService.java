package com.sergeykotov.adapter.service;

import com.sergeykotov.adapter.domain.IntegrityDto;
import com.sergeykotov.adapter.domain.Rule;
import com.sergeykotov.adapter.system.System;
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
        String time = LocalDateTime.now().toString();

        log.info("verifying integrity...");
        List<System> systems = ruleService.getSystems();
        List<Rule> rules = ruleService.getRules();
        List<String> messages = verify(systems, rules);
        log.info("integrity has been verified");

        IntegrityDto integrityDto = new IntegrityDto();
        integrityDto.setConsistent(messages.isEmpty());
        integrityDto.setTime(time);
        integrityDto.setMessages(messages);
        return integrityDto;
    }

    public void restore() {
        log.info("restoring integrity...");
        //TODO: implement integrity restoration
        try {
            Thread.sleep(15_000L); //latency simulation
        } catch (InterruptedException e) {
            log.error("integrity restoration has been interrupted");
            return;
        }
        log.info("integrity has been restored");
    }

    private List<String> verify(List<System> systems, List<Rule> rules) {
        List<String> messages = new ArrayList<>(systems.size() * rules.size());
        for (System system : systems) {
            for (Rule rule : rules) {
                if (!rule.getSystemRuleMap().containsKey(system.getName())) {
                    String message = "system " + system + " does not contain rule " + rule;
                    messages.add(message);
                }
            }
        }
        return messages;
    }
}