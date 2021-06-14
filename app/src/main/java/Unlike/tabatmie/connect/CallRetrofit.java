package Unlike.tabatmie.connect;

import android.util.Log;

import java.util.HashMap;

import Unlike.tabatmie.Dto.LoginDTO;
import Unlike.tabatmie.Dto.SignDTO;
import Unlike.tabatmie.util.Applications;
import Unlike.tabatmie.util.Preference;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallRetrofit {
    public void callLogin(String email, int snsId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("email", email);
        Call<LoginDTO> call = RetrofitClient.getApiService().login(map);
        call.enqueue(new Callback<LoginDTO>() {
            @Override
            public void onResponse(Call<LoginDTO> call, Response<LoginDTO> response) {
                if (!response.isSuccessful()) {
                    Log.e("fail", "error code : " + response.code());
                    return;
                }
                Log.e("success", response.body().toString());
                LoginDTO login = response.body();
                int code = login.getCode();
                if (code == 1000) {
                    callSign(email, snsId);
                } else if (code == 200) {
                    Applications.preference.put(Preference.EMAIL, email);
                }
            }

            @Override
            public void onFailure(Call<LoginDTO> call, Throwable t) {
                Log.e("onFailure", t.getMessage());
            }
        });
    }

    public void callSign(String email, int snsId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("email", email);
        map.put("snsId", snsId);
        Call<SignDTO> call = RetrofitClient.getApiService().sign(map);
        call.enqueue(new Callback<SignDTO>() {
            @Override
            public void onResponse(Call<SignDTO> call, Response<SignDTO> response) {
                if (!response.isSuccessful()) {
                    Log.e("fail", "error code : " + response.code());
                    return;
                }
                Log.e("success", response.body().toString());
                SignDTO sign = response.body();
                int code = sign.getCode();
                if (code == 200) {
                    callLogin(email, snsId);
                } else {

                }
            }

            @Override
            public void onFailure(Call<SignDTO> call, Throwable t) {
                Log.e("onFailure", t.getMessage());
            }
        });
    }
}
