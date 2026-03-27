package repo;
import domain.User;
import java.sql.*;
import java.util.*;
public class UserRepo {

    public List<User> findAll() throws Exception {
        try (Connection c = Db.getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT id,name FROM User");
            ResultSet rs = ps.executeQuery()) {
         List<User> out =  new ArrayList<>();
         while (rs.next()) out.add(new User(rs.getInt(1),rs.getString(2)));
         return out;
        }


    }

    public Optional<User> findByName(String name) throws Exception {
        try (Connection c = Db.getConnection();
        PreparedStatement ps = c.prepareStatement("SELECT id,name FROM User WHERE name = ?")) {
            ps.setString(1,name);
            try(ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(new User(rs.getInt(1), rs.getString(2)));
            }

        }
    }
}

