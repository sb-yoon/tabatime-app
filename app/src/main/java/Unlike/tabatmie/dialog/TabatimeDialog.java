package Unlike.tabatmie.dialog;


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
    @BindView(R.id.tv_cancel)
    TextView tv_cancel;
    @BindView(R.id.btn_ok)
    RelativeLayout btn_ok;
    @BindView(R.id.tv_ok)
    TextView tv_ok;
    private View.OnClickListener cancelClickListener;
    private View.OnClickListener okClickListener;
    private String cancel, ok;


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

    public void setCancelBtnClickListener(String cancel, View.OnClickListener cancelClickListener) {
        this.cancel = cancel;
        this.cancelClickListener = cancelClickListener;
    }

    public void setOkBtnClickListener(String ok, View.OnClickListener okClickListener) {
        this.ok = ok;
        this.okClickListener = okClickListener;
    }

    public void setCancelBtnClickListener(View.OnClickListener cancelClickListener) {
        this.cancel = cancel;
        this.cancelClickListener = cancelClickListener;
    }

    public void setOkBtnClickListener(View.OnClickListener okClickListener) {
        this.ok = ok;
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

            setfilter(et_min);
            setfilter(et_sec);
            setfilter(et_cnt);

            btn_close.setOnClickListener(closeClickListener);
            btn_apply.setOnClickListener(applyClickListener);
        }
    }

    @Override
    public void dismiss() {
        try {
            setBasicDialog();
            title = "";
            edit_info = "";
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

        btn_cancel.setOnClickListener(cancelClickListener);
        btn_ok.setOnClickListener(okClickListener);
        if (cancel != null && !cancel.equals("")) {
            tv_cancel.setText(cancel);
        }
        if (ok != null && !ok.equals("")) {
            tv_ok.setText(ok);
        }
    }

    public void setfilter(EditText et) {
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                boolean isEnabled = false;
                boolean isError = false;
                String etStr = et.getText().toString();
                if (!etStr.equals("")) {
                    int etNum = Integer.parseInt(etStr);
                    if (dialogType == 1) {
                        String time = String.format("%02d", getEtMin()) + String.format("%02d", getEtSec());
                        if (Integer.parseInt(time) >= 5 && Integer.parseInt(time) <= 300 && getEtSec() < 60) {
                            isError = false;
                        } else {
                            isError = true;
                        }
                    } else if (dialogType == 2) {
                        if (etNum >= 1 && etNum <= 20) {
                            isError = false;
                        } else {
                            isError = true;
                        }
                    }

                    if (isError) {
                        isEnabled = true;
                        et.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.edit_error));
                    } else {
                        isEnabled = false;
                        et.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.edit_line));
                    }
                } else {
                    et.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.edit_line));
                }

                if (isEnabled) {
                    tv_apply.setTextColor(ContextCompat.getColor(getContext(), R.color.light_gray));
                    btn_apply.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.layer_dailog_apply));
                    btn_apply.setClickable(false);
                } else {
                    tv_apply.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    btn_apply.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.layer_dailog_apply_active));
                    btn_apply.setClickable(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
        et.addTextChangedListener(watcher);
    }

    public int getEtMin() {
        try {
            if (et_min.getText().toString().length() > 0) {
                if (Integer.parseInt(et_min.getText().toString()) == -1) {
                    return 0;
                } else {
                    return Integer.parseInt(et_min.getText().toString());
                }
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    public int getEtSec() {
        try {
            if (et_sec.getText().toString().length() > 0) {
                if (Integer.parseInt(et_sec.getText().toString()) == -1) {
                    return 0;
                } else {
                    return Integer.parseInt(et_sec.getText().toString());
                }
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    public int getEtCnt() {
        return Integer.parseInt(et_cnt.getText().toString());
    }
}
