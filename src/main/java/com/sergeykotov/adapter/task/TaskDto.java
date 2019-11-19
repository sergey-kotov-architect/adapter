package com.sergeykotov.adapter.task;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TaskDto {
    private String submissionTime;
    private String name;
    private String note;

    public TaskDto() {
    }

    @JsonProperty("submission_time")
    public String getSubmissionTime() {
        return submissionTime;
    }

    public void setSubmissionTime(String submissionTime) {
        this.submissionTime = submissionTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}