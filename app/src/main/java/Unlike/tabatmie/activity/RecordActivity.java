package Unlike.tabatmie.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import Unlike.tabatmie.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecordActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.btn_back)
    RelativeLayout btn_back;
    @BindView(R.id.btn_delete)
    RelativeLayout btn_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        ButterKnife.bind(this);

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
            case R.id.btn_delete:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}