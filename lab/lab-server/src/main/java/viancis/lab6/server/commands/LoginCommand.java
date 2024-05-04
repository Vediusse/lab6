package viancis.lab6.server.commands;

import viancis.lab6.common.commands.CommandType;
import viancis.lab6.common.communication.Request;
import viancis.lab6.common.communication.Response;
import viancis.lab6.common.models.User;
import viancis.lab6.server.collection.Collection;
import viancis.lab6.server.db.ConnectionBataDase;
import viancis.lab6.server.db.RequestsDb;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

public class LoginCommand extends AbstractCommand{

    public LoginCommand() {
        super("login", "зарегистрироваться в крутой базе данных", 0, CommandType.WITH_LOGIN);
        this.needForm = true;
    }


    @Override
    public Response execute(Request request, Collection collectionMap, User user) {
        try(Connection connection = new ConnectionBataDase().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(RequestsDb.ADD_USER.getQuery())){
            preparedStatement.setString(1, request.user().getLogin());
            preparedStatement.setString(2, this.hashPassword(request.user().getPassword()));
            preparedStatement.execute();
            return new Response(true,"Всё кипяток");
        } catch (SQLException e) {
            return new Response(false,e.toString());
        }

    }


    public  String hashPassword(String password) {
        try {
            // Создаем объект MessageDigest с алгоритмом SHA-224
            MessageDigest digest = MessageDigest.getInstance("SHA-224");
            byte[] hashedBytes = digest.digest(password.getBytes());
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
