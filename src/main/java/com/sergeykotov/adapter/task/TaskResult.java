package com.sergeykotov.adapter.task;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TaskResult {
    private String startTime;
    private String endTime;
    private TaskDto task;
    private boolean succeeded;
    private String note;

    public TaskResult() {
    }

    @JsonProperty("start_time")
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @JsonProperty("end_time")
    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public TaskDto getTask() {
        return task;
    }

    public void setTask(TaskDto task) {
        this.task = task;
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    public void setSucceeded(boolean succeeded) {
        this.succeeded = succeeded;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}