package bunpro.jp.bunproapp.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrammarPoint {
    private static List<GrammarPoint> grammarPointList = new ArrayList<>();
    private static List<List<GrammarPoint>> arrangedGrammarPointList = new ArrayList<>();
    private static Map<String, List<GrammarPoint>> pointsByLesson = new HashMap<>();

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

    public static List<GrammarPoint> getGrammarPointList() {
        return grammarPointList;
    }
    public static void setGrammarPointList(List<GrammarPoint> grammarPointList) {
        GrammarPoint.grammarPointList.clear();
        GrammarPoint.grammarPointList.addAll(grammarPointList);
    }

    public static List<List<GrammarPoint>> getArrangedGrammarPointList() {
        return arrangedGrammarPointList;
    }
    public static void setArrangedGrammarPointList(List<List<GrammarPoint>> arrangedGrammarPointList) {
        GrammarPoint.arrangedGrammarPointList.clear();
        GrammarPoint.arrangedGrammarPointList.addAll(arrangedGrammarPointList);
    }

    public static Map<String, List<GrammarPoint>> getPointsByLessonMap() {
        return pointsByLesson;
    }
    public static void setPointsByLessonMap(Map<String, List<GrammarPoint>> pointsByLesson) {
        GrammarPoint.pointsByLesson.clear();
        GrammarPoint.pointsByLesson.putAll(pointsByLesson);
    }
}
