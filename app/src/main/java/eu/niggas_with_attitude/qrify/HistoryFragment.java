package eu.niggas_with_attitude.qrify;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import eu.niggas_with_attitude.qrify.database.CodeDatabase;
import eu.niggas_with_attitude.qrify.database.dao.SavedCodeDao;
import eu.niggas_with_attitude.qrify.database.model.SavedCode;

public class HistoryFragment extends Fragment {
    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        CodeDatabase codeDatabase = Room.databaseBuilder(requireContext(),
                CodeDatabase.class, "code-db").allowMainThreadQueries().build();
        SavedCodeDao savedCodeDao = codeDatabase.getSavedCodeDao();


        View rootView = inflater.inflate(R.layout.fragment_history, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_history_list_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<SavedCode> data = savedCodeDao.getAll();

        for(int i = 0; i < data.size(); i++){
            String text = data.get(i).getCode();
            Log.d("DATATEXT",text);
        }

        HistoryAdapter mAdapter = new HistoryAdapter(data);

        recyclerView.setAdapter(mAdapter);

        return rootView;
    }
}
