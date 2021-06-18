package Unlike.tabatmie.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

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

public class RecordActivity extends AppCompatActivity implements View.OnClickListener, RecordAdapter.ItemClick {

    @BindView(R.id.btn_back)
    RelativeLayout btn_back;
    @BindView(R.id.btn_delete)
    RelativeLayout btn_delete;

    @BindView(R.id.layer_record)
    RecyclerView layer_record;
    private RecordAdapter recordAdapter;
    private ArrayList<RecordDTO> recordList;

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
        btn_back.setOnClickListener(this);

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

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_delete:
                Applications.preference.put(Preference.DELETE, true);
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
        TabatimeDialog dialog = new TabatimeDialog(this);
        dialog.setTitle(getResources().getString(R.string.routine));
        dialog.setCancelBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setOkBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Applications.preference.put(Preference.EXERCISE, recordList.get(i).getExercise());
                Applications.preference.put(Preference.REST, recordList.get(i).getRest());
                Applications.preference.put(Preference.SET, recordList.get(i).getSet());
                Applications.preference.put(Preference.ROUND, recordList.get(i).getRound());
                Applications.preference.put(Preference.ROUND_RESET, recordList.get(i).getRound_reset());
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}