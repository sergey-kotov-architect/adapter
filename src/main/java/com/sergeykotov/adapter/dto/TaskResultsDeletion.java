package com.sergeykotov.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TaskResultsDeletion {
    private boolean deleted;
    private int count;
    private String dateTime;
    private String note;

    public TaskResultsDeletion() {
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @JsonProperty("date_time")
    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}