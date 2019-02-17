package bunpro.jp.bunproapp.models;

import java.util.Comparator;
import java.util.List;

public class Lesson {

    public int id;
    public int jlpt_level;

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
