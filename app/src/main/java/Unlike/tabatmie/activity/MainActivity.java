package Unlike.tabatmie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import Unlike.tabatmie.R;
import Unlike.tabatmie.util.Applications;
import Unlike.tabatmie.util.CommonUtil;
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
    @BindView(R.id.btn_record)
    LinearLayout btn_record;
    @BindView(R.id.btn_stat)
    LinearLayout btn_stat;
    @BindView(R.id.btn_setting)
    LinearLayout btn_setting;

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
        btn_record.setOnClickListener(this);
        btn_stat.setOnClickListener(this);
        btn_setting.setOnClickListener(this);
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

        int exercise = Applications.preference.getValue(Preference.EXERCISE, Preference.D_EXERCISE);
        int rest = Applications.preference.getValue(Preference.REST, Preference.D_REST);
        int set = Applications.preference.getValue(Preference.SET, Preference.D_SET);
        int round = Applications.preference.getValue(Preference.ROUND, Preference.D_ROUND);
        int round_reset = Applications.preference.getValue(Preference.ROUND_RESET, Preference.D_ROUND_RESET);
        int exercise_time = (exercise + rest) * set * round - rest;
        Applications.preference.put(Preference.EXERCISE_TIME, exercise_time);

        tv_exercise_time.setText(CommonUtil.getTime(exercise_time));
        tv_exercise.setText(CommonUtil.getTime(exercise));
        tv_rest.setText(CommonUtil.getTime(rest));
        tv_set.setText(set + "");
        tv_round.setText(getResources().getString(R.string.round, round + ""));
        tv_round_reset.setText(CommonUtil.getTime(round_reset));
        if (Applications.preference.getValue(Preference.SOUND, Preference.D_SOUND)) {
            tv_sound.setText(getResources().getString(R.string.sound_on));
        } else {
            tv_sound.setText(getResources().getString(R.string.sound_off));
        }

        try {
            Intent intent = getIntent();
            if (intent != null && intent.getAction() != null && intent.getAction().equals("login")) {
                goLogin();
                setIntent(new Intent().setAction(null));
            }
        } catch (Exception e) {
            e.printStackTrace();
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
            case R.id.btn_record:
                goRecord();
                break;
            case R.id.btn_stat:
                goStat();
                break;
            case R.id.btn_setting:
                goSetting();
                break;
            case R.id.btn_start_exercise:
                goExercise();
                break;
            case R.id.btn_exercise:
                goProgress("exercise", "1");
                break;
            case R.id.btn_rest:
                goProgress("rest", "1");
                break;
            case R.id.btn_set:
                goProgress("set", "2");
                break;
            case R.id.btn_round:
                goProgress("round", "2");
                break;
            case R.id.btn_round_reset:
                goProgress("round_reset", "1");
                break;
            case R.id.btn_sound:

                break;
        }
    }

    public void goLogin() {
        Intent goLogin = new Intent(MainActivity.this, SplashActivity.class);
        goLogin.setAction("login");
        startActivity(goLogin);
    }

    public void goRecord() {
        Intent goLogin = new Intent(MainActivity.this, RecordActivity.class);
        startActivity(goLogin);
    }

    public void goStat() {
        Intent goLogin = new Intent(MainActivity.this, StatActivity.class);
        startActivity(goLogin);
    }

    public void goSetting() {
        Intent goLogin = new Intent(MainActivity.this, SettingActivity.class);
        startActivity(goLogin);
    }

    public void goExercise() {
        Intent goExercise = new Intent(MainActivity.this, ExerciseActivity.class);
        startActivity(goExercise);
    }

    public void goProgress(String title, String dialog_type) {
        Intent goProgress = new Intent(MainActivity.this, ProgressActivity.class);
        goProgress.putExtra("title", title);
        goProgress.putExtra("dialog_type", dialog_type);
        startActivity(goProgress);
    }
}