package com.sergeykotov.adapter.service;

import com.sergeykotov.adapter.domain.IntegrityDto;
import com.sergeykotov.adapter.exception.ExtractionException;
import com.sergeykotov.adapter.system.System;
import com.sergeykotov.adapter.system.system1.System1;
import com.sergeykotov.adapter.system.system2.System2;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class IntegrityService {
    private static final Logger log = Logger.getLogger(IntegrityService.class);
    private final List<System> systems;

    @Autowired
    public IntegrityService(System1 system1, System2 system2) {
        List<System> systems = new ArrayList<>();
        systems.add(system1);
        systems.add(system2);
        this.systems = Collections.unmodifiableList(systems);
    }

    public IntegrityDto verify() {
        log.info("verifying integrity...");
        //TODO: implement integrity verification
        try {
            Thread.sleep(3_000L); //latency simulation
        } catch (InterruptedException e) {
            log.error("integrity verification has been interrupted");
            throw new ExtractionException();
        }
        log.info("integrity has been verified");

        String time = LocalDateTime.now().toString();
        String message = "the business rules are consistent among the systems";
        IntegrityDto integrityDto = new IntegrityDto();
        integrityDto.setConsistent(true);
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
            log.error("integrity extraction has been interrupted");
            return;
        }
        log.info("integrity has been restored");
    }
}