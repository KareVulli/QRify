package eu.niggas_with_attitude.qrify.database.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "saved_codes")
public class SavedCode implements Parcelable {
    @PrimaryKey(autoGenerate = true) // Unique ID
    private int id;

    @ColumnInfo(name = "code") // Saved code string
    private String code = "";

    @ColumnInfo(name = "source")
    private int source = 0; // 0 - scanned, 1 - generated

    @ColumnInfo(name = "created")
    private long timestamp;

    public SavedCode() {
        timestamp = System.currentTimeMillis();
    }

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

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayCode() {
        if (code.startsWith("tel:")) {
            return code.substring(4);
        } else if (code.startsWith("mailto:")) {
            return code.substring(7);
        }
        return code;
    }

    public int getSource() {
        return source;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.code);
        dest.writeInt(this.source);
        dest.writeLong(this.timestamp);
    }

    protected SavedCode(Parcel in) {
        this.id = in.readInt();
        this.code = in.readString();
        this.source = in.readInt();
        this.timestamp = in.readLong();
    }

    public static final Parcelable.Creator<SavedCode> CREATOR = new Parcelable.Creator<SavedCode>() {
        @Override
        public SavedCode createFromParcel(Parcel source) {
            return new SavedCode(source);
        }

        @Override
        public SavedCode[] newArray(int size) {
            return new SavedCode[size];
        }
    };
}
