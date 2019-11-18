package com.sergeykotov.adapter.domain;

import java.util.List;

public class IntegrityDto {
    private boolean consistent;
    private String time;
    private List<String> messages;

    public IntegrityDto() {
    }

    public boolean isConsistent() {
        return consistent;
    }

    public void setConsistent(boolean consistent) {
        this.consistent = consistent;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
}