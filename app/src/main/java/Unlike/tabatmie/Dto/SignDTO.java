package Unlike.tabatmie.Dto;

public class SignDTO {

    int code;
    String message;

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

    @Override
    public String toString() {
        return "SignDTO{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
