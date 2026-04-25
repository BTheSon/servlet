package dao;
import model.*;
import java.sql.*;
import java.util.*;

public class PostDAO {
    public List<Post> getAllPosts() {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT * FROM posts ORDER BY created_at DESC";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                posts.add(new Post(rs.getInt("id"), rs.getString("title"),
                                  rs.getString("body"), rs.getInt("user_id"),
                                  rs.getString("status"), rs.getTimestamp("created_at")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }
    
    public boolean createPost(int userId, String title, String body, String status) {
        String sql = "INSERT INTO posts(title, body, user_id, status) VALUES(?,?,?,?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, body);
            stmt.setInt(3, userId);
            stmt.setString(4, status);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Post> getFeedPosts(int userId) {
        List<Post> posts = new ArrayList<>();
        String sql =
                "SELECT * FROM posts " +
                "WHERE " +
                // Bài viết của chính mình: thấy cả draft lẫn published
                "      user_id = ? " +
                "   OR ( " +
                // Bài viết của người mình follow: chỉ thấy bài đã publish
                "        status = 'published' " +
                "        AND user_id IN ( " +
                "            SELECT followed_user_id FROM follows WHERE following_user_id = ? " +
                "        ) " +
                "      ) " +
                "ORDER BY created_at DESC";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    posts.add(new Post(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("body"),
                            rs.getInt("user_id"),
                            rs.getString("status"),
                            rs.getTimestamp("created_at")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    public List<PostDTO> getRankedFeedPosts(int userId, int limit, int offset) {
        List<PostDTO> posts = new ArrayList<>();
        String sql =
                "SELECT p.*, u.username as author_name, " +
                " (SELECT COUNT(*) FROM post_likes pl WHERE pl.post_id = p.id) as like_count, " +
                " (SELECT 1 FROM post_likes pl2 WHERE pl2.post_id = p.id AND pl2.user_id = ?) as is_liked_by_me, " +
                " ( " +
                "   (CASE WHEN p.user_id IN (SELECT followed_user_id FROM follows f WHERE f.following_user_id = ?) THEN 10 ELSE 0 END) + " +
                "   LEAST((SELECT COUNT(*) FROM interactions i WHERE i.actor_id = ? AND i.target_user_id = p.user_id) * 5, 20) + " +
                "   (SELECT COUNT(*) FROM post_likes pl3 WHERE pl3.post_id = p.id) * 2 - " +
                "   DATEDIFF(CURRENT_TIMESTAMP, p.created_at) " +
                " ) as rank_score " +
                "FROM posts p " +
                "JOIN users u ON p.user_id = u.id " +
                "WHERE p.status = 'published' OR p.user_id = ? " +
                "ORDER BY rank_score DESC, p.created_at DESC " +
                "LIMIT ? OFFSET ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            stmt.setInt(3, userId);
            stmt.setInt(4, userId);
            stmt.setInt(5, limit);
            stmt.setInt(6, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    posts.add(new PostDTO(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("body"),
                            rs.getInt("user_id"),
                            rs.getString("status"),
                            rs.getTimestamp("created_at"),
                            rs.getString("author_name"),
                            rs.getInt("like_count"),
                            rs.getInt("is_liked_by_me") == 1,
                            rs.getDouble("rank_score")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }
}