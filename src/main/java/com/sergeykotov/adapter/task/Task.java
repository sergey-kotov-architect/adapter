package com.sergeykotov.adapter.task;

import java.time.LocalDateTime;

public interface Task {
    LocalDateTime getSubmissionTime();

    TaskType getName();

    String getNote();

    default TaskDto getTaskDto() {
        TaskDto taskDto = new TaskDto();
        taskDto.setSubmissionTime(getSubmissionTime().toString());
        taskDto.setName(getName());
        taskDto.setNote(getNote());
        return taskDto;
    }

    TaskResult execute();
}