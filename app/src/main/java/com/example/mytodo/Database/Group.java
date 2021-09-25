package com.example.mytodo.Database;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "group_tbl")
public class Group implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int groupId;
    private String groupTitle;
    private int counter;
    private boolean isStar;

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public boolean isStar() {
        return isStar;
    }

    public void setStar(boolean star) {
        isStar = star;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.groupId);
        dest.writeString(this.groupTitle);
        dest.writeInt(this.counter);
        dest.writeByte(this.isStar ? (byte) 1 : (byte) 0);
    }

    public void readFromParcel(Parcel source) {
        this.groupId = source.readInt();
        this.groupTitle = source.readString();
        this.counter = source.readInt();
        this.isStar = source.readByte() != 0;
    }

    public Group() {
    }

    protected Group(Parcel in) {
        this.groupId = in.readInt();
        this.groupTitle = in.readString();
        this.counter = in.readInt();
        this.isStar = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Group> CREATOR = new Parcelable.Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel source) {
            return new Group(source);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };
}

