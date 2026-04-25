package model;

import java.sql.Timestamp;

public class PostLike {
    private int id;
    private int postId;
    private int userId;
    private Timestamp createdAt;

    public PostLike(int id, int postId, int userId, Timestamp createdAt) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
