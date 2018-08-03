package bunpro.jp.bunprosrs.models;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Comparator;
import java.util.List;

public class GrammarPoint {

    public int id;
    public String title;
    public String created_at, updated_at;
    public String alternate;
    public String meaning;
    public String caution;
    public String structure;
    public boolean formal;
    public String level;
    public String lesson_id;
    public boolean new_grammar;
    public String yomikata;
    public String nuance;
    public List<ExampleSentence> example_sentences;
    public List<SupplementalLink> supplemental_links;

    public GrammarPoint() {

    }

    public static Comparator<GrammarPoint> levelComparator = new Comparator<GrammarPoint>() {
        @Override
        public int compare(GrammarPoint point1, GrammarPoint point2) {

            String level1Str = point1.level.replaceAll("[^0-9]","");
            String level2Str = point2.level.replaceAll("[^0-9]", "");
            int number1 = Integer.parseInt(level1Str);
            int number2 = Integer.parseInt(level2Str);

            if (number1 != number2) {
                if (number1 > number2) {
                    return -1;
                } else {
                    return 1;
                }
            } else {
                return 0;
            }
        }
    };

    public static Comparator<GrammarPoint> IdComparator = new Comparator<GrammarPoint>() {
        @Override
        public int compare(GrammarPoint point1, GrammarPoint point2) {
            if (point1.id > point2.id) {
                return 1;
            } else {
                return -1;
            }
        }
    };

}
