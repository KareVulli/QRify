package eu.niggas_with_attitude.qrify.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import eu.niggas_with_attitude.qrify.database.dao.SavedCodeDao;
import eu.niggas_with_attitude.qrify.database.model.SavedCode;

@Database(entities = {SavedCode.class}, version = 1)
public abstract class CodeDatabase extends RoomDatabase {
    private static CodeDatabase instance;

    public static CodeDatabase getDatabase(final Context context) {
        if (instance == null) {
            synchronized (CodeDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.getApplicationContext(), CodeDatabase.class, "code-db"
                    ).build();
                }
            }
        }
        return instance;
    }

    public abstract SavedCodeDao getSavedCodeDao();
}
