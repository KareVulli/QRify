package eu.niggas_with_attitude.qrify;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import eu.niggas_with_attitude.qrify.database.model.SavedCode;
import eu.niggas_with_attitude.qrify.viewmodels.HistoryViewModel;

public class HistoryFragment extends Fragment implements HistoryAdapter.OnHistoryItemClickListener {

    private ClipboardManager clipboardManager;
    private HistoryViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        clipboardManager = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
        viewModel = new ViewModelProvider(requireActivity()).get(HistoryViewModel.class);

        HistoryAdapter mAdapter = new HistoryAdapter(new ArrayList<>(), this);

        RecyclerView recyclerView = view.findViewById(R.id.fragment_history_list_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);

        viewModel.getSavedCodeList().observe(getViewLifecycleOwner(), savedCodes -> {
            mAdapter.setData(savedCodes);
            mAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onItemClick(SavedCode code) {
        ClipData clipData = ClipData.newPlainText("text", code.getCode());
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(requireContext(), "Text Copied", Toast.LENGTH_SHORT).show();
    }
}
