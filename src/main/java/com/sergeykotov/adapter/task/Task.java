package com.sergeykotov.adapter.task;

import java.time.LocalDateTime;

public interface Task {
    long getID();

    LocalDateTime getSubmissionTime();

    TaskType getName();

    String getNote();

    default TaskDto getTaskDto() {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(getID());
        taskDto.setSubmissionTime(getSubmissionTime().toString());
        taskDto.setName(getName().toString());
        taskDto.setNote(getNote());
        return taskDto;
    }

    TaskResult execute();
}