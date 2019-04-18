package bunpro.jp.bunproapp.models;

import android.util.Log;

import java.util.Comparator;
import java.util.List;

public class GrammarPoint {

    public int id;
    public String title;
    public String meaning;
    public String caution;
    public String structure;
    public String level;
    public int lesson_id;
    public String yomikata;
    public String nuance;
    public boolean incomplete;
    public int grammar_order;

    public GrammarPoint() {

    }

    public static Comparator<GrammarPoint> levelComparator = new Comparator<GrammarPoint>() {
        @Override
        public int compare(GrammarPoint point1, GrammarPoint point2) {
            int number1, number2;
            try {
                String level1Str = point1.level.replaceAll("[^0-9]","");
                number1 = Integer.parseInt(level1Str);
            } catch (NumberFormatException e) {
                number1 = 0;
            }
            try {
                String level2Str = point2.level.replaceAll("[^0-9]", "");
                number2 = Integer.parseInt(level2Str);
            } catch (NumberFormatException e) {
                number2 = 0;
            }

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
