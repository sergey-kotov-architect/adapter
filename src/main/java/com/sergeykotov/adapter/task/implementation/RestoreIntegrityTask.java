package com.sergeykotov.adapter.task.implementation;

import com.sergeykotov.adapter.domain.Rule;
import com.sergeykotov.adapter.service.IntegrityService;
import com.sergeykotov.adapter.task.Task;
import com.sergeykotov.adapter.task.TaskResult;
import com.sergeykotov.adapter.task.TaskType;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class RestoreIntegrityTask implements Task {
    private final long ID;
    private final IntegrityService integrityService;
    private final List<Rule> rules;
    private final LocalDateTime submissionTime = LocalDateTime.now();

    public RestoreIntegrityTask(long ID, IntegrityService integrityService, List<Rule> rules) {
        this.ID = ID;
        this.integrityService = integrityService;
        this.rules = Collections.unmodifiableList(rules);
    }

    @Override
    public long getID() {
        return ID;
    }

    @Override
    public LocalDateTime getSubmissionTime() {
        return submissionTime;
    }

    @Override
    public TaskType getName() {
        return TaskType.RESTORE_INTEGRITY;
    }

    @Override
    public String getNote() {
        return "";
    }

    @Override
    public TaskResult execute() {
        return integrityService.restore(rules);
    }

    @Override
    public String toString() {
        return getName().toString();
    }
}