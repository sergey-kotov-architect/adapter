package com.sergeykotov.adapter.controller;

import com.sergeykotov.adapter.domain.IntegrityDto;
import com.sergeykotov.adapter.domain.Rule;
import com.sergeykotov.adapter.queue.TaskQueue;
import com.sergeykotov.adapter.queue.TaskQueueDto;
import com.sergeykotov.adapter.service.IntegrityService;
import com.sergeykotov.adapter.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class Controller {
    private final TaskQueue taskQueue;
    private final RuleService ruleService;
    private final IntegrityService integrityService;

    @Autowired
    public Controller(TaskQueue taskQueue, RuleService ruleService,IntegrityService integrityService) {
        this.taskQueue = taskQueue;
        this.ruleService = ruleService;
        this.integrityService = integrityService;
    }

    @GetMapping("/rule")
    public List<Rule> getRules() {
        return ruleService.getRules();
    }

    @GetMapping("/rule/{id}")
    public Rule getRule(@PathVariable long id) {
        return ruleService.getRule(id);
    }

    @PostMapping("/rule")
    public void createRule(@RequestBody @Valid Rule rule) {
        taskQueue.submitCreateRuleTask(ruleService, rule);
    }

    @PutMapping("/rule")
    public void updateRule(@RequestBody @Valid Rule rule) {
        taskQueue.submitUpdateRuleTask(ruleService, rule);
    }

    @DeleteMapping("/rule")
    public void deleteRule(@RequestBody @Valid Rule rule) {
        taskQueue.submitDeleteRuleTask(ruleService, rule);
    }

    @GetMapping("/integrity")
    public IntegrityDto verifyIntegrity() {
        return integrityService.verify();
    }

    @PostMapping("/integrity")
    public void restoreIntegrity() {
        taskQueue.submitRestoreIntegrityTask(integrityService);
    }

    @GetMapping("/queue")
    public TaskQueueDto getTaskQueueDto() {
        return taskQueue.getTaskQueueDto();
    }
}