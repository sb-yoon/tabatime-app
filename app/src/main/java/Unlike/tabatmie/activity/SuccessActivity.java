package Unlike.tabatmie.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import Unlike.tabatmie.R;
import Unlike.tabatmie.util.Applications;
import Unlike.tabatmie.util.CommonUtil;
import Unlike.tabatmie.util.Preference;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SuccessActivity extends AppCompatActivity implements View.OnClickListener {

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

    private boolean go_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        init();
    }

    public void init() {
        ButterKnife.bind(this);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        btn_success.setOnClickListener(this);
        layer_finish.setOnClickListener(this);

        tv_exercise_record.setText(getResources().getString(R.string.today));
        tv_exercise_time.setText(CommonUtil.getTime(Applications.preference.getValue(Preference.EXERCISE_TIME, 0)));

        tv_exercise.setText(CommonUtil.getTime(Applications.preference.getValue(Preference.EXERCISE, Preference.D_EXERCISE)));
        tv_rest.setText(CommonUtil.getTime(Applications.preference.getValue(Preference.REST, Preference.D_REST)));
        tv_set.setText(Applications.preference.getValue(Preference.SET, Preference.D_SET) + "");
        tv_round.setText(Applications.preference.getValue(Preference.ROUND, Preference.ROUND) + "");

        go_login = Applications.preference.getValue(Preference.UID, "").isEmpty();
        if (go_login) {
            tv_success.setText(getResources().getString(R.string.success_login));
            layer_finish.setVisibility(View.VISIBLE);
        } else {
            tv_success.setText(getResources().getString(R.string.finish));
            layer_finish.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_success:
                if (go_login) {
                    goLogin();
                } else {
                    goMain();
                }
                break;
            case R.id.layer_finish:
                goMain();
                break;
        }
    }

    public void goMain() {
        Intent intent = new Intent(SuccessActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void goLogin() {
        Intent goLogin = new Intent(SuccessActivity.this, MainActivity.class);
        goLogin.setAction("login");
        startActivity(goLogin);
        finish();
    }
}