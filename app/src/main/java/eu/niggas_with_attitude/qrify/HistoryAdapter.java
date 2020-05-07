package eu.niggas_with_attitude.qrify;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import eu.niggas_with_attitude.qrify.database.model.SavedCode;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<SavedCode> data;
    private OnHistoryItemClickListener listener;

    HistoryAdapter(List<SavedCode> data, OnHistoryItemClickListener listener) {
        this.data = data;
        this.listener = listener;
    }

    void setData(List<SavedCode> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_history_item, parent, false);
        return new HistoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SavedCode code = data.get(position);
        holder.Code.setText(code.getCode());
        holder.source.setText(getCodeSource(code));
        holder.date.setText(sf.format(new Date(code.getTimestamp())));
        holder.itemView.setOnClickListener(v -> listener.onItemClick(code));
    }

    private String getCodeSource(SavedCode savedCode) {
        if(savedCode.getSource() == 1) {
            return "generated";
        } else {
            return "scanned";
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {

        TextView Code;
        TextView date;
        TextView source;

        HistoryViewHolder(View v) {
            super(v);
            Code = v.findViewById(R.id.history_item_code);
            date = v.findViewById(R.id.history_item_date);
            source = v.findViewById(R.id.history_item_source);
        }
    }

    public interface OnHistoryItemClickListener {
        void onItemClick(SavedCode code);
    }
}
