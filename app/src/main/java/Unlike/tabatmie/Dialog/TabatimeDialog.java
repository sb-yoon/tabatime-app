package Unlike.tabatmie.Dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import Unlike.tabatmie.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TabatimeDialog extends Dialog {
    private int dialogType = 0;

    @BindView(R.id.tv_dialog_title)
    TextView tv_dialog_title;
    String title;

    @BindView(R.id.btn_close)
    RelativeLayout btn_close;
    @BindView(R.id.iv_close)
    ImageView iv_close;
    private View.OnClickListener closeClickListener;

    @BindView(R.id.layer_edits)
    RelativeLayout layer_edits;
    @BindView(R.id.layer_time_edit)
    LinearLayout layer_time_edit;
    @BindView(R.id.et_min)
    EditText et_min;
    @BindView(R.id.et_sec)
    EditText et_sec;
    @BindView(R.id.layer_cnt_edit)
    LinearLayout layer_cnt_edit;
    @BindView(R.id.et_cnt)
    EditText et_cnt;

    @BindView(R.id.tv_edit_info)
    TextView tv_edit_info;
    String edit_info;

    @BindView(R.id.btn_apply)
    RelativeLayout btn_apply;
    @BindView(R.id.tv_apply)
    TextView tv_apply;
    private View.OnClickListener applyClickListener;

    @BindView(R.id.layer_btns)
    LinearLayout layer_btns;
    @BindView(R.id.btn_cancel)
    RelativeLayout btn_cancel;
    @BindView(R.id.btn_ok)
    RelativeLayout btn_ok;
    private View.OnClickListener cancelClickListener;
    private View.OnClickListener okClickListener;


    public void setDialogType(int dialogType) {
        this.dialogType = dialogType;
    }

    public void closeClickListener(View.OnClickListener closeClickListener) {
        this.closeClickListener = closeClickListener;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void seteditInfo(String edit_info) {
        this.edit_info = edit_info;
    }

    public void applyClickListener(View.OnClickListener applyClickListener) {
        this.applyClickListener = applyClickListener;
    }

    public void cancelClickListener(View.OnClickListener cancelClickListener) {
        this.cancelClickListener = cancelClickListener;
    }

    public void okClickListener(View.OnClickListener okClickListener) {
        this.okClickListener = okClickListener;
    }

    public TabatimeDialog(@NonNull Context context) {
        super(context, R.style.TabatimeDialog);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_tabatime);
        ButterKnife.bind(this);

        tv_dialog_title.setText(title);

        if (dialogType == 0) {
            setBasicDialog();
        } else {
            iv_close.setVisibility(View.VISIBLE);
            btn_close.setVisibility(View.VISIBLE);
            layer_edits.setVisibility(View.VISIBLE);
            if (dialogType == 1) {
                layer_time_edit.setVisibility(View.VISIBLE);
                layer_cnt_edit.setVisibility(View.GONE);
            } else if (dialogType == 2) {
                layer_time_edit.setVisibility(View.GONE);
                layer_cnt_edit.setVisibility(View.VISIBLE);
            }
            tv_edit_info.setVisibility(View.VISIBLE);
            tv_edit_info.setText(edit_info);
            btn_apply.setVisibility(View.VISIBLE);
            layer_btns.setVisibility(View.GONE);
        }

        btn_close.setOnClickListener(closeClickListener);
        btn_apply.setOnClickListener(applyClickListener);
        btn_cancel.setOnClickListener(cancelClickListener);
        btn_ok.setOnClickListener(okClickListener);

        setfilter(et_min);
        setfilter(et_sec);
        setfilter(et_cnt);
    }

    @Override
    public void dismiss() {
        try {
            setBasicDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.dismiss();
    }

    public void setBasicDialog() {
        iv_close.setVisibility(View.GONE);
        btn_close.setVisibility(View.GONE);
        layer_edits.setVisibility(View.GONE);
        layer_time_edit.setVisibility(View.GONE);
        layer_cnt_edit.setVisibility(View.GONE);
        tv_edit_info.setVisibility(View.GONE);
        btn_apply.setVisibility(View.GONE);
        layer_btns.setVisibility(View.VISIBLE);
    }

    public void setfilter(EditText et) {
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                boolean isEnabled = false;
                if (et.getText().toString().length() > 0) {
                    isEnabled = true;
                }
                if (dialogType == 1) {
                    if (isEnabled) {
                        tv_apply.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                        btn_apply.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.layer_dailog_apply_active));
                    } else {
                        tv_apply.setTextColor(ContextCompat.getColor(getContext(), R.color.light_gray));
                        btn_apply.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.layer_dailog_apply));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        et.addTextChangedListener(watcher);
    }
}
