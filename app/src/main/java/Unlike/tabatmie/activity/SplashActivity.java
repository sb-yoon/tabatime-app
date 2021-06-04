package Unlike.tabatmie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import java.util.Timer;
import java.util.TimerTask;

import Unlike.tabatmie.R;
import Unlike.tabatmie.util.Applications;
import Unlike.tabatmie.util.Preference;
import butterknife.BindView;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = this.getClass().toString();

    @BindView(R.id.btn_login)
    RelativeLayout btn_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        Applications.fristInit(this);
        init();

        Intent intent = getIntent();
        if (intent != null && intent.getAction() != null && intent.getAction().equals("login")) {
            btn_login.setVisibility(View.VISIBLE);
            setIntent(new Intent().setAction(null));
        } else {
            btn_login.setVisibility(View.GONE);
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    goMain(1);
                }
            };
            Timer timer = new Timer();
            long splashDelay = 2000;
            timer.schedule(task, splashDelay);

        }

    }

    public void init() {
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
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
                //login fail
            } else {
                if (oAuthToken != null) {
                    UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
                        @Override
                        public Unit invoke(User user, Throwable throwable) {
                            if (user != null) {
                                //login success
                                Applications.preference.put(Preference.UID, "success");
                                if (!user.getKakaoAccount().getEmail().isEmpty()) {
                                    Applications.preference.put(Preference.EMAIL, user.getKakaoAccount().getEmail());
                                }
                                goMain(2);
                            } else {
                                //login fail
                            }
                            return null;
                        }
                    });

                } else {
                    //login fail
                }
            }
            return null;
        }
    };

    public void goMain(int type) {
        Log.e(TAG, "goMain / " + type);
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}