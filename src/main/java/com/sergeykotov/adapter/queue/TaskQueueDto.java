package com.sergeykotov.adapter.queue;

import com.sergeykotov.adapter.task.TaskDto;

import java.util.List;

public class TaskQueueDto {
    private int capacity;
    private int size;
    private List<TaskDto> tasks;

    public TaskQueueDto() {
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<TaskDto> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDto> tasks) {
        this.tasks = tasks;
    }
}