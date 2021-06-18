package Unlike.tabatmie.Dto;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JsonArray getData() {
        return data;
    }

    public void setData(JsonArray data) {
        this.data = data;
    }
}
