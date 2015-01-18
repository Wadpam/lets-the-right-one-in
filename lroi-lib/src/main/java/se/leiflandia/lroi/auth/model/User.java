package se.leiflandia.lroi.auth.model;

public class User {
    private String username;
    private String password;
    private String email;

    private User() { }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getEmail() {
        return email;
    }
}
