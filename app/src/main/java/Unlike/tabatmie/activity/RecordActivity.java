package Unlike.tabatmie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import Unlike.tabatmie.Dto.RecordDTO;
import Unlike.tabatmie.R;
import Unlike.tabatmie.adapter.RecordAdapter;
import Unlike.tabatmie.connect.CallRetrofit;
import Unlike.tabatmie.dialog.TabatimeDialog;
import Unlike.tabatmie.util.Applications;
import Unlike.tabatmie.util.Preference;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecordActivity extends AppCompatActivity implements RecordAdapter.ItemClick {

    @BindView(R.id.btn_back)
    RelativeLayout btn_back;
    @BindView(R.id.btn_delete)
    RelativeLayout btn_delete;
    @BindView(R.id.tv_delete_info)
    TextView tv_delete_info;

    @BindView(R.id.layer_record)
    RecyclerView layer_record;
    private RecordAdapter recordAdapter;
    private ArrayList<RecordDTO> recordList;
    TabatimeDialog selecet_dialog;

    @BindView(R.id.layer_delete)
    LinearLayout layer_delete;
    @BindView(R.id.btn_cancel)
    RelativeLayout btn_cancel;
    @BindView(R.id.btn_record_delete)
    RelativeLayout btn_record_delete;
    TabatimeDialog delete_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        ButterKnife.bind(this);

        try {
            Applications.setRefreshActivity(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        init();
    }

    public void init() {
        Applications.preference.put(Preference.DELETE, "");

        recordList = new ArrayList<>();

        CallRetrofit call = new CallRetrofit();
        call.callRecord();
    }

    public void setRecord(ArrayList<RecordDTO> recordList) {
        if (recordList.size() > 0) {
            this.recordList = recordList;
            recordAdapter = new RecordAdapter(this, recordList);
            recordAdapter.setOnItemClick(RecordActivity.this);
            layer_record.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            layer_record.setAdapter(recordAdapter);
            btn_delete.setVisibility(View.VISIBLE);
        } else {
            btn_delete.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.btn_back, R.id.btn_delete, R.id.btn_cancel, R.id.btn_record_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_delete:
                Applications.preference.put(Preference.DELETE, "Y");
                btn_delete.setVisibility(View.GONE);
                layer_delete.setVisibility(View.VISIBLE);
                tv_delete_info.setVisibility(View.VISIBLE);
                recordAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_cancel:
                btn_delete.setVisibility(View.VISIBLE);
                layer_delete.setVisibility(View.GONE);
                tv_delete_info.setVisibility(View.GONE);

                Applications.preference.put(Preference.DELETE, "N");
                recordAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_record_delete:
                delete_dialog = new TabatimeDialog(this);
                delete_dialog.setTitle(getResources().getString(R.string.delete));
                delete_dialog.setCancelBtnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        delete_dialog.dismiss();
                    }
                });
                delete_dialog.setOkBtnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        delete_dialog.dismiss();
                    }
                });
                delete_dialog.setCancelable(false);
                delete_dialog.show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Override
    public void onItemClick(View view, int i) {
        selecet_dialog = new TabatimeDialog(this);
        selecet_dialog.setTitle(getResources().getString(R.string.routine));
        selecet_dialog.setCancelBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selecet_dialog.dismiss();
            }
        });
        selecet_dialog.setOkBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Applications.preference.put(Preference.EXERCISE, recordList.get(i).getExercise());
                Applications.preference.put(Preference.REST, recordList.get(i).getRest());
                Applications.preference.put(Preference.SET, recordList.get(i).getSet());
                Applications.preference.put(Preference.ROUND, recordList.get(i).getRound());
                Applications.preference.put(Preference.ROUND_RESET, recordList.get(i).getRound_reset());
                selecet_dialog.dismiss();
                goMain();
            }
        });
        selecet_dialog.setCancelable(false);
        selecet_dialog.show();
    }

    public void goMain() {
        Intent intent = new Intent(RecordActivity.this, MainActivity.class);
        startActivity(intent);
        onBackPressed();
    }
}