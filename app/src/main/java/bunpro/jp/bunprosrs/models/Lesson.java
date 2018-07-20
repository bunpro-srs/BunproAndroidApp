package bunpro.jp.bunprosrs.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Lesson implements Parcelable {

    public int id;
    public int jlpt_level;
    public String created_at, updated_at;
    public List<Integer> grammar_point_ids;

    public List<GrammarPoint> grammar_points;

    public Lesson() {

    }

    protected Lesson(Parcel in) {
        id = in.readInt();
        jlpt_level = in.readInt();
        created_at = in.readString();
        updated_at = in.readString();
        grammar_points = in.createTypedArrayList(GrammarPoint.CREATOR);
    }

    public static final Creator<Lesson> CREATOR = new Creator<Lesson>() {
        @Override
        public Lesson createFromParcel(Parcel in) {
            return new Lesson(in);
        }

        @Override
        public Lesson[] newArray(int size) {
            return new Lesson[0];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(jlpt_level);
        parcel.writeString(created_at);
        parcel.writeString(updated_at);
        parcel.writeTypedList(grammar_points);
    }
}
