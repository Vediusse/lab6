package viancis.lab6.server.db;

import io.jsonwebtoken.*;
import viancis.lab6.common.messages.Category;
import viancis.lab6.common.messages.Message;
import viancis.lab6.common.messages.Sender;
import viancis.lab6.common.models.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
public class JwtGenerator {

    private final String SECRET_KEY = "Maksim_is_the_best_practic"; // Замените на ваш секретный ключ

    public String generateToken(User user) {
        try (Connection connection = new ConnectionBataDase().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(RequestsDb.FIND_USER_BY_LOG_AND_PASS.getQuery())) {

            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, this.hashPassword(user.getPassword()));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Map<String, Object> claims = new HashMap<>();
                    claims.put("username", user.getLogin());
                    claims.put("password", user.getPassword());
                    claims.put("auth_date", new Date());

                    return Jwts.builder()
                            .setClaims(claims)
                            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                            .compact();
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            return null;
        }
    }

    public User validateToken(String jwtToken) {
        try {

            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(jwtToken)
                    .getBody();

            String login = (String) claims.get("username");
            String password = (String) claims.get("password");
            password = this.hashPassword(password);
            try (Connection connection = new ConnectionBataDase().getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(RequestsDb.FIND_USER_BY_LOG_AND_PASS.getQuery())) {
                preparedStatement.setString(1, login);
                preparedStatement.setString(2, password);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {

                    if (resultSet.next()) {
                        String role = resultSet.getString("role");
                        String username = resultSet.getString("login");
                        return new User(username,  this.getRoleFromString(role));
                    } else {
                        return null;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException |
                 IllegalArgumentException e) {
            return null;
        }
        return null;
    }
    public User.Roles getRoleFromString(String roleString) {
        for (User.Roles role : User.Roles.values()) {
            if (role.role.equals(roleString)) {
                return role;
            }
        }
        return null; // Если значение не найдено
    }

    public  String hashPassword(String password) {
        try {
            // Создаем объект MessageDigest с алгоритмом SHA-224
            MessageDigest digest = MessageDigest.getInstance("SHA-224");
            // Вычисляем хеш пароля
            byte[] hashedBytes = digest.digest(password.getBytes());
            // Преобразуем массив байтов в строку в шестнадцатеричном формате
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }


}
