package services;

import entities.Like;
import utils.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceLike implements IService<Like> {
    private Connection con;

    public ServiceLike() {
        con = MyDB.getInstance().getConnection();
    }

    @Override
    public void ajouter(Like like) throws SQLException {
        String sql = "INSERT INTO `like` (user_id, publication_id, type) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, like.getUserId());
            pstmt.setInt(2, like.getPublicationId());
            pstmt.setBoolean(3, like.isType());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void modifier(Like like) throws SQLException {
        String sql = "UPDATE `like` SET type=? WHERE user_id=? AND publication_id=?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setBoolean(1, like.isType());
            pstmt.setInt(2, like.getUserId());
            pstmt.setInt(3, like.getPublicationId());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void supprimer(Like like) throws SQLException {
        String sql = "DELETE FROM `like` WHERE user_id=? AND publication_id=?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, like.getUserId());
            pstmt.setInt(2, like.getPublicationId());
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<Like> afficher() throws SQLException {
        List<Like> likes = new ArrayList<>();
        String sql = "SELECT * FROM `like`";
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                likes.add(new Like(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("publication_id"),
                        rs.getBoolean("type")
                ));
            }
        }
        return likes;
    }

    public int countLikes(int publicationId) throws SQLException {
        return countType(true, publicationId);
    }

    public int countDislikes(int publicationId) throws SQLException {
        return countType(false, publicationId);
    }

    private int countType(boolean type, int publicationId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM `like` WHERE type=? AND publication_id=?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setBoolean(1, type);
            pstmt.setInt(2, publicationId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        }
    }

    public void addOrUpdateLike(int userId, int publicationId, boolean type) throws SQLException {
        try {
            // Start transaction
            con.setAutoCommit(false);

            // Check if the like/dislike already exists
            String checkSql = "SELECT id FROM `like` WHERE user_id=? AND publication_id=?";
            PreparedStatement checkStmt = con.prepareStatement(checkSql);
            checkStmt.setInt(1, userId);
            checkStmt.setInt(2, publicationId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // If it exists, update it
                String updateSql = "UPDATE `like` SET type=? WHERE user_id=? AND publication_id=?";
                PreparedStatement updateStmt = con.prepareStatement(updateSql);
                updateStmt.setBoolean(1, type);
                updateStmt.setInt(2, userId);
                updateStmt.setInt(3, publicationId);
                updateStmt.executeUpdate();
            } else {
                // If not, add a new like/dislike
                String insertSql = "INSERT INTO `like` (user_id, publication_id, type) VALUES (?, ?, ?)";
                PreparedStatement insertStmt = con.prepareStatement(insertSql);
                insertStmt.setInt(1, userId);
                insertStmt.setInt(2, publicationId);
                insertStmt.setBoolean(3, type);
                insertStmt.executeUpdate();
            }

            // Commit transaction
            con.commit();
        } catch (SQLException e) {
            // Roll back if exception occurs
            con.rollback();
            throw e;
        } finally {
            // Restore auto-commit mode
            con.setAutoCommit(true);
        }
    }

}
