package repo;
import domain.Topic;
import java.sql.*;
import java.util.*;
public class TopicRepo {

    public List<Topic> findBySubstring(String s) throws Exception {
        try (Connection c = Db.getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT * FROM Topic WHERE name LIKE ? ORDER BY name")) {
            ps.setString(1, "%" + s + "%");
            try( ResultSet rs = ps.executeQuery()) {
                List<Topic> out = new ArrayList<>();
                while (rs.next()) {
                    out.add(new Topic(rs.getInt(1),rs.getString(2)));
                }
                return out;
            }
        }
    }

    public List<Topic> findSubscribedTopics(int userId) throws Exception {
        String sql = """
    SELECT t.id, t.name
    FROM Topic t
    JOIN UserTopic ut ON ut.topic_id = t.id
    WHERE ut.user_id = ?
    ORDER BY t.name
    """;

        try (Connection c = Db.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Topic> out = new ArrayList<>();
                while (rs.next()) out.add(new Topic(rs.getInt(1),rs.getString(2)));
                return out;
            }
        }
    }

    public void subscribe(int userId, int topicId) throws Exception {
        try (Connection c = Db.getConnection();
        PreparedStatement ps = c.prepareStatement("INSERT OR IGNORE INTO UserTopic VALUES (?, ?)")){
            ps.setInt(1,userId);
            ps.setInt(2,topicId);
            ps.executeUpdate();
        }
    }

}
