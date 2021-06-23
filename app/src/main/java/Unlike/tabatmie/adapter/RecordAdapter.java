package Unlike.tabatmie.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Insets;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowMetrics;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Unlike.tabatmie.Dto.RecordDTO;
import Unlike.tabatmie.R;
import Unlike.tabatmie.util.Applications;
import Unlike.tabatmie.util.CommonUtil;
import Unlike.tabatmie.util.Preference;

public class RecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<RecordDTO> recordList;

    private RecordAdapter.ItemClick itemClick;

    public RecordAdapter(Context context, ArrayList<RecordDTO> recordList) {
        this.context = context;
        this.recordList = recordList;
    }

    public interface ItemClick {
        void onItemClick(View view, int i);
    }

    public void setOnItemClick(RecordAdapter.ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_record, parent, false);
        return new RecordViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecordViewHolder recordViewHolder = (RecordViewHolder) holder;
        if (!recordList.isEmpty()) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    getWidth() - (CommonUtil.dpToPx(16) * 2),
                    RelativeLayout.LayoutParams.MATCH_PARENT
            );
            params.addRule(RelativeLayout.CENTER_HORIZONTAL, -1);
            recordViewHolder.layer_record.setLayoutParams(params);

            if (!Applications.preference.getValue(Preference.DELETE, "").isEmpty()) {
                if (Applications.preference.getValue(Preference.DELETE, "").equals("Y")) {
                    recordViewHolder.layer_record.setOnClickListener(null);

                    recordViewHolder.btn_checked.setVisibility(View.VISIBLE);
                    recordViewHolder.btn_checked.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (recordViewHolder.btn_checked.isChecked()) {
                                recordViewHolder.iv_checked.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_check_off));
                            } else {
                                recordViewHolder.iv_checked.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_check_on));
                            }
                        }
                    });

                    float distance = context.getResources().getDimensionPixelSize(R.dimen.mt36);
                    TranslateAnimation moveAnim = new TranslateAnimation(0, distance, 0, 0);
                    moveAnim.setDuration(1000);
                    moveAnim.setFillAfter(true);
                    recordViewHolder.layer_record.setAnimation(moveAnim);
                    moveAnim.start();
                } else {
                    recordViewHolder.btn_checked.setVisibility(View.GONE);

                    float distance = context.getResources().getDimensionPixelSize(R.dimen.mt36);
                    TranslateAnimation moveAnim = new TranslateAnimation(distance, 0, 0, 0);
                    moveAnim.setDuration(1000);
                    moveAnim.setFillAfter(true);
                    recordViewHolder.layer_record.setAnimation(moveAnim);
                    moveAnim.start();
                }
            } else {
                recordViewHolder.layer_record.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (itemClick != null) {
                            itemClick.onItemClick(v, position);
                        }
                    }
                });

                recordViewHolder.btn_checked.setVisibility(View.GONE);
            }

            String date = recordList.get(position).getRegDate();
            String current_date = new SimpleDateFormat("yyyy.MM.dd").format(new Date());
            if (date.equals(current_date)) {
                recordViewHolder.tv_date.setText(context.getResources().getString(R.string.today));
            } else {
                recordViewHolder.tv_date.setText(date);
            }
            recordViewHolder.tv_exercise_time.setText(CommonUtil.getTime(recordList.get(position).getExercise_time()));
            recordViewHolder.tv_exercise.setText(recordList.get(position).getExercise() + "");
            recordViewHolder.tv_rest.setText(recordList.get(position).getRest() + "");
            recordViewHolder.tv_set.setText(recordList.get(position).getSet() + "");
            recordViewHolder.tv_round.setText(recordList.get(position).getRound() + "");
        }
    }

    @Override
    public int getItemCount() {
        return recordList != null ? recordList.size() : 0;
    }

    static class RecordViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_checked;
        ToggleButton btn_checked;
        LinearLayout layer_record;
        TextView tv_date, tv_exercise_time, tv_exercise, tv_rest, tv_set, tv_round;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_checked = (ImageView) itemView.findViewById(R.id.iv_checked);
            btn_checked = (ToggleButton) itemView.findViewById(R.id.btn_checked);
            layer_record = (LinearLayout) itemView.findViewById(R.id.layer_record);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_exercise_time = (TextView) itemView.findViewById(R.id.tv_exercise_time);
            tv_exercise = (TextView) itemView.findViewById(R.id.tv_exercise);
            tv_rest = (TextView) itemView.findViewById(R.id.tv_rest);
            tv_set = (TextView) itemView.findViewById(R.id.tv_set);
            tv_round = (TextView) itemView.findViewById(R.id.tv_round);
        }
    }

    public int getWidth() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = ((Activity) context).getWindowManager().getCurrentWindowMetrics();
            Insets insets = windowMetrics.getWindowInsets()
                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
            return windowMetrics.getBounds().width() - insets.left - insets.right;
        } else {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics.widthPixels;
        }
    }
}
