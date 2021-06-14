package Unlike.tabatmie.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import Unlike.tabatmie.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class StatActivity extends AppCompatActivity implements View.OnClickListener, Animator.AnimatorListener {

    @BindView(R.id.btn_back)
    RelativeLayout btn_back;

    @BindView(R.id.tv_comment)
    TextView tv_comment;
    @BindView(R.id.tv_lank)
    TextView tv_lank;

    @BindView(R.id.arc_progress)
    com.github.lzyzsd.circleprogress.ArcProgress arc_progress;
    private int lank = 0;
    private int ani = 5000;

    @BindView(R.id.tv_total_cnt)
    TextView tv_total_cnt;

    @BindView(R.id.tv_stat_hour)
    TextView tv_stat_hour;
    @BindView(R.id.tv_stat_min)
    TextView tv_stat_min;
    @BindView(R.id.tv_stat_sec)
    TextView tv_stat_sec;
    private int hour, min, sec;
    private ValueAnimator hourAnimator = null, minAnimator = null, secAnimator = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);

        ButterKnife.bind(this);

        init();
    }

    public void init() {
        btn_back.setOnClickListener(this);
        setProgressAnim(lank);
        setTimeAnim(hour, min, sec);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_back:
                onBackPressed();
                break;
        }
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

    public void setTimeAnim(int hour, int min, int sec) {
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
        int time = ani / animator;
        if (hour > 0) {
            hourAnimator = ValueAnimator.ofInt(0, hour);
            hourAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    tv_stat_hour.setText(String.format("%02d", value));
                }
            });
            hourAnimator.setDuration(time);
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
            minAnimator.setDuration(time);
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
            secAnimator.setDuration(time);
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