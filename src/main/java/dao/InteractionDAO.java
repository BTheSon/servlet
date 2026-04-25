package dao;

import model.Database;
import model.Interaction;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InteractionDAO {

    public boolean addInteraction(int actorId, Integer targetUserId, Integer targetPostId, String type) {
        String sql = "INSERT INTO interactions (actor_id, target_user_id, target_post_id, type) VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, actorId);
            if (targetUserId != null) {
                stmt.setInt(2, targetUserId);
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            if (targetPostId != null) {
                stmt.setInt(3, targetPostId);
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            stmt.setString(4, type);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Interaction> getInteractionsByActor(int actorId) {
        List<Interaction> interactions = new ArrayList<>();
        String sql = "SELECT * FROM interactions WHERE actor_id = ? ORDER BY created_at DESC";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, actorId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    interactions.add(new Interaction(
                        rs.getInt("id"),
                        rs.getInt("actor_id"),
                        (Integer) rs.getObject("target_user_id"),
                        (Integer) rs.getObject("target_post_id"),
                        rs.getString("type"),
                        rs.getTimestamp("created_at")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return interactions;
    }
}
