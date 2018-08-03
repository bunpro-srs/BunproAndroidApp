package bunpro.jp.bunprosrs.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Lesson {

    public int id;
    public int jlpt_level;
    public String created_at, updated_at;
    public List<Integer> grammar_point_ids;

    public List<GrammarPoint> grammar_points;

    public Lesson() {

    }

}
