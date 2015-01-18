package se.leiflandia.lroi.auth.model;

public class ResetPassword {
    private final String email;

    public ResetPassword(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
