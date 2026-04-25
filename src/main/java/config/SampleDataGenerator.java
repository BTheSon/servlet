package config;

import model.Database;
import java.sql.*;
import java.util.Random;

public class SampleDataGenerator {

    public static void main(String[] args) {
        generate();
    }

    public static void generate() {
        try (Connection conn = Database.getConnection()) {
            System.out.println("Đang tạo dữ liệu mẫu...");
            Random rand = new Random();

            // 1. Tạo thêm 20 users
            String userSql = "INSERT IGNORE INTO users(username, password, role) VALUES (?, '123', 'user')";
            try (PreparedStatement ps = conn.prepareStatement(userSql)) {
                for (int i = 1; i <= 20; i++) {
                    ps.setString(1, "user_test_" + i);
                    ps.executeUpdate();
                }
            }

            // Lấy danh sách user IDs
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT id FROM users");
            java.util.List<Integer> userIds = new java.util.ArrayList<>();
            while (rs.next()) {
                userIds.add(rs.getInt("id"));
            }

            // 2. Tạo 100 bài viết
            String postSql = "INSERT INTO posts(title, body, user_id, status, created_at) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(postSql)) {
                for (int i = 1; i <= 100; i++) {
                    int authorId = userIds.get(rand.nextInt(userIds.size()));
                    ps.setString(1, "Khám phá chủ đề số " + i);
                    ps.setString(2, "Đây là nội dung chi tiết của bài viết thứ " + i + ". Bài viết này chứa thông tin hữu ích về lập trình và đời sống công nghệ.");
                    ps.setInt(3, authorId);
                    ps.setString(4, rand.nextBoolean() ? "published" : "published"); // Ưu tiên published để test feed
                    
                    // Random thời gian trong 30 ngày qua
                    long now = System.currentTimeMillis();
                    long randomPast = now - (long)rand.nextInt(30) * 24 * 60 * 60 * 1000;
                    ps.setTimestamp(5, new Timestamp(randomPast));
                    
                    ps.executeUpdate();
                }
            }

            // Lấy danh sách post IDs
            rs = st.executeQuery("SELECT id FROM posts");
            java.util.List<Integer> postIds = new java.util.ArrayList<>();
            while (rs.next()) {
                postIds.add(rs.getInt("id"));
            }

            // 3. Tạo Follow ngẫu nhiên
            String followSql = "INSERT IGNORE INTO follows(following_user_id, followed_user_id) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(followSql)) {
                for (int i = 0; i < 50; i++) {
                    int u1 = userIds.get(rand.nextInt(userIds.size()));
                    int u2 = userIds.get(rand.nextInt(userIds.size()));
                    if (u1 != u2) {
                        ps.setInt(1, u1);
                        ps.setInt(2, u2);
                        ps.executeUpdate();
                    }
                }
            }

            // 4. Tạo Like ngẫu nhiên
            String likeSql = "INSERT IGNORE INTO post_likes(post_id, user_id) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(likeSql)) {
                for (int i = 0; i < 200; i++) {
                    int pid = postIds.get(rand.nextInt(postIds.size()));
                    int uid = userIds.get(rand.nextInt(userIds.size()));
                    ps.setInt(1, pid);
                    ps.setInt(2, uid);
                    ps.executeUpdate();
                }
            }

            // 5. Tạo Interaction ngẫu nhiên (để test ranking)
            String interSql = "INSERT INTO interactions(actor_id, target_user_id, target_post_id, type) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(interSql)) {
                for (int i = 0; i < 100; i++) {
                    int u1 = userIds.get(rand.nextInt(userIds.size()));
                    int u2 = userIds.get(rand.nextInt(userIds.size()));
                    int pid = postIds.get(rand.nextInt(postIds.size()));
                    ps.setInt(1, u1);
                    ps.setInt(2, u2);
                    ps.setInt(3, pid);
                    ps.setString(4, "like_post");
                    ps.executeUpdate();
                }
            }

            System.out.println("Thành công! Đã tạo 20 users, 100 bài viết và hàng trăm tương tác mẫu.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
