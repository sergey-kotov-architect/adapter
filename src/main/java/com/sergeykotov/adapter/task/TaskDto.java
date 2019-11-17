package com.sergeykotov.adapter.task;

public class TaskDto {
    private TaskType type;
    private String name;

    public TaskDto() {
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}