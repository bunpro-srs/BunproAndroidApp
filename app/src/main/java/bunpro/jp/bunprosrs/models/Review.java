package bunpro.jp.bunprosrs.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Review implements Parcelable {

    public int id;
    public int user_id;
    public int study_question_id;
    public int grammar_point_id;
    public int times_correct;
    public int times_incorrect;
    public int streak;
    public String next_review;
    public String created_at, updated_at;
    public boolean complete;
    public String last_studied_at;
    public boolean was_correct;
    public boolean self_study;
    public int review_misses;
    public List<History> history;
    public List<Integer> missed_question_ids;
    public List<Integer> studied_question_ids;
    public String review_type;

    public Review() {

    }

    private Review(Parcel in) {
        id = in.readInt();
        user_id = in.readInt();
        study_question_id = in.readInt();
        grammar_point_id = in.readInt();
        times_correct = in.readInt();
        times_incorrect = in.readInt();
        streak = in.readInt();
        next_review = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
        complete = in.readByte() != 0;
        last_studied_at = in.readString();
        was_correct = in.readByte() != 0;
        self_study = in.readByte() != 0;
        review_misses = in.readInt();
        history = in.createTypedArrayList(History.CREATOR);
        review_type = in.readString();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(user_id);
        parcel.writeInt(study_question_id);
        parcel.writeInt(grammar_point_id);
        parcel.writeInt(times_correct);
        parcel.writeInt(times_incorrect);
        parcel.writeInt(streak);
        parcel.writeString(next_review);
        parcel.writeString(created_at);
        parcel.writeString(updated_at);
        parcel.writeByte((byte) (complete ? 1 : 0));
        parcel.writeString(last_studied_at);
        parcel.writeByte((byte) (was_correct ? 1 : 0));
        parcel.writeByte((byte) (self_study ? 1 : 0));
        parcel.writeInt(review_misses);
        parcel.writeTypedList(history);
        parcel.writeString(review_type);
    }
}
