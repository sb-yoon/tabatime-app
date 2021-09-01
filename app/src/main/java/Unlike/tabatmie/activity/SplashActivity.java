package Unlike.tabatmie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import Unlike.tabatmie.R;
import Unlike.tabatmie.connect.CallRetrofit;
import Unlike.tabatmie.util.Applications;
import Unlike.tabatmie.util.Preference;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class SplashActivity extends AppCompatActivity {

    private String TAG = this.getClass().toString();

    @BindView(R.id.btn_login)
    RelativeLayout btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        ButterKnife.bind(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!Applications.preference.getValue(Preference.TOKEN, "").isEmpty()) {
                    CallRetrofit callProfile = new CallRetrofit();
                    callProfile.callProfile();
                }
            }
        }).start();

        Intent intent = getIntent();
        if (intent != null && intent.getAction() != null && intent.getAction().equals("login")) {
            btn_login.setVisibility(View.VISIBLE);
            setIntent(new Intent().setAction(null));
        } else {
            btn_login.setVisibility(View.GONE);
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    goMain();
                }
            };
            Timer timer = new Timer();
            long splashDelay = 2000;
            timer.schedule(task, splashDelay);
        }
    }

    @OnClick({R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(SplashActivity.this)) {
                    UserApiClient.getInstance().loginWithKakaoTalk(SplashActivity.this, kLoginCallback);
                } else {
                    UserApiClient.getInstance().loginWithKakaoAccount(SplashActivity.this, kLoginCallback);
                }
                break;
        }
    }

    Function2<OAuthToken, Throwable, Unit> kLoginCallback = new Function2<OAuthToken, Throwable, Unit>() {
        @Override
        public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
            if (throwable != null) {
                Toast.makeText(SplashActivity.this, "Login Fail", Toast.LENGTH_SHORT).show();
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
                                    CallRetrofit callLogin = new CallRetrofit();
                                    callLogin.setHashMap(map);
                                    callLogin.callLogin(false);
                                    Applications.preference.put(Preference.EMAIL, email);
                                }
                                finish();
                            } else {
                                Toast.makeText(SplashActivity.this, "Login Fail", Toast.LENGTH_SHORT).show();
                            }
                            return null;
                        }
                    });

                } else {
                    Toast.makeText(SplashActivity.this, "Login Fail", Toast.LENGTH_SHORT).show();
                }
            }
            return null;
        }
    };

    public void goMain() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}