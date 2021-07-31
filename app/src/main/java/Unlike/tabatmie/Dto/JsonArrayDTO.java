package Unlike.tabatmie.Dto;

import com.google.gson.JsonArray;

public class JsonArrayDTO {
    int code;
    String message;
    JsonArray data;

    public JsonArrayDTO(int code, String message, JsonArray data) {
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

    public JsonArray getData() {
        return data;
    }
}
