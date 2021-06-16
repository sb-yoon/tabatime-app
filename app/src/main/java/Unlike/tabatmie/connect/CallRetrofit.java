package Unlike.tabatmie.connect;

import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

import Unlike.tabatmie.Dto.BasicDTO;
import Unlike.tabatmie.Dto.JsonDTO;
import Unlike.tabatmie.util.Applications;
import Unlike.tabatmie.util.CommonUtil;
import Unlike.tabatmie.util.Preference;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallRetrofit {

    private HashMap<String, Object> map;
    private boolean callRoutine = false;

    public void setHashMap(HashMap<String, Object> map) {
        this.map = map;
    }

    public String getToken() {
        return Applications.preference.getValue(Preference.TOKEN, "");
    }

    public void callLogin(boolean callRoutine) {
        this.callRoutine = callRoutine;
        Call<JsonDTO> call = RetrofitClient.getApiService().login(map);
        call.enqueue(new Callback<JsonDTO>() {
            @Override
            public void onResponse(Call<JsonDTO> call, Response<JsonDTO> response) {
                if (!response.isSuccessful()) {
                    Log.e("fail", "error code : " + response.code());
                    return;
                }
                JsonDTO login = response.body();
                int code = login.getCode();
                if (code == 1000) {
                    callSign();
                } else if (code == 200) {
                    try {
                        String rst = login.getData().toString();
                        JSONObject jo = new JSONObject(rst);
                        Log.e("token", jo.getString("token"));
                        Applications.preference.put(Preference.TOKEN, "Bearer " + jo.getString("token"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (callRoutine) {
                        callRoutine();
                    } else {
                        Applications.refreshActivity();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonDTO> call, Throwable t) {
                Log.e("onFailure", t.getMessage());
            }
        });
    }

    public void callSign() {
        Call<BasicDTO> call = RetrofitClient.getApiService().sign(map);
        call.enqueue(new Callback<BasicDTO>() {
            @Override
            public void onResponse(Call<BasicDTO> call, Response<BasicDTO> response) {
                if (!response.isSuccessful()) {
                    Log.e("fail", "error code : " + response.code());
                    return;
                }
                BasicDTO sign = response.body();
                int code = sign.getCode();
                if (code == 200) {
                    callLogin(false);
                } else {

                }
            }

            @Override
            public void onFailure(Call<BasicDTO> call, Throwable t) {
                Log.e("onFailure", t.getMessage());
            }
        });
    }

    public void callProfile() {
        Call<JsonDTO> call = RetrofitClient.getApiService().profile(getToken());
        call.enqueue(new Callback<JsonDTO>() {
            @Override
            public void onResponse(Call<JsonDTO> call, Response<JsonDTO> response) {
                if (!response.isSuccessful()) {
                    Log.e("fail", "error code : " + response.code());
                    return;
                }
                JsonDTO profile = response.body();
                int code = profile.getCode();
                if (code == 200) {

                } else if (code == 401) {
                    CommonUtil.logout();
                }
            }

            @Override
            public void onFailure(Call<JsonDTO> call, Throwable t) {
                Log.e("onFailure", t.getMessage());
            }
        });
    }

    public void callRoutine() {
        HashMap<String, Object> routineMap = new HashMap<>();
        routineMap.put("exerciseTime", Applications.preference.getValue(Preference.EXERCISE, Preference.D_EXERCISE));
        routineMap.put("restTime", Applications.preference.getValue(Preference.REST, Preference.D_REST));
        routineMap.put("setCnt", Applications.preference.getValue(Preference.SET, Preference.D_SET));
        routineMap.put("roundCnt", Applications.preference.getValue(Preference.ROUND, Preference.D_ROUND));
        routineMap.put("roundTime", Applications.preference.getValue(Preference.ROUND_RESET, Preference.D_ROUND_RESET));
        routineMap.put("totalTime", Applications.preference.getValue(Preference.EXERCISE_TIME, Preference.D_EXERCISE_TIME));
        Call<BasicDTO> call = RetrofitClient.getApiService().routine(getToken(), routineMap);
        call.enqueue(new Callback<BasicDTO>() {
            @Override
            public void onResponse(Call<BasicDTO> call, Response<BasicDTO> response) {
                if (!response.isSuccessful()) {
                    Log.e("fail", "error code : " + response.code());
                    return;
                }
                BasicDTO sign = response.body();
                int code = sign.getCode();
                if (code == 200) {

                } else {

                }
            }

            @Override
            public void onFailure(Call<BasicDTO> call, Throwable t) {
                Log.e("onFailure", t.getMessage());
            }
        });
    }
}
