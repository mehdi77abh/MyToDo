package com.example.mytodo.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbl_tasks")
public class Task implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    @ColumnInfo(name = "is_complete")
    private boolean isComplete;
    private int importance;
    private long dateLong;
    private long notificationId;
    private String description;
    private int groupId;
    private String date;

    public long getDateLong() {
        return dateLong;
    }

    public void setDateLong(long dateLong) {
        this.dateLong = dateLong;
    }

    public long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(long notificationId) {
        this.notificationId = notificationId;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }




    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.title);
        dest.writeByte(this.isComplete ? (byte) 1 : (byte) 0);
        dest.writeInt(this.importance);
        dest.writeLong(this.dateLong);
        dest.writeLong(this.notificationId);
        dest.writeString(this.description);
        dest.writeInt(this.groupId);
        dest.writeString(this.date);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readLong();
        this.title = source.readString();
        this.isComplete = source.readByte() != 0;
        this.importance = source.readInt();
        this.dateLong = source.readLong();
        this.notificationId = source.readLong();
        this.description = source.readString();
        this.groupId = source.readInt();
        this.date = source.readString();
    }

    public Task() {
    }

    protected Task(Parcel in) {
        this.id = in.readLong();
        this.title = in.readString();
        this.isComplete = in.readByte() != 0;
        this.importance = in.readInt();
        this.dateLong = in.readLong();
        this.notificationId = in.readLong();
        this.description = in.readString();
        this.groupId = in.readInt();
        this.date = in.readString();
    }

    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel source) {
            return new Task(source);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}
