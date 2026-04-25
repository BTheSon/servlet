package model;

import java.sql.Timestamp;

public class PostDTO extends Post {
    private String authorName;
    private int likeCount;
    private boolean likedByMe;
    private double rankScore;

    public PostDTO(int id, String title, String body, int userId, String status, Timestamp createdAt, 
                   String authorName, int likeCount, boolean likedByMe, double rankScore) {
        super(id, title, body, userId, status, createdAt);
        this.authorName = authorName;
        this.likeCount = likeCount;
        this.likedByMe = likedByMe;
        this.rankScore = rankScore;
    }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public int getLikeCount() { return likeCount; }
    public void setLikeCount(int likeCount) { this.likeCount = likeCount; }

    public boolean isLikedByMe() { return likedByMe; }
    public void setLikedByMe(boolean likedByMe) { this.likedByMe = likedByMe; }

    public double getRankScore() { return rankScore; }
    public void setRankScore(double rankScore) { this.rankScore = rankScore; }
}
