package bunpro.jp.bunproapp.models;

import java.util.Comparator;

import io.realm.RealmObject;

public class SupplementalLink extends RealmObject {

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
