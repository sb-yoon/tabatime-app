package Unlike.tabatmie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import java.util.HashMap;

import Unlike.tabatmie.R;
import Unlike.tabatmie.connect.CallRetrofit;
import Unlike.tabatmie.dialog.TabatimeDialog;
import Unlike.tabatmie.util.Applications;
import Unlike.tabatmie.util.CommonUtil;
import Unlike.tabatmie.util.Preference;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class SuccessActivity extends AppCompatActivity {

    private String TAG = this.getClass().toString();

    @BindView(R.id.tv_exercise_record)
    TextView tv_exercise_record;
    @BindView(R.id.tv_exercise_time)
    TextView tv_exercise_time;

    @BindView(R.id.tv_exercise)
    TextView tv_exercise;
    @BindView(R.id.tv_rest)
    TextView tv_rest;
    @BindView(R.id.tv_set)
    TextView tv_set;
    @BindView(R.id.tv_round)
    TextView tv_round;

    @BindView(R.id.btn_success)
    RelativeLayout btn_success;
    @BindView(R.id.tv_success)
    TextView tv_success;
    @BindView(R.id.layer_finish)
    LinearLayout layer_finish;

    private boolean go_login = false, go_save = false;

    private CallRetrofit callLogin;
    private CallRetrofit callRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        ButterKnife.bind(this);

        try {
            Applications.setRefreshActivity(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        init();
    }

    public void init() {
        tv_exercise_record.setText(getResources().getString(R.string.today));
        tv_exercise_time.setText(CommonUtil.getTime(Applications.preference.getValue(Preference.EXERCISE_TIME, 0)));

        tv_exercise.setText(CommonUtil.getTime(Applications.preference.getValue(Preference.EXERCISE, Preference.D_EXERCISE)));
        tv_rest.setText(CommonUtil.getTime(Applications.preference.getValue(Preference.REST, Preference.D_REST)));
        tv_set.setText(Applications.preference.getValue(Preference.SET, Preference.D_SET) + "");
        tv_round.setText(Applications.preference.getValue(Preference.ROUND, Preference.D_ROUND) + "");

        go_login = Applications.preference.getValue(Preference.TOKEN, "").isEmpty();
        if (go_login) {
            tv_success.setText(getResources().getString(R.string.success_login));
            layer_finish.setVisibility(View.VISIBLE);
        } else {
            go_save = true;
            tv_success.setText(getResources().getString(R.string.finish));
            layer_finish.setVisibility(View.GONE);
            callRecord = new CallRetrofit();
            callRecord.callRoutine(false);
        }
    }

    @OnClick({R.id.btn_success, R.id.layer_finish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_success:
                if (go_login) {
                    go_save = true;
                    if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(SuccessActivity.this)) {
                        UserApiClient.getInstance().loginWithKakaoTalk(SuccessActivity.this, kLoginCallback);
                    } else {
                        UserApiClient.getInstance().loginWithKakaoAccount(SuccessActivity.this, kLoginCallback);
                    }
                } else {
                    chkCon();
                }
                break;
            case R.id.layer_finish:
                goMain();
                break;
        }
    }

    public void chkCon() {
        Log.e("chkCon", Applications.preference.getValue(Preference.SAVE_SUCCESS, false) + ":" + go_save);
        if (!Applications.preference.getValue(Preference.SAVE_SUCCESS, false) && go_save) {
            callRecord = new CallRetrofit();
            callRecord.callRoutine(true);
        } else {
            Applications.preference.put(Preference.SAVE_SUCCESS, false);
            goMain();
        }
    }

    public void goMain() {
        Intent intent = new Intent(SuccessActivity.this, MainActivity.class);
        startActivity(intent);
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    Function2<OAuthToken, Throwable, Unit> kLoginCallback = new Function2<OAuthToken, Throwable, Unit>() {
        @Override
        public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
            if (throwable != null) {
                Toast.makeText(SuccessActivity.this, "Login Fail", Toast.LENGTH_SHORT).show();
            } else {
                if (oAuthToken != null) {
                    UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
                        @Override
                        public Unit invoke(User user, Throwable throwable) {
                            if (user != null) {
                                if (!user.getKakaoAccount().getEmail().isEmpty() && user.getId() > 0) {
                                    String email = user.getKakaoAccount().getEmail();
                                    int id = (int) user.getId();
                                    HashMap<String, Object> map = new HashMap<>();
                                    map.put("email", email);
                                    map.put("snsId", id);
                                    callLogin = new CallRetrofit();
                                    callLogin.setHashMap(map);
                                    callLogin.callLogin(true);
                                    Applications.preference.put(Preference.EMAIL, email);
                                }
                            } else {
                                Toast.makeText(SuccessActivity.this, "Login Fail", Toast.LENGTH_SHORT).show();
                            }
                            return null;
                        }
                    });

                } else {
                    Toast.makeText(SuccessActivity.this, "Login Fail", Toast.LENGTH_SHORT).show();
                }
            }
            return null;
        }
    };

    public void refresh() {
        if (Applications.preference.getValue(Preference.TOKEN, "").isEmpty()) {
            go_login = true;
            tv_success.setText(getResources().getString(R.string.success_login));
            layer_finish.setVisibility(View.VISIBLE);
        } else {
            go_login = false;
            tv_success.setText(getResources().getString(R.string.finish));
            layer_finish.setVisibility(View.GONE);
        }
    }

    public void tryAgain() {
        TabatimeDialog dialog = new TabatimeDialog(this);
        dialog.setDialogType(0);
        dialog.setTitle(getResources().getString(R.string.error));
        dialog.setCancelBtnClickListener(getResources().getString(R.string.try_again), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callRecord == null) {
                    callRecord = new CallRetrofit();
                }
                callRecord.callRoutine(true);
                dialog.dismiss();
            }
        });
        dialog.setOkBtnClickListener(getResources().getString(R.string.no_save), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                onBackPressed();
            }
        });
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Applications.preference.put(Preference.SAVE_SUCCESS, false);
    }
}