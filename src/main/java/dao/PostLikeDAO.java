package dao;

import model.Database;
import java.sql.*;

public class PostLikeDAO {

    public boolean toggleLike(int postId, int userId) {
        if (isLiked(postId, userId)) {
            return removeLike(postId, userId);
        } else {
            return addLike(postId, userId);
        }
    }

    public boolean addLike(int postId, int userId) {
        String sql = "INSERT INTO post_likes (post_id, user_id) VALUES (?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, postId);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean removeLike(int postId, int userId) {
        String sql = "DELETE FROM post_likes WHERE post_id = ? AND user_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, postId);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isLiked(int postId, int userId) {
        String sql = "SELECT 1 FROM post_likes WHERE post_id = ? AND user_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, postId);
            stmt.setInt(2, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getLikeCount(int postId) {
        String sql = "SELECT COUNT(*) FROM post_likes WHERE post_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, postId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
