package eu.niggas_with_attitude.qrify.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "saved_codes")
public class SavedCode {
    @PrimaryKey(autoGenerate = true) // Unique ID
    private int id;

    @ColumnInfo(name = "code") // Saved code string
    private String code;

    @ColumnInfo(name = "source")
    private int source; // 0 - scanned, 1 - generated

    // Don't use this
    public void setId(int id) {
        this.id = id;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public int getSource() {
        return source;
    }
}
