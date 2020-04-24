package eu.niggas_with_attitude.qrify;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import eu.niggas_with_attitude.qrify.database.model.SavedCode;



public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private ClipboardManager clipboardManager;
    private List<SavedCode> data;
    Context context;

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {

        public TextView Code;

        public HistoryViewHolder(View v) {
            super(v);
            Code = v.findViewById(R.id.history_item_code);
        }
    }


    public HistoryAdapter(List<SavedCode> data) {
        this.data = data;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_history_item, parent, false);

        HistoryViewHolder vh = new HistoryViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Position = vh.getAdapterPosition();
                String selectedCode = data.get(Position).getCode();
                clipboardManager = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("text", selectedCode);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(context, "Text Copied", Toast.LENGTH_SHORT).show();

            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        String text = data.get(position).getCode();
        holder.Code.setText(text);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
