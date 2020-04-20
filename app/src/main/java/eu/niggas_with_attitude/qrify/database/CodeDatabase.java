package eu.niggas_with_attitude.qrify.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import eu.niggas_with_attitude.qrify.database.dao.SavedCodeDao;
import eu.niggas_with_attitude.qrify.database.model.SavedCode;

@Database(entities = {SavedCode.class}, version = 1)
public abstract class CodeDatabase extends RoomDatabase {
    public abstract SavedCodeDao getSavedCodeDao();
}
