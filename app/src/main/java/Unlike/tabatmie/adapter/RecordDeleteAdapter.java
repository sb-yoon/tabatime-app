package Unlike.tabatmie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import Unlike.tabatmie.Dto.RecordDTO;
import Unlike.tabatmie.R;

public class RecordDeleteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<RecordDTO> recordList;
    private ArrayList<Integer> deleteList;
    private boolean isChecked;

    private RecordDeleteAdapter.ItemClick deleteClick;

    public interface ItemClick {
        void onDeleteClick(int idx, boolean isChecked);
    }

    public void setOnDeleteClick(RecordDeleteAdapter.ItemClick deleteClick) {
        this.deleteClick = deleteClick;
    }

    public void setCheckBox(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public RecordDeleteAdapter(Context context, ArrayList<RecordDTO> recordList) {
        this.context = context;
        this.recordList = recordList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_record_delete, parent, false);
        deleteList = new ArrayList<>();
        return new RecordViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecordViewHolder recordViewHolder = (RecordViewHolder) holder;
        if (!recordList.isEmpty()) {
            if (isChecked) {
                recordViewHolder.btn_checked.setChecked(false);
            }

            recordViewHolder.btn_checked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (deleteClick != null) {
                        deleteClick.onDeleteClick(recordList.get(position).getId(), recordViewHolder.btn_checked.isChecked());

                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return recordList != null ? recordList.size() : 0;
    }

    static class RecordViewHolder extends RecyclerView.ViewHolder {
        CheckBox btn_checked;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            btn_checked = (CheckBox) itemView.findViewById(R.id.btn_checked);
        }
    }
}
