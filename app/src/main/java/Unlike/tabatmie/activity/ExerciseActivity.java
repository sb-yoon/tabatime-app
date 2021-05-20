package Unlike.tabatmie.activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import com.dinuscxj.progressbar.CircleProgressBar;

import Unlike.tabatmie.R;
import Unlike.tabatmie.util.Applications;
import Unlike.tabatmie.util.CommonUtil;
import Unlike.tabatmie.util.Preference;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ExerciseActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = this.getClass().toString();

    @BindView(R.id.tv_exercise_title)
    TextView tv_exercise_title;

    @BindView(R.id.layer_pause)
    RelativeLayout layer_pause;
    @BindView(R.id.switch_pause)
    SwitchCompat switch_pause;

    @BindView(R.id.layer_exercise_num)
    LinearLayout layer_exercise_num;
    @BindView(R.id.tv_set_total_num)
    TextView tv_set_total_num;
    @BindView(R.id.tv_round_num)
    TextView tv_round_num;
    @BindView(R.id.tv_round_total_num)
    TextView tv_round_total_num;

    @BindView(R.id.progressbar)
    CircleProgressBar progressbar;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_state)
    TextView tv_state;

    @BindView(R.id.btn_action)
    RelativeLayout btn_action;
    @BindView(R.id.iv_play)
    ImageView iv_play;
    @BindView(R.id.iv_pause)
    ImageView iv_pause;
    @BindView(R.id.btn_sound)
    RelativeLayout btn_sound;
    @BindView(R.id.iv_sound_set)
    ImageView iv_sound_set;
    @BindView(R.id.btn_exercise_out)
    RelativeLayout btn_exercise_out;

    int exercise, rest, set, round, round_reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        ButterKnife.bind(this);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        init();
        firstStep();
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    public void init() {
        exercise = Applications.preference.getValue(Preference.EXERCISE, Preference.D_EXERCISE);
        rest = Applications.preference.getValue(Preference.REST, Preference.D_REST);
        set = Applications.preference.getValue(Preference.SET, Preference.D_SET);
        round = Applications.preference.getValue(Preference.ROUND, Preference.D_ROUND);
        round_reset = Applications.preference.getValue(Preference.ROUND_RESET, Preference.D_ROUND_RESET);

        switch_pause.setOnClickListener(this);
        btn_sound.setOnClickListener(this);
        btn_action.setOnClickListener(this);
        btn_exercise_out.setOnClickListener(this);

        progressbar.setMax(rest);
        progressbar.setProgress(5);


    }

    public void firstStep() {
        tv_exercise_title.setText(getResources().getString(R.string.exercise_ready_title));

        layer_pause.setVisibility(View.VISIBLE);
        layer_exercise_num.setVisibility(View.GONE);

        tv_time.setText(CommonUtil.getTime(rest));
        tv_state.setText(getResources().getString(R.string.exercise_ready));
        if (Applications.preference.getValue(Preference.SOUND, Preference.D_SOUND).equals("음소거")) {
            iv_sound_set.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_mute_white));
        } else {
            iv_sound_set.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_volume_white));
        }

        iv_play.setVisibility(View.VISIBLE);
        iv_play.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play_pink));
        iv_pause.setVisibility(View.GONE);
        iv_pause.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_pause_pink));

        btn_exercise_out.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.switch_pause:
                break;
            case R.id.btn_sound:
                break;
            case R.id.btn_action:
                break;
            case R.id.btn_exercise_out:
                finish();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}