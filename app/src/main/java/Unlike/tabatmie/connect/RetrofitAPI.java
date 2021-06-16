package Unlike.tabatmie.connect;

import java.util.HashMap;

import Unlike.tabatmie.Dto.BasicDTO;
import Unlike.tabatmie.Dto.JsonDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RetrofitAPI {

    @POST("/api/v1/auth/login")
    Call<JsonDTO> login(@Body HashMap<String, Object> map);

    @POST("api/v1/auth/signup")
    Call<BasicDTO> sign(@Body HashMap<String, Object> map);

    @GET("/api/v1/user/me")
    Call<JsonDTO> profile(@Header("Authorization") String token);

    @POST("/api/v1/routine")
    Call<BasicDTO> routine(@Header("Authorization") String token, @Body HashMap<String, Object> map);
}
