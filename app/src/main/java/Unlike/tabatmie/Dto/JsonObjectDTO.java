package Unlike.tabatmie.Dto;

import com.google.gson.JsonObject;

public class JsonObjectDTO {
    int code;
    String message;
    JsonObject data;

    public JsonObjectDTO(int code, String message, JsonObject data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public JsonObject getData() {
        return data;
    }
}
