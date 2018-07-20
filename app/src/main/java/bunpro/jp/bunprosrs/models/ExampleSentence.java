package bunpro.jp.bunprosrs.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ExampleSentence implements Parcelable {

    public int id;
    public int grammar_point_id;
    public String structure;
    public String japanese;
    public String english;
    public String alternate_japanese;
    public String created_at, updated_at;
    public String audio_link;
    public String nuance;

    public ExampleSentence() {

    }

    protected ExampleSentence(Parcel in) {
        id = in.readInt();
        grammar_point_id = in.readInt();
        structure = in.readString();
        japanese = in.readString();
        english = in.readString();
        alternate_japanese = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
        audio_link = in.readString();
        nuance = in.readString();
    }

    public static final Creator<ExampleSentence> CREATOR = new Creator<ExampleSentence>() {
        @Override
        public ExampleSentence createFromParcel(Parcel in) {
            return new ExampleSentence(in);
        }

        @Override
        public ExampleSentence[] newArray(int size) {
            return new ExampleSentence[0];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(grammar_point_id);
        parcel.writeString(structure);
        parcel.writeString(japanese);
        parcel.writeString(english);
        parcel.writeString(alternate_japanese);
        parcel.writeString(created_at);
        parcel.writeString(updated_at);
        parcel.writeString(audio_link);
        parcel.writeString(nuance);
    }
}
