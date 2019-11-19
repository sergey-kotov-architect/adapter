package com.sergeykotov.adapter.queue;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sergeykotov.adapter.task.TaskDto;

import java.util.List;

public class TaskQueueDto {
    private int capacity;
    private int size;
    private TaskDto executingTask;
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

    @JsonProperty("executing_task")
    public TaskDto getExecutingTask() {
        return executingTask;
    }

    public void setExecutingTask(TaskDto executingTask) {
        this.executingTask = executingTask;
    }

    public List<TaskDto> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDto> tasks) {
        this.tasks = tasks;
    }
}