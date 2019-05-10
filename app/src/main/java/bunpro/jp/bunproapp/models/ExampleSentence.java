package bunpro.jp.bunproapp.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ExampleSentence {
    private static List<ExampleSentence> exampleSentenceList = new ArrayList<>();
    private static ExampleSentence currentExampleSentence;

    public int id;
    public int grammar_point_id;
    public String structure;
    public String japanese;
    public String english;
    public String alternate_japanese;
    public String created_at, updated_at;
    public String audio_link;
    public String nuance;
    public int sentence_order;
    public String type;

    public ExampleSentence() {

    }

    public static Comparator<ExampleSentence> IdComparator = new Comparator<ExampleSentence>() {
        @Override
        public int compare(ExampleSentence point1, ExampleSentence point2) {
            if (point1.id > point2.id) {
                return 1;
            } else {
                return -1;
            }
        }
    };

    public static List<ExampleSentence> getExampleSentenceList() {
        return exampleSentenceList;
    }
    public static void setExampleSentenceList(List<ExampleSentence> exampleSentenceList) {
        ExampleSentence.exampleSentenceList.clear();
        ExampleSentence.exampleSentenceList.addAll(exampleSentenceList);
    }

    public static ExampleSentence getCurrentExampleSentence() {
        return currentExampleSentence;
    }
    public static void setCurrentExampleSentence(ExampleSentence currentExampleSentence) {
        ExampleSentence.currentExampleSentence = currentExampleSentence;
    }
}
