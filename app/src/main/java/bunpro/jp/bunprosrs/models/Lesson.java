package bunpro.jp.bunprosrs.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;
import java.util.List;

public class Lesson {

    public int id;
    public int jlpt_level;
    public String created_at, updated_at;
    public List<Integer> grammar_point_ids;

    public List<GrammarPoint> grammar_points;

    public Lesson() {

    }

    public static Comparator<Lesson> IdComparator = new Comparator<Lesson>() {
        @Override
        public int compare(Lesson point1, Lesson point2) {
            if (point1.id > point2.id) {
                return 1;
            } else {
                return -1;
            }
        }
    };

}
