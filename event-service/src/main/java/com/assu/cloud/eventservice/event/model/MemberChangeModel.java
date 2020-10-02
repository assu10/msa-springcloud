package com.assu.cloud.eventservice.event.model;

/**
 * 발행될 메시지를 표현하는 POJO
 */
public class MemberChangeModel {
    private String type;
    private String action;
    private String userId;
    private String correlationId;

    public MemberChangeModel(String type, String action, String userId, String correlationId) {
        // super();
        this.type = type;
        this.action = action;
        this.userId = userId;
        this.correlationId = correlationId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }
}
