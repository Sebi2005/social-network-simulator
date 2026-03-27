package repo;
import domain.Post;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class PostRepo {

    public List<Post> findAllDesc() throws Exception {
        try(Connection c=Db.getConnection();
            PreparedStatement ps=c.prepareStatement(
                    "SELECT id,user_id,text,ts FROM Post ORDER BY ts DESC");
            ResultSet rs=ps.executeQuery()){
            List<Post> out=new ArrayList<>();
            while(rs.next()){
                out.add(new Post(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        LocalDateTime.parse(rs.getString(4))
                ));
            }
            return out;
        }
    }

    public List<Post> findByUserDesc(int userId) throws Exception {
        try(Connection c=Db.getConnection();
            PreparedStatement ps=c.prepareStatement(
                    "SELECT id,user_id,text,ts FROM Post WHERE user_id=? ORDER BY ts DESC")){
            ps.setInt(1,userId);
            try(ResultSet rs=ps.executeQuery()){
                List<Post> out=new ArrayList<>();
                while(rs.next()){
                    out.add(new Post(rs.getInt(1), rs.getInt(2), rs.getString(3),
                            LocalDateTime.parse(rs.getString(4))));
                }
                return out;
            }
        }
    }
    public void updateText(int postId, String newText) throws Exception {
        try(Connection c=Db.getConnection();
            PreparedStatement ps=c.prepareStatement(
                    "UPDATE Post SET text=? WHERE id=?")){
            ps.setString(1,newText);
            ps.setInt(2,postId);
            ps.executeUpdate();
        }
    }
    public void add(int userId, String text, LocalDateTime ts) throws Exception {
        try(Connection c=Db.getConnection();
            PreparedStatement ps=c.prepareStatement(
                    "INSERT INTO Post(user_id,text,ts) VALUES(?,?,?)")){
            ps.setInt(1,userId);
            ps.setString(2,text);
            ps.setString(3,ts.toString());
            ps.executeUpdate();
        }
    }
}
