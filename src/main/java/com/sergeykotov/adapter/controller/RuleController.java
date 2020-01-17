package com.sergeykotov.adapter.controller;

import com.sergeykotov.adapter.domain.Rule;
import com.sergeykotov.adapter.exception.NotFoundException;
import com.sergeykotov.adapter.queue.TaskProducer;
import com.sergeykotov.adapter.service.AuthorizationService;
import com.sergeykotov.adapter.service.RuleService;
import com.sergeykotov.adapter.task.TaskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rule")
public class RuleController {
    private final TaskProducer taskProducer;
    private final AuthorizationService authorizationService;
    private final RuleService ruleService;

    @Autowired
    public RuleController(TaskProducer taskProducer, AuthorizationService authorizationService, RuleService ruleService) {
        this.taskProducer = taskProducer;
        this.authorizationService = authorizationService;
        this.ruleService = ruleService;
    }

    @GetMapping
    public List<Rule> getRules(@RequestHeader String authorization) {
        authorizationService.authorize(authorization);
        return ruleService.getRules();
    }

    @GetMapping("/find")
    public Rule getRule(@RequestHeader String authorization, @RequestBody @Valid Rule rule) throws NotFoundException {
        authorizationService.authorize(authorization);
        return ruleService.getRule(rule);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public TaskDto createRule(@RequestHeader String authorization, @RequestBody @Valid Rule rule) {
        authorizationService.authorize(authorization);
        return taskProducer.submitCreateRuleTask(rule);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public TaskDto updateRule(@RequestHeader String authorization, @RequestBody @Valid Rule rule) {
        authorizationService.authorize(authorization);
        return taskProducer.submitUpdateRuleTask(rule);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public TaskDto deleteRule(@RequestHeader String authorization, @RequestBody @Valid Rule rule) {
        authorizationService.authorize(authorization);
        return taskProducer.submitDeleteRuleTask(rule);
    }
}