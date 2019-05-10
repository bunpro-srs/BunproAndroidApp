package bunpro.jp.bunproapp.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import io.realm.RealmObject;

public class SupplementalLink extends RealmObject {
    private static List<SupplementalLink> supplementalLinkList = new ArrayList<>();

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

    public static List<SupplementalLink> getSupplementalLinkList() {
        return supplementalLinkList;
    }
    public static void setSupplementalLinkList(List<SupplementalLink> supplementalLinkList) {
        SupplementalLink.supplementalLinkList.clear();
        SupplementalLink.supplementalLinkList.addAll(supplementalLinkList);
    }
}
