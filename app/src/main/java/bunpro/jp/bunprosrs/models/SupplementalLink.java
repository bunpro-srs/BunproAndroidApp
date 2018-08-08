package bunpro.jp.bunprosrs.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;

public class SupplementalLink {

    public int id;
    public int grammar_point_id;
    public String site;
    public String link;
    public String description;
    public String created_at, updated_at;

    public SupplementalLink() {

    }

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
