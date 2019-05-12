package bunpro.jp.bunproapp.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class GrammarPoint extends RealmObject {
    /* Temporary workaround for non working API endpoint */
    private static List<Integer> n2GrammarPointsLearned = new ArrayList<>();
    private static List<Integer> n1GrammarPointsLearned = new ArrayList<>();
    private static List<Integer> n2GrammarPointsTotal = new ArrayList<>();
    private static List<Integer> n1GrammarPointsTotal = new ArrayList<>();

    @PrimaryKey
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

    /* Temporary workaround for non working API endpoint */
    public static List<Integer> getN2GrammarPointsLearned() {
        return n2GrammarPointsLearned;
    }
    public static void setN2GrammarPointsLearned(List<Integer> n2GrammarPointsLearned) {
        GrammarPoint.n2GrammarPointsLearned = n2GrammarPointsLearned;
    }
    public static List<Integer> getN1GrammarPointsLearned() {
        return n1GrammarPointsLearned;
    }
    public static void setN1GrammarPointsLearned(List<Integer> n1GrammarPointsLearned) {
        GrammarPoint.n1GrammarPointsLearned = n1GrammarPointsLearned;
    }
    public static List<Integer> getN2GrammarPointsTotal() {
        return n2GrammarPointsTotal;
    }
    public static void setN2GrammarPointsTotal(List<Integer> n2GrammarPointsTotal) {
        GrammarPoint.n2GrammarPointsTotal = n2GrammarPointsTotal;
    }

    public static List<Integer> getN1GrammarPointsTotal() {
        return n1GrammarPointsTotal;
    }

    public static void setN1GrammarPointsTotal(List<Integer> n1GrammarPointsTotal) {
        GrammarPoint.n1GrammarPointsTotal = n1GrammarPointsTotal;
    }
}
