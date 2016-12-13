package ch.heigvd.gamification.dto;

public class CredentialsDTO {
    private String applicationName;
    private String password;

    public CredentialsDTO() {}

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
