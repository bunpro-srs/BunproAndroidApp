package bunpro.jp.bunprosrs.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Review {

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


}
