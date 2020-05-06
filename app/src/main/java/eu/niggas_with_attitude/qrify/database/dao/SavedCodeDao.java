package eu.niggas_with_attitude.qrify.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import eu.niggas_with_attitude.qrify.database.model.SavedCode;

@Dao
public interface SavedCodeDao {

    // Get all codes
    @Query("SELECT * FROM saved_codes ORDER BY id DESC")
    LiveData<List<SavedCode>> getAll();

    // Get all codes where the source id matches
    @Query("SELECT * FROM saved_codes WHERE source = :sourceId")
    LiveData<List<SavedCode>> findBySource(int sourceId);

    // Insert a new code
    @Insert
    void insert(SavedCode savedCode);

    // Deletes a code, should not need to use it
    @Delete
    void delete(SavedCode... saved_codes);
}
