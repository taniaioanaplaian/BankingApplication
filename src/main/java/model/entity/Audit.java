package model.entity;

import java.time.LocalDateTime;

public class Audit {

    private Long auditId;
    private LocalDateTime dateTimeAction;
    private String username;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    private String action;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    public Audit() {
    }

    public Long getAuditId() {
        return auditId;
    }

    public void setAuditId(Long auditId) {
        this.auditId = auditId;
    }

    public LocalDateTime getDateTimeAction() {
        return dateTimeAction;
    }

    public void setDateTimeAction(LocalDateTime dateTimeAction) {
        this.dateTimeAction = dateTimeAction;
    }



    public Audit(LocalDateTime dateTimeAction, String username, String action) {

        this.dateTimeAction = dateTimeAction;
        this.username = username;
        this.action = action;
    }
}
