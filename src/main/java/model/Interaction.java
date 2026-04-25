package model;

import java.sql.Timestamp;

public class Interaction {

    private int id;
    private int actorId;
    private Integer targetUserId; // Dùng Integer để có thể null
    private Integer targetPostId; // Dùng Integer để có thể null
    private String type;
    private Timestamp createdAt;

    public Interaction(int id, int actorId, Integer targetUserId, Integer targetPostId, String type, Timestamp createdAt) {
        this.id = id;
        this.actorId = actorId;
        this.targetUserId = targetUserId;
        this.targetPostId = targetPostId;
        this.type = type;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getActorId() {
        return actorId;
    }

    public void setActorId(int actorId) {
        this.actorId = actorId;
    }

    public Integer getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(Integer targetUserId) {
        this.targetUserId = targetUserId;
    }

    public Integer getTargetPostId() {
        return targetPostId;
    }

    public void setTargetPostId(Integer targetPostId) {
        this.targetPostId = targetPostId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
