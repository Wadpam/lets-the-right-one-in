package se.leiflandia.lroi.auth.model;

import java.util.Date;

public abstract class AbstractCreatedUpdatedEntity {
    private String createdBy;
    private Date createdDate;
    private String updatedBy;
    private Date updatedDate;

    public String getCreatedBy() {
        return createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }
}
