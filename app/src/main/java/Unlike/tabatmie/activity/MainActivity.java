package Unlike.tabatmie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import Unlike.tabatmie.R;
import Unlike.tabatmie.util.Applications;
import Unlike.tabatmie.util.Preference;
import butterknife.BindView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = this.getClass().toString();

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_menu)
    RelativeLayout btn_menu;
    @BindView(R.id.tv_login)
    TextView tv_login;
    @BindView(R.id.tv_mail)
    TextView tv_mail;

    @BindView(R.id.btn_start_exercise)
    RelativeLayout btn_start_exercise;
    @BindView(R.id.tv_exercise_time)
    TextView tv_exercise_time;

    @BindView(R.id.btn_exercise)
    RelativeLayout btn_exercise;
    @BindView(R.id.btn_rest)
    RelativeLayout btn_rest;
    @BindView(R.id.btn_set)
    RelativeLayout btn_set;
    @BindView(R.id.btn_round)
    RelativeLayout btn_round;
    @BindView(R.id.btn_round_reset)
    RelativeLayout btn_round_reset;
    @BindView(R.id.btn_sound)
    RelativeLayout btn_sound;
    @BindView(R.id.tv_exercise)
    TextView tv_exercise;
    @BindView(R.id.tv_rest)
    TextView tv_rest;
    @BindView(R.id.tv_set)
    TextView tv_set;
    @BindView(R.id.tv_round)
    TextView tv_round;
    @BindView(R.id.tv_round_reset)
    TextView tv_round_reset;
    @BindView(R.id.tv_sound)
    TextView tv_sound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Applications.fristInit(this);
        init();
    }

    public void init() {
        setSupportActionBar(toolbar);

        btn_menu.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        btn_start_exercise.setOnClickListener(this);
        btn_exercise.setOnClickListener(this);
        btn_rest.setOnClickListener(this);
        btn_set.setOnClickListener(this);
        btn_round.setOnClickListener(this);
        btn_round_reset.setOnClickListener(this);
        btn_sound.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String email = Applications.preference.getValue(Preference.EMAIL, "");
        String uid = Applications.preference.getValue(Preference.UID, "");
        if (uid.isEmpty()) {
            tv_login.setVisibility(View.VISIBLE);
            tv_mail.setVisibility(View.GONE);
        } else {
            tv_login.setVisibility(View.GONE);
            tv_mail.setVisibility(View.VISIBLE);
            tv_mail.setText(email);
        }

        int exercise = Applications.preference.getValue(Preference.EXERCISE, 30);
        int rest = Applications.preference.getValue(Preference.REST, 10);
        int set = Applications.preference.getValue(Preference.SET, 5);
        int round = Applications.preference.getValue(Preference.ROUND, 1);
        int round_reset = Applications.preference.getValue(Preference.ROUND_RESET, 10);
        int exercise_time = (exercise + rest) * set * round - rest;

        tv_exercise_time.setText(getTime(exercise_time));
        tv_exercise.setText(getTime(exercise));
        tv_rest.setText(getTime(rest));
        tv_set.setText(set + "");
        tv_round.setText(getResources().getString(R.string.round, round + ""));
        tv_round_reset.setText(getTime(round_reset));
        tv_sound.setText(Applications.preference.getValue(Preference.SOUND, "일반음"));
    }

    public String getTime(int cnt) {
        int hour, min, sec;
        sec = cnt;
        min = sec / 60;
        hour = min / 60;
        sec = sec % 60;
        min = min % 60;
        if (hour > 0) {
            return String.format("%02d", hour) + ":" + String.format("%02d", min) + ":" + String.format("%02d", sec);
        } else {
            return String.format("%02d", min) + ":" + String.format("%02d", sec);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_menu:
                drawer.openDrawer(GravityCompat.START);
                break;
            case R.id.tv_login:
                goLogin();
                break;
            case R.id.btn_start_exercise:
                goExercise();
                break;
            case R.id.btn_exercise:
            case R.id.btn_rest:
            case R.id.btn_set:
            case R.id.btn_round:
            case R.id.btn_round_reset:
            case R.id.btn_sound:
                break;
        }
    }

    public void goLogin() {
        Intent goLogin = new Intent(MainActivity.this, SplashActivity.class);
        goLogin.setAction("login");
        startActivity(goLogin);
    }

    public void goExercise() {
        Intent goLogin = new Intent(MainActivity.this, ExerciseActivity.class);
        startActivity(goLogin);
    }
}