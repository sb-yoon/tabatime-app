package Unlike.tabatmie.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import Unlike.tabatmie.R;
import Unlike.tabatmie.connect.CallRetrofit;
import Unlike.tabatmie.util.Applications;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StatActivity extends AppCompatActivity implements Animator.AnimatorListener {

    @BindView(R.id.btn_back)
    RelativeLayout btn_back;

    @BindView(R.id.tv_comment)
    TextView tv_comment;
    @BindView(R.id.tv_lank)
    TextView tv_lank;

    @BindView(R.id.arc_progress)
    com.github.lzyzsd.circleprogress.ArcProgress arc_progress;
    private int ani = 1000;

    @BindView(R.id.tv_total_cnt)
    TextView tv_total_cnt;

    @BindView(R.id.tv_stat_hour)
    TextView tv_stat_hour;
    @BindView(R.id.tv_stat_min)
    TextView tv_stat_min;
    @BindView(R.id.tv_stat_sec)
    TextView tv_stat_sec;
    private ValueAnimator hourAnimator = null, minAnimator = null, secAnimator = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);

        ButterKnife.bind(this);

        try {
            Applications.setRefreshActivity(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        init();
    }

    public void init() {
        CallRetrofit call = new CallRetrofit();
        call.callStat();
    }

    @OnClick({R.id.btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
        }
    }

    public void setStat(int lank, int cnt, int time) {
        tv_comment.setText(getResources().getString(R.string.user_now));
        tv_lank.setVisibility(View.VISIBLE);
        tv_lank.setText(getResources().getString(R.string.lank, lank + "") + "%");
        setProgressAnim(100 - lank);
        setCntAnim(cnt);
        setTimeAnim(time);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void setProgressAnim(int lank) {
        ValueAnimator lankAnimator = ValueAnimator.ofInt(0, lank);
        lankAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                arc_progress.setProgress(value);
            }
        });
        lankAnimator.setDuration(ani);
        lankAnimator.start();
    }

    public void setCntAnim(int cnt) {
        ValueAnimator lankAnimator = ValueAnimator.ofInt(0, cnt);
        lankAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                tv_total_cnt.setText(value + "");
            }
        });
        lankAnimator.setDuration(ani);
        lankAnimator.start();
    }

    public void setTimeAnim(int time) {
        int hour, min, sec;
        sec = time;
        min = sec / 60;
        hour = min / 60;
        sec = sec % 60;
        min = min % 60;

        int animator = 0;
        if (hour > 0) {
            animator++;
        }
        if (min > 0) {
            animator++;
        }
        if (sec > 0) {
            animator++;
        }
        if (animator == 0) {
            animator = 1;
        }
        int anim_time = ani / animator;
        Log.e("stat", anim_time + "");

        if (hour > 0) {
            hourAnimator = ValueAnimator.ofInt(0, hour);
            hourAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    tv_stat_hour.setText(String.format("%02d", value));
                }
            });
            hourAnimator.setDuration(anim_time);
        }
        if (min > 0) {
            minAnimator = ValueAnimator.ofInt(0, min);
            minAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    tv_stat_min.setText(String.format("%02d", value));
                }
            });
            minAnimator.setDuration(anim_time);
        }
        if (sec > 0) {
            secAnimator = ValueAnimator.ofInt(0, sec);
            secAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    tv_stat_sec.setText(String.format("%02d", value));
                }
            });
            secAnimator.setDuration(anim_time);
        }
        if (secAnimator != null) {
            secAnimator.start();
            secAnimator.addListener(this);
        } else if (minAnimator != null) {
            minAnimator.start();
            minAnimator.addListener(this);
        } else if (hourAnimator != null) {
            hourAnimator.start();
            hourAnimator.addListener(this);
        }
    }

    @Override
    public void onAnimationStart(Animator animator) {

    }

    @Override
    public void onAnimationEnd(Animator animator) {
        if (secAnimator != null) {
            secAnimator = null;
            if (minAnimator != null) {
                minAnimator.start();
                minAnimator.addListener(this);
            }
        } else if (minAnimator != null) {
            minAnimator = null;
            if (hourAnimator != null) {
                hourAnimator.start();
            }
        }
    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }
}