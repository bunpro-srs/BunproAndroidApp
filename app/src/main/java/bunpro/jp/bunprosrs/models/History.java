package bunpro.jp.bunprosrs.models;

import android.os.Parcel;
import android.os.Parcelable;

public class History implements Parcelable {

    public int id;
    public String time;
    public boolean status;
    public int attempts;
    public int streak;

    public History() {

    }

    protected History(Parcel in) {
        id = in.readInt();
        time = in.readString();
        status = in.readByte() != 0;
        attempts = in.readInt();
        streak = in.readInt();
    }

    public static final Creator<History> CREATOR = new Creator<History>() {
        @Override
        public History createFromParcel(Parcel in) {
            return new History(in);
        }

        @Override
        public History[] newArray(int size) {
            return new History[0];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(time);
        parcel.writeByte((byte) (status ? 1 : 0));
        parcel.writeInt(attempts);
        parcel.writeInt(streak);
    }
}
