package eu.niggas_with_attitude.qrify;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import eu.niggas_with_attitude.qrify.database.CodeDatabase;
import eu.niggas_with_attitude.qrify.database.dao.SavedCodeDao;
import eu.niggas_with_attitude.qrify.database.model.SavedCode;

public class HistoryFragment extends Fragment implements HistoryAdapter.OnHistoryItemClickListener {

    private ClipboardManager clipboardManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CodeDatabase codeDatabase = Room.databaseBuilder(requireContext(),
                CodeDatabase.class, "code-db").allowMainThreadQueries().build();
        SavedCodeDao savedCodeDao = codeDatabase.getSavedCodeDao();

        clipboardManager = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);

        List<SavedCode> data = savedCodeDao.getAll();
        HistoryAdapter mAdapter = new HistoryAdapter(data, this);

        RecyclerView recyclerView = view.findViewById(R.id.fragment_history_list_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(SavedCode code) {
        ClipData clipData = ClipData.newPlainText("text", code.getCode());
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(requireContext(), "Text Copied", Toast.LENGTH_SHORT).show();
    }
}
