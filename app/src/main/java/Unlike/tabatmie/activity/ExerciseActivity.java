package Unlike.tabatmie.activity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.core.content.ContextCompat;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import Unlike.tabatmie.R;
import Unlike.tabatmie.util.Applications;
import Unlike.tabatmie.util.CommonUtil;
import Unlike.tabatmie.util.ExerciseTimerPause;
import Unlike.tabatmie.util.Preference;
import Unlike.tabatmie.util.SoundPoolPlayer;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExerciseActivity extends AppCompatActivity {

    private String TAG = this.getClass().toString();

    @BindView(R.id.layer_exercise)
    LinearLayout layer_exercise;

    @BindView(R.id.tv_exercise_title)
    TextView tv_exercise_title;

    @BindView(R.id.layer_pause)
    RelativeLayout layer_pause;
    @BindView(R.id.ready_pause)
    Switch ready_pause;
    @BindView(R.id.round_pause)
    Switch round_pause;

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
    @BindView(R.id.btn_skip)
    RelativeLayout btn_skip;
    @BindView(R.id.layer_exercise_time)
    RelativeLayout layer_exercise_time;
    @BindView(R.id.tv_exercise_time)
    TextView tv_exercise_time;
    private SoundPoolPlayer mPlayer;

    private int exercise_time, ready, exercise, rest, set, round, round_reset;
    private int max_set, max_round;

    private ValueAnimator progressTimer;
    private ExerciseTimerPause timer;
    private ExerciseTimerPause exercise_timer;
    private boolean isExercise = false;
    private boolean isLastRest = false;
    private boolean isStart = false;
    private boolean isActionPause = false;
    private boolean isSwitchPause = true;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        ButterKnife.bind(this);

        init();

        setReady();
    }

    public void init() {

        try {
            mAdView = findViewById(R.id.adView);
            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                    AdRequest adRequest = new AdRequest.Builder().build();
                    mAdView.loadAd(adRequest);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        isSwitchPause = Applications.preference.getValue(Preference.EXERCISE_PAUSE, CommonUtil.D_PAUSE);
        ready_pause.setChecked(isSwitchPause);

        exercise_time = Applications.preference.getValue(Preference.EXERCISE_TIME, CommonUtil.D_EXERCISE_TIME);
        ready = CommonUtil.D_READY;
        exercise = Applications.preference.getValue(Preference.EXERCISE, CommonUtil.D_EXERCISE);
        rest = Applications.preference.getValue(Preference.REST, CommonUtil.D_REST);
        set = 1;
        round = 1;
        round_reset = Applications.preference.getValue(Preference.ROUND_RESET, CommonUtil.D_ROUND_RESET);

        max_set = Applications.preference.getValue(Preference.SET, CommonUtil.D_SET);
        max_round = Applications.preference.getValue(Preference.ROUND, CommonUtil.D_ROUND);

        tv_set_total_num.setText(getResources().getString(R.string.total_num, String.valueOf(max_set)));
        tv_round_total_num.setText(getResources().getString(R.string.total_num, String.valueOf(max_round)));


        ViewTreeObserver vto = layer_progressbar.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int w = getWidth();
                RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, w);
                param.addRule(RelativeLayout.CENTER_IN_PARENT, -1);
                layer_progressbar.setLayoutParams(param);
            }
        });

        iv_play.setVisibility(View.GONE);
        iv_pause.setVisibility(View.VISIBLE);

        if (Applications.preference.getValue(Preference.SOUND, CommonUtil.D_SOUND)) {
            iv_sound_set.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_volume_white));
        } else {
            iv_sound_set.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_mute_white));
        }

        btn_exercise_out.setVisibility(View.GONE);
        layer_exercise_time.setVisibility(View.INVISIBLE);
    }

    public int getWidth() {
        int h = progressbar.getMeasuredWidth();
        if (h == 0) {
            h = 248;
        }
        return h;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isSwitchPause && isStart && !isActionPause) {
            resumeTimer();
            if (mPlayer != null) {
                mPlayer.play();
            }
        }
    }

    public void setReady() {
        layer_exercise.setBackgroundColor(ContextCompat.getColor(this, R.color.color_lightorange));

        tv_exercise_title.setText(getResources().getString(R.string.exercise_ready_title));

        layer_pause.setVisibility(View.VISIBLE);
        ready_pause.setVisibility(View.VISIBLE);
        round_pause.setVisibility(View.GONE);
        layer_exercise_num.setVisibility(View.GONE);

        progressbar.setMax(ready * 1000);
        tv_time.setText(CommonUtil.getTime(ready));
        tv_state.setText(getResources().getString(R.string.exercise_ready));

        iv_play.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play_pink));
        iv_pause.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_pause_pink));
        btn_skip.setVisibility(View.VISIBLE);

        startTimer(ready, false);
        isStart = true;
    }

    public void setExercise() {
        Log.e(TAG, "setExercise");
        if (set == 1 && exercise_timer != null) {
            exercise_timer.resume();
        }

        isExercise = true;
        layer_exercise.setBackgroundColor(ContextCompat.getColor(this, R.color.main));

        tv_exercise_title.setText(getResources().getString(R.string.exercising));

        layer_pause.setVisibility(View.GONE);
        layer_exercise_num.setVisibility(View.VISIBLE);

        layer_pause.setVisibility(View.GONE);
        layer_exercise_num.setVisibility(View.VISIBLE);
        tv_set_num.setText(String.valueOf(set));
        tv_round_num.setText(String.valueOf(round));

        progressbar.setMax(exercise * 1000);
        tv_time.setText(CommonUtil.getTime(exercise));
        tv_state.setText(getResources().getString(R.string.exercising));

        iv_play.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play_blue));
        iv_pause.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_pause_blue));
        btn_skip.setVisibility(View.INVISIBLE);
        layer_exercise_time.setVisibility(View.VISIBLE);

        set++;
        startTimer(exercise, true);
    }

    public void setRest() {
        Log.e(TAG, "setRest");
        layer_exercise.setBackgroundColor(ContextCompat.getColor(this, R.color.defalut_black));

        tv_exercise_title.setText(getResources().getString(R.string.exercise_rest));

        progressbar.setMax(rest * 1000);
        tv_time.setText(CommonUtil.getTime(rest));
        tv_state.setText(getResources().getString(R.string.exercise_rest));

        iv_play.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play_navy));
        iv_pause.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_pause_navy));

        startTimer(rest, false);
    }

    public void setRoundReset() {
        if (exercise_timer != null) {
            exercise_timer.pause();
        }
        layer_exercise.setBackgroundColor(ContextCompat.getColor(this, R.color.pink));

        tv_exercise_title.setText(getResources().getString(R.string.title_round_reset));

        if (isSwitchPause) {
            round_pause.setChecked(true);
        } else {
            round_pause.setChecked(false);
        }
        layer_pause.setVisibility(View.VISIBLE);
        ready_pause.setVisibility(View.GONE);
        round_pause.setVisibility(View.VISIBLE);
        layer_exercise_num.setVisibility(View.GONE);

        progressbar.setMax(round_reset * 1000);
        tv_time.setText(CommonUtil.getTime(round_reset));
        tv_state.setText(getResources().getString(R.string.title_round_reset));

        iv_play.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play_round_pink));
        iv_pause.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_pause_roundpink));
        btn_skip.setVisibility(View.VISIBLE);
        layer_exercise_time.setVisibility(View.INVISIBLE);

        startTimer(round_reset, false);
    }

    public void chkExerciseStep() {
        boolean isReset = false;
        if (max_round == 1 || round == max_round) {
            if (set == (max_set + 1)) {
                round++;
                set = 1;
                isReset = true;
            }
        } else {
            if (set == (max_set + 1)) {
                isLastRest = true;
                isReset = false;
            }
        }

        if (round == (max_round + 1)) {
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
                if (isLastRest) {
                    isLastRest = false;
                    round++;
                    set = 1;
                    setRoundReset();
                } else {
                    setExercise();
                }
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

    public void startTimer(int time, boolean start) {
        setProgressTime(time);
        setTime(time);
        if (start && exercise_timer == null) {
            setExerciseTime(exercise_time);
        }
    }

    public void setTime(int time) {
        int sec = time * 1000;
        tv_time.setText(CommonUtil.getTime(time));
        timer = new ExerciseTimerPause(sec, 1000, true) {
            @Override
            public void onTick(long millisUntilFinished) {
                long count = millisUntilFinished / 1000;
                tv_time.setText(CommonUtil.getTime((int) count));
                try {
                    if (Applications.preference.getValue(Preference.SOUND, CommonUtil.D_SOUND) && count == 3) {
                        if (mPlayer == null) {
                            mPlayer = SoundPoolPlayer.create(ExerciseActivity.this, R.raw.countdown);
                            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mediaPlayer) {
                                    mPlayer = null;
                                }
                            });
                            mPlayer.play();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    public void setExerciseTime(int time) {
        int sec = time * 1000;
        tv_exercise_time.setText(CommonUtil.getTime(time));
        exercise_timer = new ExerciseTimerPause(sec, 1000, true) {
            @Override
            public void onTick(long millisUntilFinished) {
                long count = millisUntilFinished / 1000;
                tv_exercise_time.setText(getResources().getString(R.string.remain, CommonUtil.getTime((int) count)));
            }

            @Override
            public void onFinish() {
                Log.e(TAG, "onFinish");
                tv_exercise_time.setText(CommonUtil.getTime(0));
            }
        };
        exercise_timer.create();
    }

    public void pauseTimer() {
        try {
            progressTimer.pause();
            timer.pause();
            exercise_timer.pause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resumeTimer() {
        try {
            progressTimer.resume();
            timer.resume();
            exercise_timer.resume();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopTimer() {
        try {
            progressTimer.cancel();
            timer.cancel();
            exercise_timer.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.ready_pause, R.id.round_pause, R.id.btn_sound, R.id.btn_action, R.id.btn_exercise_out, R.id.btn_skip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ready_pause:
                isSwitchPause = ready_pause.isChecked();
                break;
            case R.id.round_pause:
                isSwitchPause = round_pause.isChecked();
                break;
            case R.id.btn_sound:
                if (btn_sound.isChecked()) {
                    Applications.preference.put(Preference.SOUND, false);
                    iv_sound_set.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_mute_white));
                    if (mPlayer != null && mPlayer.isPlaying()) {
                        mPlayer.stop();
                        mPlayer = null;
                    }
                } else {
                    Applications.preference.put(Preference.SOUND, true);
                    iv_sound_set.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_volume_white));
                }
                break;
            case R.id.btn_action:
                if (btn_action.isChecked()) {
                    iv_play.setVisibility(View.VISIBLE);
                    iv_pause.setVisibility(View.GONE);
                    btn_exercise_out.setVisibility(View.VISIBLE);
                    pauseTimer();
                    isActionPause = true;
                    if (Applications.preference.getValue(Preference.SOUND, CommonUtil.D_SOUND)) {
                        if (mPlayer != null && mPlayer.isPlaying()) {
                            mPlayer.pause();
                        }
                    }
                } else {
                    iv_play.setVisibility(View.GONE);
                    iv_pause.setVisibility(View.VISIBLE);
                    btn_exercise_out.setVisibility(View.GONE);
                    resumeTimer();
                    isActionPause = false;
                    if (Applications.preference.getValue(Preference.SOUND, CommonUtil.D_SOUND)) {
                        if (mPlayer != null) {
                            mPlayer.play();
                        }
                    }
                }
                break;
            case R.id.btn_exercise_out:
                goBack();
                break;
            case R.id.btn_skip:
                if (mPlayer != null) {
                    mPlayer.stop();
                    mPlayer = null;
                }
                stopTimer();
                chkExerciseStep();
                break;
        }
    }

    public void goSuccess() {
        Intent intent = new Intent(ExerciseActivity.this, SuccessActivity.class);
        startActivity(intent);
        finish();
    }

    public void goBack() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer = null;
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isSwitchPause) {
            pauseTimer();
            if (mPlayer != null && mPlayer.isPlaying()) {
                mPlayer.pause();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (timer != null) {
                timer.cancel();
            }
            if (exercise_timer != null) {
                exercise_timer.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}