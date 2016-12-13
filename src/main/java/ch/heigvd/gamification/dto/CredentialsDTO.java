package ch.heigvd.gamification.dto;

public class CredentialsDTO {
    private String name;
    private String password;

    public CredentialsDTO() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
