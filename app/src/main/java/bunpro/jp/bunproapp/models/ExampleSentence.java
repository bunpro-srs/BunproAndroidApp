package bunpro.jp.bunproapp.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ExampleSentence extends RealmObject {
    private static ExampleSentence currentExampleSentence;

    @PrimaryKey
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

    public static ExampleSentence getCurrentExampleSentence() {
        return currentExampleSentence;
    }
    public static void setCurrentExampleSentence(ExampleSentence currentExampleSentence) {
        ExampleSentence.currentExampleSentence = currentExampleSentence;
    }
}
