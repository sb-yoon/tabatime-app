package Unlike.tabatmie.connect;

import java.util.HashMap;

import Unlike.tabatmie.Dto.SignDTO;
import Unlike.tabatmie.Dto.LoginDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitAPI {

    @POST("api/v1/auth/signup")
    Call<SignDTO> sign(@Body HashMap<String, Object> map);

    @POST("/api/v1/auth/login")
    Call<LoginDTO> login(@Body HashMap<String, Object> map);
}
