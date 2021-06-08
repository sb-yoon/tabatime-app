package Unlike.tabatmie.activity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import com.dinuscxj.progressbar.CircleProgressBar;

import Unlike.tabatmie.R;
import Unlike.tabatmie.util.Applications;
import Unlike.tabatmie.util.CommonUtil;
import Unlike.tabatmie.util.ExerciseTimerPause;
import Unlike.tabatmie.util.Preference;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ExerciseActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = this.getClass().toString();

    @BindView(R.id.layer_exercise)
    RelativeLayout layer_exercise;

    @BindView(R.id.tv_exercise_title)
    TextView tv_exercise_title;

    @BindView(R.id.layer_pause)
    RelativeLayout layer_pause;
    @BindView(R.id.switch_pause)
    SwitchCompat switch_pause;

    @BindView(R.id.layer_exercise_num)
    LinearLayout layer_exercise_num;
    @BindView(R.id.tv_set_num)
    TextView tv_set_num;
    @BindView(R.id.tv_set_total_num)
    TextView tv_set_total_num;
    @BindView(R.id.tv_round_num)
    TextView tv_round_num;
    @BindView(R.id.tv_round_total_num)
    TextView tv_round_total_num;

    @BindView(R.id.layer_progressbar)
    RelativeLayout layer_progressbar;
    @BindView(R.id.progressbar)
    CircleProgressBar progressbar;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_state)
    TextView tv_state;

    @BindView(R.id.btn_action)
    AppCompatToggleButton btn_action;
    @BindView(R.id.iv_play)
    ImageView iv_play;
    @BindView(R.id.iv_pause)
    ImageView iv_pause;
    @BindView(R.id.btn_sound)
    AppCompatToggleButton btn_sound;
    @BindView(R.id.iv_sound_set)
    ImageView iv_sound_set;
    @BindView(R.id.btn_exercise_out)
    RelativeLayout btn_exercise_out;

    private int ready, exercise, rest, set, round, round_reset;
    private int max_set, max_round;

    private ValueAnimator progressTimer;
    private ExerciseTimerPause timer;
    private boolean isExercise = false;
    private boolean isExerciseStart = false;
    private boolean isActionPause = false;
    private boolean isSwitchPause = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        init();

        setReady();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isSwitchPause && isExerciseStart && !isActionPause) {
            resumeTimer();
        }
    }

    public void init() {
        ButterKnife.bind(this);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        switch_pause.setOnClickListener(this);
        btn_sound.setOnClickListener(this);
        btn_action.setOnClickListener(this);
        btn_exercise_out.setOnClickListener(this);

        ready = Preference.D_READY;
        exercise = Applications.preference.getValue(Preference.EXERCISE, Preference.D_EXERCISE);
        rest = Applications.preference.getValue(Preference.REST, Preference.D_REST);
        set = Applications.preference.getValue(Preference.SET, Preference.D_SET);
        round = Applications.preference.getValue(Preference.ROUND, Preference.D_ROUND);
        round_reset = Applications.preference.getValue(Preference.ROUND_RESET, Preference.D_ROUND_RESET);

        max_set = set;
        max_round = round;

        tv_set_total_num.setText(getResources().getString(R.string.total_num, max_set + ""));
        tv_round_total_num.setText(getResources().getString(R.string.total_num, max_round + ""));


        ViewTreeObserver vto = layer_progressbar.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int w = getWidth();
                RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, w);
                param.addRule(RelativeLayout.CENTER_IN_PARENT,-1);
                layer_progressbar.setLayoutParams(param);
            }
        });

        iv_play.setVisibility(View.GONE);
        iv_pause.setVisibility(View.VISIBLE);

        if (Applications.preference.getValue(Preference.SOUND, Preference.SOUND_ON).equals(Preference.SOUND_ON)) {
            iv_sound_set.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_volume_white));
        } else {
            iv_sound_set.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_mute_white));
        }

        btn_exercise_out.setVisibility(View.GONE);
    }

    public void setReady() {
        layer_exercise.setBackgroundColor(ContextCompat.getColor(this, R.color.color_lightorange));

        tv_exercise_title.setText(getResources().getString(R.string.exercise_ready_title));

        layer_pause.setVisibility(View.VISIBLE);
        layer_exercise_num.setVisibility(View.GONE);

        progressbar.setMax(ready * 1000);
        tv_time.setText(CommonUtil.getTime(ready));
        tv_state.setText(getResources().getString(R.string.exercise_ready));

        iv_play.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play_pink));
        iv_pause.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_pause_pink));

        startTimer(ready);
        isExerciseStart = true;
    }

    public void setExercise() {
        isExercise = true;
        layer_exercise.setBackgroundColor(ContextCompat.getColor(this, R.color.main));

        tv_exercise_title.setText(getResources().getString(R.string.exercising));

        layer_pause.setVisibility(View.GONE);
        layer_exercise_num.setVisibility(View.VISIBLE);
        tv_set_num.setText(set + "");
        tv_round_num.setText(round + "");

        progressbar.setMax(exercise * 1000);
        tv_time.setText(CommonUtil.getTime(exercise));
        tv_state.setText(getResources().getString(R.string.exercising));

        iv_play.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play_blue));
        iv_pause.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_pause_blue));

        startTimer(exercise);
        set--;
    }

    public void setRest() {
        layer_exercise.setBackgroundColor(ContextCompat.getColor(this, R.color.defalut_black));

        tv_exercise_title.setText(getResources().getString(R.string.exercise_rest));

        tv_set_num.setText(set + "");
        tv_round_num.setText(round + "");

        progressbar.setMax(rest * 1000);
        tv_time.setText(CommonUtil.getTime(rest));
        tv_state.setText(getResources().getString(R.string.exercise_rest));

        iv_play.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play_navy));
        iv_pause.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_pause_navy));

        startTimer(rest);
    }

    public void setRoundReset() {
        layer_exercise.setBackgroundColor(ContextCompat.getColor(this, R.color.defalut_black));

        tv_exercise_title.setText(getResources().getString(R.string.title_round_reset));

        tv_set_num.setText(set + "");
        tv_round_num.setText(round + "");

        progressbar.setMax(round_reset * 1000);
        tv_time.setText(CommonUtil.getTime(round_reset));
        tv_state.setText(getResources().getString(R.string.title_round_reset));

        iv_play.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play_navy));
        iv_pause.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_pause_navy));

        startTimer(round_reset);
    }

    public void chkExerciseStep() {
        boolean isReset = false;
        if (set == 0) {
            round--;
            set = max_set;
            isReset = true;
        }
        if (round == 0) {
            goSuccess();
        } else {
            if (isExercise) {
                isExercise = false;
                if (isReset) {
                    setRoundReset();
                } else {
                    setRest();
                }
            } else {
                setExercise();
            }
        }
    }

    public void setProgressTime(int time) {
        int sec = time * 1000;
        progressTimer = ValueAnimator.ofInt(0, sec);
        progressTimer.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                progressbar.setProgress(value);
            }
        });
        progressTimer.setDuration(sec);
        progressTimer.start();
    }

    public void setTime(int time) {
        int sec = time * 1000;
        tv_time.setText(CommonUtil.getTime(time));
        timer = new ExerciseTimerPause(sec, 1000, true) {
            @Override
            public void onTick(long millisUntilFinished) {
                long count = millisUntilFinished / 1000;
                tv_time.setText(CommonUtil.getTime((int) count));
            }

            @Override
            public void onFinish() {
                Log.e(TAG, "onFinish");
                tv_time.setText(CommonUtil.getTime(0));
                chkExerciseStep();
            }
        };
        timer.create();
    }

    public void startTimer(int time) {
        setProgressTime(time);
        setTime(time);
    }

    public void stopTimer() {
        try {
            progressTimer.pause();
            timer.pause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resumeTimer() {
        try {
            progressTimer.resume();
            timer.resume();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.switch_pause:
                if (switch_pause.isChecked()) {
                    isSwitchPause = false;
                } else {
                    isSwitchPause = true;
                }
                break;
            case R.id.btn_sound:
                if (btn_sound.isChecked()) {
                    Applications.preference.put(Preference.SOUND, Preference.SOUND_ON);
                    iv_sound_set.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_volume_white));
                } else {
                    Applications.preference.put(Preference.SOUND, Preference.SOUND_OFF);
                    iv_sound_set.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_mute_white));
                }
                break;
            case R.id.btn_action:
                if (btn_action.isChecked()) {
                    iv_play.setVisibility(View.VISIBLE);
                    iv_pause.setVisibility(View.GONE);
                    btn_exercise_out.setVisibility(View.VISIBLE);
                    stopTimer();
                    isActionPause = true;
                } else {
                    iv_play.setVisibility(View.GONE);
                    iv_pause.setVisibility(View.VISIBLE);
                    btn_exercise_out.setVisibility(View.GONE);
                    resumeTimer();
                    isActionPause = false;
                }
                break;
            case R.id.btn_exercise_out:
                finish();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isSwitchPause) {
            stopTimer();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    public void goSuccess() {
        Intent intent = new Intent(ExerciseActivity.this, SuccessActivity.class);
        startActivity(intent);
        finish();
    }

    public int getWidth() {
        int h = progressbar.getMeasuredWidth();
        if (h == 0) {
            h = 248;
        }
        return h;
    }

}