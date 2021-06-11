package Unlike.tabatmie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import Unlike.tabatmie.Dto.ProgressDTO;
import Unlike.tabatmie.R;

public class ProgressAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<ProgressDTO> progressList;
    private String type;
    private ProgressViewHolder progressViewHolder;

    public ProgressAdapter(Context context, ArrayList<ProgressDTO> progressList, String type) {
        this.context = context;
        this.progressList = progressList;
        this.type = type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_progress, parent, false);

        return new ProgressViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        progressViewHolder = (ProgressViewHolder) holder;
        if (!progressList.isEmpty()) {
            String cnt = progressList.get(position).getTv_cnt();
            if (Integer.parseInt(cnt) == 0) {
                progressViewHolder.tv_cnt.setText(cnt);
                progressViewHolder.tv_cnt.setVisibility(View.INVISIBLE);
                progressViewHolder.tv_title.setVisibility(View.INVISIBLE);
            } else {
                if (progressList.get(position).isCenter()) {
                    progressViewHolder.tv_title.setVisibility(View.VISIBLE);
                    progressViewHolder.tv_cnt.setVisibility(View.GONE);
                    progressViewHolder.tv_title.setText(cnt);
                } else {
                    progressViewHolder.tv_title.setVisibility(View.GONE);
                    progressViewHolder.tv_cnt.setVisibility(View.VISIBLE);
                    progressViewHolder.tv_cnt.setText(cnt);
                }

            }
        }
        holder.itemView.requestLayout();
    }

    @Override
    public int getItemCount() {
        return progressList != null ? progressList.size() : 0;
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {

        TextView tv_cnt, tv_title;

        public ProgressViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_cnt = (TextView) itemView.findViewById(R.id.tv_cnt);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }
}
