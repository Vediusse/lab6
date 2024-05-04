package viancis.lab6.common.models;

import java.io.Serializable;

public class User implements Serializable {
    private String login;
    private String password;

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' + '}';
    }


    public enum Roles {
        Admin("admin"),
        Moderator("moderator"),
        User("user");

        public final String role;

        Roles(String role) {

            this.role = role;
        }
    }

    private Roles role;

    public User(String login, Roles role) {

        this.login = login;
        this.role = role;
    }


    public User(String login, String password, Roles role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }


    public User( String username) {
        this.login = username;
    }




    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }




    public void setLogin(String username) {
        this.login = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
