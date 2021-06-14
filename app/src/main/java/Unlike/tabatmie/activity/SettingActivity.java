package Unlike.tabatmie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import Unlike.tabatmie.BuildConfig;
import Unlike.tabatmie.R;
import Unlike.tabatmie.util.Applications;
import Unlike.tabatmie.util.Preference;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.btn_back)
    RelativeLayout btn_back;

    @BindView(R.id.btn_sound_on)
    RelativeLayout btn_sound_on;
    @BindView(R.id.tv_sound_on)
    TextView tv_sound_on;
    @BindView(R.id.btn_sound_off)
    RelativeLayout btn_sound_off;
    @BindView(R.id.tv_sound_off)
    TextView tv_sound_off;

    @BindView(R.id.switch_pause)
    Switch switch_pause;

    @BindView(R.id.tv_version)
    TextView tv_version;

    @BindView(R.id.btn_terms)
    TextView btn_terms;

    @BindView(R.id.btn_logout)
    TextView btn_logout;
    @BindView(R.id.btn_login)
    RelativeLayout btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ButterKnife.bind(this);

        init();
    }

    public void init() {
        btn_back.setOnClickListener(this);
        btn_sound_on.setOnClickListener(this);
        btn_sound_off.setOnClickListener(this);
        switch_pause.setOnClickListener(this);
        btn_terms.setOnClickListener(this);
        btn_logout.setOnClickListener(this);
        btn_login.setOnClickListener(this);

        if (Applications.preference.getValue(Preference.SOUND, Preference.D_SOUND)) {
            setSound(true);
        } else {
            setSound(false);
        }

        if (Applications.preference.getValue(Preference.EXERCISE_PAUSE, Preference.D_PAUSE)) {
            switch_pause.setChecked(true);
        } else {
            switch_pause.setChecked(false);
        }

        tv_version.setText(BuildConfig.VERSION_NAME);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String uid = Applications.preference.getValue(Preference.UID, "");
        if (uid.isEmpty()) {
            btn_logout.setVisibility(View.GONE);
            btn_login.setVisibility(View.VISIBLE);
        } else {
            btn_logout.setVisibility(View.VISIBLE);
            btn_login.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_sound_on:
                setSound(true);
                break;
            case R.id.btn_sound_off:
                setSound(false);
                break;
            case R.id.switch_pause:
                if (switch_pause.isChecked()) {
                    Applications.preference.put(Preference.EXERCISE_PAUSE, true);
                } else {
                    Applications.preference.put(Preference.EXERCISE_PAUSE, false);
                }
                break;
            case R.id.btn_terms:
                break;
            case R.id.btn_logout:
                btn_logout.setVisibility(View.GONE);
                btn_login.setVisibility(View.VISIBLE);
                Applications.preference.put(Preference.UID, "");
                Applications.preference.put(Preference.EMAIL, "");
                break;
            case R.id.btn_login:
                goLogin();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void setSound(boolean sound) {
        Applications.preference.put(Preference.SOUND, sound);
        if (sound) {
            btn_sound_on.setBackground(ContextCompat.getDrawable(this, R.drawable.layer_main_round_21));
            btn_sound_off.setBackground(null);
            tv_sound_on.setTextColor(ContextCompat.getColor(this, R.color.white));
            tv_sound_off.setTextColor(ContextCompat.getColor(this, R.color.defalut_black));
        } else {
            btn_sound_on.setBackground(null);
            btn_sound_off.setBackground(ContextCompat.getDrawable(this, R.drawable.layer_main_round_21));
            tv_sound_on.setTextColor(ContextCompat.getColor(this, R.color.defalut_black));
            tv_sound_off.setTextColor(ContextCompat.getColor(this, R.color.white));
        }
    }

    public void goLogin() {
        Intent goLogin = new Intent(SettingActivity.this, SplashActivity.class);
        goLogin.setAction("login");
        startActivity(goLogin);
    }
}