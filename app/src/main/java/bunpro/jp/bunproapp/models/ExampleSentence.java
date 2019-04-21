package bunpro.jp.bunproapp.models;

import java.util.Comparator;

public class ExampleSentence {

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
}
