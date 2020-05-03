package eu.niggas_with_attitude.qrify.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import eu.niggas_with_attitude.qrify.database.CodeDatabase;
import eu.niggas_with_attitude.qrify.database.dao.SavedCodeDao;
import eu.niggas_with_attitude.qrify.database.model.SavedCode;

public class CodeRepository {
    private SavedCodeDao savedCodeDao;
    private LiveData<List<SavedCode>> savedCodes;

    public CodeRepository (Application application) {
        CodeDatabase db = CodeDatabase.getDatabase(application);
        savedCodeDao = db.getSavedCodeDao();
        savedCodes = savedCodeDao.getAll();
    }

    public LiveData<List<SavedCode>> getAll() {
        return savedCodes;
    }

    public void insert(SavedCode savedCode) {
        new insertAsyncTask(savedCodeDao).execute(savedCode);
    }

    private static class insertAsyncTask extends AsyncTask<SavedCode, Void, Void> {

        private SavedCodeDao asyncTaskDao;

        insertAsyncTask(SavedCodeDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final SavedCode... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }


}
