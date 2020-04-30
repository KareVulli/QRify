package eu.niggas_with_attitude.qrify;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import eu.niggas_with_attitude.qrify.database.model.SavedCode;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<SavedCode> data;
    private OnHistoryItemClickListener listener;

    HistoryAdapter(List<SavedCode> data, OnHistoryItemClickListener listener) {
        this.data = data;
        this.listener = listener;
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
        SavedCode code = data.get(position);
        holder.Code.setText(code.getCode());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(code));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {

        TextView Code;

        HistoryViewHolder(View v) {
            super(v);
            Code = v.findViewById(R.id.history_item_code);
        }
    }

    public interface OnHistoryItemClickListener {
        void onItemClick(SavedCode code);
    }
}
