package Unlike.tabatmie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

public class SignActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = this.getClass().toString();

    @BindView(R.id.btn_kLogin)
    Button btn_kLogin;
    @BindView(R.id.btn_nLogin)
    Button btn_nLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        Applications.fristInit(this);
        init();

        if (Applications.preference.getValue(Preference.UID, "").equals("")) {
            btn_kLogin.setVisibility(View.VISIBLE);
            btn_nLogin.setVisibility(View.VISIBLE);
        } else {
            btn_kLogin.setVisibility(View.GONE);
            btn_nLogin.setVisibility(View.GONE);
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    goMain();
                }
            };
            Timer timer = new Timer();
            long splashDelay = 0;
            timer.schedule(task, splashDelay);
        }
    }

    public void init() {
        btn_kLogin.setOnClickListener(this);
        btn_nLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_kLogin:
                if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(SignActivity.this)) {
                    UserApiClient.getInstance().loginWithKakaoTalk(SignActivity.this, kLoginCallback);
                } else {
                    UserApiClient.getInstance().loginWithKakaoAccount(SignActivity.this, kLoginCallback);
                }
                break;
            case R.id.btn_nLogin:
                goMain();
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

    public void goMain() {
        Intent intent = new Intent(SignActivity.this, MainActivity.class);
        startActivity(intent);
    }
}