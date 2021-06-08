package Unlike.tabatmie.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import Unlike.tabatmie.Dialog.TabatimeDialog;
import Unlike.tabatmie.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ProgressActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = this.getClass().toString();

    @BindView(R.id.btn_back)
    RelativeLayout btn_back;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_cnt_title)
    TextView tv_cnt_title;
    @BindView(R.id.layer_cnt)
    RecyclerView layer_cnt;

    @BindView(R.id.btn_self)
    RelativeLayout btn_self;
    String type;

    @BindView(R.id.btn_apply)
    TextView btn_apply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        init();
    }

    public void init() {
        ButterKnife.bind(this);

        btn_back.setOnClickListener(this);
        btn_self.setOnClickListener(this);
        btn_apply.setOnClickListener(this);

        try {
            String getTitle = getIntent().getStringExtra("title");
            tv_title.setText(getTitle);
            String getCntTitle = getIntent().getStringExtra("desc");
            tv_cnt_title.setText(getCntTitle);
            type = getIntent().getStringExtra("dialog_type");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_self:
                int dialog_type = Integer.parseInt(type);
                TabatimeDialog dialog = new TabatimeDialog(this);
                dialog.setDialogType(dialog_type);
                if (dialog_type == 1) {
                    dialog.setTitle(getResources().getString(R.string.dialog_title_time));
                    dialog.seteditInfo(getResources().getString(R.string.time_limit));
                } else {
                    dialog.setTitle(getResources().getString(R.string.dialog_title_cnt));
                    dialog.seteditInfo(getResources().getString(R.string.cnt_limit));
                }
                dialog.closeClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.applyClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                dialog.show();
                break;
            case R.id.btn_apply:
                finish();
                break;
        }
    }
}