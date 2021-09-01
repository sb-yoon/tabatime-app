package Unlike.tabatmie.connect;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Unlike.tabatmie.Dto.BasicDTO;
import Unlike.tabatmie.Dto.JsonArrayDTO;
import Unlike.tabatmie.Dto.JsonObjectDTO;
import Unlike.tabatmie.Dto.RecordDTO;
import Unlike.tabatmie.util.Applications;
import Unlike.tabatmie.util.CommonUtil;
import Unlike.tabatmie.util.Preference;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallRetrofit {

    private String TAG = this.getClass().toString();

    private HashMap<String, Object> map;

    public void setHashMap(HashMap<String, Object> map) {
        this.map = map;
    }

    public String getToken() {
        return Applications.preference.getValue(Preference.TOKEN, "");
    }

    public void callLogin(boolean callRoutine) {
        Applications.preference.put(Preference.GO_ROUTINE, callRoutine);
        Call<JsonObjectDTO> call = RetrofitClient.getApiService().login(map);
        call.enqueue(new Callback<JsonObjectDTO>() {
            @Override
            public void onResponse(Call<JsonObjectDTO> call, Response<JsonObjectDTO> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "fail error code : " + response.code());
                    return;
                }
                JsonObjectDTO login = response.body();
                int code = login.getCode();
                if (code == 1000) {
                    callSign();
                } else if (code == 200) {
                    try {
                        String rst = login.getData().toString();
                        JSONObject jo = new JSONObject(rst);
                        Log.e(TAG, "token" + jo.getString("token"));
                        Applications.preference.put(Preference.TOKEN, "Bearer " + jo.getString("token"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (callRoutine) {
                        callRoutine(false);
                    } else {
                        Applications.refreshActivity();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObjectDTO> call, Throwable t) {
                Log.e(TAG, "onFailure : " + t.getMessage());
            }
        });
    }

    public void callSign() {
        Call<BasicDTO> call = RetrofitClient.getApiService().sign(map);
        call.enqueue(new Callback<BasicDTO>() {
            @Override
            public void onResponse(Call<BasicDTO> call, Response<BasicDTO> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "fail error code : " + response.code());
                    return;
                }
                BasicDTO sign = response.body();
                int code = sign.getCode();
                if (code == 200) {
                    callLogin(Applications.preference.getValue(Preference.GO_ROUTINE, false));
                } else {

                }
            }

            @Override
            public void onFailure(Call<BasicDTO> call, Throwable t) {
                Log.e(TAG, "onFailure : " + t.getMessage());
            }
        });
    }

    public void callProfile() {
        Call<JsonObjectDTO> call = RetrofitClient.getApiService().profile(getToken());
        call.enqueue(new Callback<JsonObjectDTO>() {
            @Override
            public void onResponse(Call<JsonObjectDTO> call, Response<JsonObjectDTO> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "fail error code : " + response.code());
                    return;
                }
                JsonObjectDTO profile = response.body();
                int code = profile.getCode();
                if (code == 200) {
                } else if (code == 401) {
                    CommonUtil.logout();
                }
            }

            @Override
            public void onFailure(Call<JsonObjectDTO> call, Throwable t) {
                Log.e(TAG, "onFailure : " + t.getMessage());
            }
        });
    }

    public void callRoutine(boolean tryAgain) {
        HashMap<String, Object> routineMap = new HashMap<>();
        routineMap.put("exerciseTime", Applications.preference.getValue(Preference.EXERCISE, CommonUtil.D_EXERCISE));
        routineMap.put("restTime", Applications.preference.getValue(Preference.REST, CommonUtil.D_REST));
        routineMap.put("setCnt", Applications.preference.getValue(Preference.SET, CommonUtil.D_SET));
        routineMap.put("roundCnt", Applications.preference.getValue(Preference.ROUND, CommonUtil.D_ROUND));
        routineMap.put("roundTime", Applications.preference.getValue(Preference.ROUND_RESET, CommonUtil.D_ROUND_RESET));
        routineMap.put("totalTime", Applications.preference.getValue(Preference.EXERCISE_TIME, CommonUtil.D_EXERCISE_TIME));
        Call<BasicDTO> call = RetrofitClient.getApiService().routine(getToken(), routineMap);
        call.enqueue(new Callback<BasicDTO>() {
            @Override
            public void onResponse(Call<BasicDTO> call, Response<BasicDTO> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "fail error code : " + response.code());
                    if (tryAgain) {
                        Applications.tryAgain();
                    }
                    return;
                }
                BasicDTO routine = response.body();
                int code = routine.getCode();
                if (code == 200) {
                    Log.e(TAG, "routine : " + Applications.preference.getValue(Preference.GO_ROUTINE, false));
                    if (Applications.preference.getValue(Preference.GO_ROUTINE, false)) {
                        Applications.preference.put(Preference.GO_ROUTINE, false);
                        Applications.refreshActivity();
                    }
                    Applications.preference.put(Preference.SAVE_SUCCESS, true);
                } else if (code == 401) {
                    CommonUtil.logout();
                    Applications.refreshActivity();
                } else {
                    if (tryAgain) {
                        Applications.tryAgain();
                    }
                }
            }

            @Override
            public void onFailure(Call<BasicDTO> call, Throwable t) {
                Log.e(TAG, "onFailure : " + t.getMessage());
                if (tryAgain) {
                    Applications.tryAgain();
                }
            }
        });
    }

    public void callRecord() {
        Call<JsonArrayDTO> call = RetrofitClient.getApiService().record(getToken());
        call.enqueue(new Callback<JsonArrayDTO>() {
            @Override
            public void onResponse(Call<JsonArrayDTO> call, Response<JsonArrayDTO> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "fail error code : " + response.code());
                    return;
                }
                JsonArrayDTO record = response.body();
                int code = record.getCode();
                if (code == 200) {
                    try {
                        String rst = record.getData().toString();
                        Applications.setRecord(setRecordList(rst), false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (code == 401) {
                    CommonUtil.logout();
                    Applications.refreshActivity();
                }
            }

            @Override
            public void onFailure(Call<JsonArrayDTO> call, Throwable t) {
                Log.e(TAG, "onFailure : " + t.getMessage());
            }
        });
    }

    public ArrayList<RecordDTO> setRecordList(String rst) throws Exception {
        JSONArray ja = new JSONArray(rst);
        ArrayList<RecordDTO> recordList = new ArrayList<>();
        if (ja.length() > 0) {
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                String date = jo.getString("regDate").substring(0, 10);
                date = date.replace("-", ".");
                recordList.add(
                        new RecordDTO(jo.getInt("id"),
                                jo.getInt("totalTime"),
                                jo.getInt("exerciseTime"),
                                jo.getInt("restTime"),
                                jo.getInt("setCnt"),
                                jo.getInt("roundCnt"),
                                jo.getInt("roundTime"),
                                date));
            }
        }
        return recordList;
    }

    public void callRecordDelete(List<Integer> deleteList, ArrayList<RecordDTO> recordList) {
        HashMap<String, Object> deleteMap = new HashMap<>();
        deleteMap.put("routineIds", deleteList);
        Call<JsonObjectDTO> call = RetrofitClient.getApiService().recordDelete(getToken(), deleteMap);
        call.enqueue(new Callback<JsonObjectDTO>() {
            @Override
            public void onResponse(Call<JsonObjectDTO> call, Response<JsonObjectDTO> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "fail error code : " + response.code());
                    return;
                }
                JsonObjectDTO recordDelete = response.body();
                int code = recordDelete.getCode();
                if (code == 200) {
                    try {
                        Applications.setRecord(updateRecordList(deleteList, recordList), true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (code == 401) {
                    CommonUtil.logout();
                    Applications.refreshActivity();
                }
            }

            @Override
            public void onFailure(Call<JsonObjectDTO> call, Throwable t) {
                Log.e(TAG, "onFailure : " + t.getMessage());
            }
        });
    }

    public ArrayList<RecordDTO> updateRecordList(List<Integer> deleteList, ArrayList<RecordDTO> recordList) throws Exception {
        List<Integer> idxList = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
            int idx = recordList.get(i).getId();
            for (int j = 0; j < deleteList.size(); j++) {
                int deleteIdx = deleteList.get(j);
                if (idx == deleteIdx) {
                    idxList.add(i);
                }
            }
        }
        if (!idxList.isEmpty()) {
            for (int i = 0; i < idxList.size(); i++) {
                recordList.remove(i);
            }
        }
        return recordList;
    }

    public void callStat() {
        Call<JsonObjectDTO> call = RetrofitClient.getApiService().statistics(getToken());
        call.enqueue(new Callback<JsonObjectDTO>() {
            @Override
            public void onResponse(Call<JsonObjectDTO> call, Response<JsonObjectDTO> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "fail error code : " + response.code());
                    return;
                }
                JsonObjectDTO record = response.body();
                int code = record.getCode();
                if (code == 200) {
                    try {
                        String rst = record.getData().toString();
                        JSONObject jo = new JSONObject(rst);
                        if (jo.getInt("totalRoutineCnt") > 0) {
                            Applications.setStatistics((int) jo.getInt("rankPer"), jo.getInt("totalRoutineCnt"), jo.getInt("totalRoutineTime"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (code == 401) {
                    CommonUtil.logout();
                    Applications.refreshActivity();
                }
            }

            @Override
            public void onFailure(Call<JsonObjectDTO> call, Throwable t) {
                Log.e(TAG, "onFailure : " + t.getMessage());
            }
        });
    }
}
