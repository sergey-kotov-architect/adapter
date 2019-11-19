package com.sergeykotov.adapter.task;

public interface Task {
    TaskType getType();

    String getName();

    default TaskDto getTaskDto() {
        TaskDto taskDto = new TaskDto();
        taskDto.setType(getType());
        taskDto.setName(getName());
        return taskDto;
    }

    TaskResult execute();
}