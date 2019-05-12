package bunpro.jp.bunproapp.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class SupplementalLink extends RealmObject {
    @PrimaryKey
    public int id;
    public String type;
    public int grammar_point_id;
    public String site;
    public String link;
    public String description;

    public static Comparator<SupplementalLink> IdComparator = new Comparator<SupplementalLink>() {
        @Override
        public int compare(SupplementalLink point1, SupplementalLink point2) {
            if (point1.id > point2.id) {
                return 1;
            } else {
                return -1;
            }
        }
    };
}
