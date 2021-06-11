package Unlike.tabatmie.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import Unlike.tabatmie.R;
import Unlike.tabatmie.util.Applications;
import butterknife.BindView;

public class StatActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.btn_back)
    RelativeLayout btn_back;

    @BindView(R.id.tv_comment)
    TextView tv_comment;
    @BindView(R.id.tv_lank)
    TextView tv_lank;

    @BindView(R.id.tv_total_cnt)
    TextView tv_total_cnt;

    @BindView(R.id.tv_total_time)
    RelativeLayout tv_total_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);

        Applications.fristInit(this);
        init();
    }

    public void init() {
        btn_back.setOnClickListener(this);


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
}