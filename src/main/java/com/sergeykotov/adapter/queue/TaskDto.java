package com.sergeykotov.adapter.queue;

public class TaskDto {
    private String name;

    public TaskDto() {
    }

    public TaskDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}