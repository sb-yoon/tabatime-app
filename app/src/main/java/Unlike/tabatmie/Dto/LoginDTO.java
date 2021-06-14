package Unlike.tabatmie.Dto;

import org.json.JSONArray;

public class LoginDTO {

    int code;
    String message;
    JSONArray data;
    String token;

    public LoginDTO(int code, String message, JSONArray data, String token) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.token = token;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JSONArray getData() {
        return data;
    }

    public void setData(JSONArray data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "LoginDTO{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data.toString() +
                ", token='" + token + '\'' +
                '}';
    }
}
