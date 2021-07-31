package Unlike.tabatmie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Unlike.tabatmie.Dto.RecordDTO;
import Unlike.tabatmie.R;
import Unlike.tabatmie.adapter.RecordAdapter;
import Unlike.tabatmie.adapter.RecordDeleteAdapter;
import Unlike.tabatmie.connect.CallRetrofit;
import Unlike.tabatmie.dialog.TabatimeDialog;
import Unlike.tabatmie.util.Applications;
import Unlike.tabatmie.util.CommonUtil;
import Unlike.tabatmie.util.Preference;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecordActivity extends AppCompatActivity implements RecordAdapter.ItemClick, RecordDeleteAdapter.ItemClick {

    private String TAG = this.getClass().toString();

    @BindView(R.id.btn_back)
    RelativeLayout btn_back;
    @BindView(R.id.btn_delete)
    RelativeLayout btn_delete;
    @BindView(R.id.tv_delete_info)
    TextView tv_delete_info;

    @BindView(R.id.recycler_layer)
    RelativeLayout recycler_layer;

    @BindView(R.id.layer_record)
    RecyclerView layer_record;
    private RecordAdapter recordAdapter;
    private ArrayList<RecordDTO> recordList;
    TabatimeDialog selecet_dialog;

    @BindView(R.id.layer_record_delete)
    RecyclerView layer_record_delete;
    private RecordDeleteAdapter recordDeleteAdapter;
    private List<Integer> deleteList;
    private HashMap<Integer, Boolean> map;

    @BindView(R.id.layer_delete)
    LinearLayout layer_delete;
    @BindView(R.id.btn_cancel)
    RelativeLayout btn_cancel;
    @BindView(R.id.btn_record_delete)
    RelativeLayout btn_record_delete;
    private TabatimeDialog delete_dialog;

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
        recordList = new ArrayList<>();
        deleteList = new ArrayList<>();
        map = new HashMap<>();

        CallRetrofit call = new CallRetrofit();
        call.callRecord();
    }

    public void setRecord(ArrayList<RecordDTO> recordList, boolean isUpdate) {
        if (recordList.size() > 0) {
            this.recordList = recordList;
            Log.e(TAG, recordList.size() + "");
            if (!isUpdate) {
                recordAdapter = new RecordAdapter(this, recordList);
                recordAdapter.setOnItemClick(RecordActivity.this);
                layer_record.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                layer_record.setAdapter(recordAdapter);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        CommonUtil.getDisplayWidth(RecordActivity.this) - (CommonUtil.dpToPx(16) * 2),
                        RelativeLayout.LayoutParams.MATCH_PARENT
                );
                params.addRule(RelativeLayout.CENTER_HORIZONTAL, -1);
                layer_record.setLayoutParams(params);

                recordDeleteAdapter = new RecordDeleteAdapter(this, recordList);
                recordDeleteAdapter.setOnDeleteClick(RecordActivity.this);
                layer_record_delete.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                layer_record_delete.setAdapter(recordDeleteAdapter);

                btn_delete.setVisibility(View.VISIBLE);
            } else {
                closeDelete();
            }
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
                if (selecet_dialog != null) {
                    selecet_dialog.dismiss();
                }
                btn_delete.setVisibility(View.GONE);
                layer_delete.setVisibility(View.VISIBLE);
                tv_delete_info.setVisibility(View.VISIBLE);

                layer_record_delete.setVisibility(View.VISIBLE);
                recordAdapter.setOnItemClick(null);
                listAnimation(false);
                break;
            case R.id.btn_cancel:
                closeDelete();
                break;
            case R.id.btn_record_delete:
                delete_dialog = new TabatimeDialog(this);
                if (!delete_dialog.isShowing()) {
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
                            if (getDeleteList() != null && getDeleteList().size() > 0) {
                                CallRetrofit call = new CallRetrofit();
                                call.callRecordDelete(getDeleteList(), recordList);
                            } else {
                                Toast.makeText(RecordActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    delete_dialog.setCancelable(false);
                    delete_dialog.show();
                }
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
        if (!selecet_dialog.isShowing()) {
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
    }

    public void goMain() {
        Intent intent = new Intent(RecordActivity.this, MainActivity.class);
        startActivity(intent);
        onBackPressed();
    }

    public void listAnimation(boolean close) {
        int fromXelta = 0;
        int toXDelta = 0;
        int distance = this.getResources().getDimensionPixelSize(R.dimen.mt36);
        if (close) {
            fromXelta = distance;
        } else {
            toXDelta = distance;
        }
        TranslateAnimation moveAnim = new TranslateAnimation(fromXelta, toXDelta, 0, 0);
        moveAnim.setDuration(500);
        moveAnim.setFillAfter(true);
        layer_record.setAnimation(moveAnim);
        moveAnim.start();
    }

    public void closeDelete() {
        btn_delete.setVisibility(View.VISIBLE);
        layer_delete.setVisibility(View.GONE);
        tv_delete_info.setVisibility(View.GONE);

        layer_record_delete.setVisibility(View.GONE);
        recordAdapter.setOnItemClick(RecordActivity.this);
        listAnimation(true);
        recordDeleteAdapter.setCheckBox(true);
        recordDeleteAdapter.notifyDataSetChanged();
        recordAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDeleteClick(int idx, boolean isChecked) {
        map.put(idx, isChecked);
    }

    public List<Integer> getDeleteList() {
        try {
            if (map != null && !map.isEmpty()) {
                List<Integer> deleteList = new ArrayList<>();
                for (Integer key : map.keySet()) {
                    if (map.get(key)) {
                        deleteList.add(key);
                    }
                }
                return deleteList;
            }
        } catch (Exception e) {
            e.printStackTrace();
            deleteList = null;
        }
        return deleteList;
    }
}