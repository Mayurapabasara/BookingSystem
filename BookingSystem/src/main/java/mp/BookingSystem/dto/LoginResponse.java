package mp.BookingSystem.dto;

public class LoginResponse {

    //private String token;
    private String accessToken;
    private String refreshToken;

    public LoginResponse( String accessToken, String refreshToken) {
        //this.token = token;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
