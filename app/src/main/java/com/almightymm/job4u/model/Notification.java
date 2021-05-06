package com.almightymm.job4u.model;

public class Notification {

    String title,body,userId,jobId;

    public Notification() {
    }

    public Notification(String title, String body, String userId, String jobId) {
        this.title = title;
        this.body = body;
        this.userId = userId;
        this.jobId = jobId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }
}
