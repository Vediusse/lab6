package viancis.lab6.server.commands;

import viancis.lab6.common.commands.CommandType;
import viancis.lab6.common.communication.Request;
import viancis.lab6.common.communication.Response;
import viancis.lab6.common.models.User;
import viancis.lab6.server.collection.Collection;
import viancis.lab6.server.collection.Storage;
import viancis.lab6.server.db.ConnectionBataDase;
import viancis.lab6.server.db.JwtGenerator;
import viancis.lab6.server.db.ManagerDb;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

public class AuthCommand extends AbstractCommand{
    public JwtGenerator jwtGenerator = new JwtGenerator();

    public AuthCommand() {
        super("auth", "получить jwt token", 0, CommandType.WITH_LOGIN);
        this.needForm = true;
    }


    @Override
    public Response execute(Request request, Collection collection, User user) {
        User registrationUser = request.user();
        String token = jwtGenerator.generateToken(registrationUser);
        if (token ==  null){
            return  new Response(false,"В базе данных нет пользователя с таким именем");
        }
        return new Response(token, "auth");
    }



}