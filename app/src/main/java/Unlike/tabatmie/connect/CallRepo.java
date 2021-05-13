package Unlike.tabatmie.connect;

import Unlike.tabatmie.Dto.DefaultDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CallRepo {

    @GET("users/{user}")
    Call<DefaultDto> listRepos(@Path("user") String user);

}
