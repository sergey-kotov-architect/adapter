package com.sergeykotov.adapter.task;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TaskDto {
    private long id;
    private String submissionTime;
    private String name;
    private String note;

    public TaskDto() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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