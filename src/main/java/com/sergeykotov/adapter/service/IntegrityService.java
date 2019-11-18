package com.sergeykotov.adapter.service;

import com.sergeykotov.adapter.domain.IntegrityDto;
import com.sergeykotov.adapter.domain.Rule;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        log.info("verifying integrity...");
        String time = LocalDateTime.now().toString();
        List<Rule> rules = ruleService.getRules();
        boolean consistent = verify(rules);
        log.info("integrity has been verified");

        String success = "the business rules are consistent among the systems";
        String fail = "integrity is violated";
        String message = consistent ? success : fail;

        IntegrityDto integrityDto = new IntegrityDto();
        integrityDto.setConsistent(consistent);
        integrityDto.setTime(time);
        integrityDto.setMessage(message);
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

    private boolean verify(List<Rule> rules) {
        return true;
    }
}