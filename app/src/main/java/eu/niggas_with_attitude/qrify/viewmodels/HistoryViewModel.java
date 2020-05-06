package eu.niggas_with_attitude.qrify.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import eu.niggas_with_attitude.qrify.database.model.SavedCode;
import eu.niggas_with_attitude.qrify.repositories.CodeRepository;

public class HistoryViewModel extends AndroidViewModel {
    private CodeRepository codeRepository;
    private LiveData<List<SavedCode>> savedCodeList;

    public HistoryViewModel(@NonNull Application application) {
        super(application);
        codeRepository = new CodeRepository(application);
        savedCodeList = codeRepository.getAll();
    }

    public LiveData<List<SavedCode>> getSavedCodeList() {
        return savedCodeList;
    }

    public void insertSavedCode(SavedCode savedCode) {
        codeRepository.insert(savedCode);
    }
}
