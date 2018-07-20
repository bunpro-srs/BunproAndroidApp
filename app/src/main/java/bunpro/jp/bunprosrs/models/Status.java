package bunpro.jp.bunprosrs.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Status implements Parcelable {

    public String name;
    public int status;
    public int total;

    public Status() {

    }

    public Status(String name, int status, int total) {
        this.name = name;
        this.total = total;
        this.status = status;
    }

    protected Status(Parcel in) {
        name = in.readString();
        status = in.readInt();
        total = in.readInt();
    }

    public static final Creator<Status> CREATOR = new Creator<Status>() {
        @Override
        public Status createFromParcel(Parcel in) {
            return new Status(in);
        }

        @Override
        public Status[] newArray(int size) {
            return new Status[0];
        }
    };

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getStatus() {
        return this.status;
    }

    public int getTotal() {
        return this.total;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(status);
        parcel.writeInt(total);
    }
}
