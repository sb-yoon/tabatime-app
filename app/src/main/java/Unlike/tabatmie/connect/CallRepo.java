package Unlike.tabatmie.connect;

import java.util.HashMap;

import Unlike.tabatmie.Dto.DefaultDto;
import Unlike.tabatmie.Dto.SignDTO;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CallRepo {

    @GET("users/{user}")
    Call<DefaultDto> listRepos(@Path("user") String user);

    @POST("api/v1/auth/signup")
    Call<SignDTO> postOverlapCheck(@FieldMap HashMap<String, Object> param);
}
