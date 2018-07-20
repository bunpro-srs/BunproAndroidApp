package bunpro.jp.bunprosrs.models;

import android.os.Parcel;
import android.os.Parcelable;

public class SupplementalLink implements Parcelable {

    public int id;
    public int grammar_point_id;
    public String site;
    public String link;
    public String description;
    public String created_at, updated_at;

    public SupplementalLink() {

    }

    protected SupplementalLink(Parcel in) {
        id = in.readInt();
        grammar_point_id = in.readInt();
        site = in.readString();
        link = in.readString();
        description = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
    }

    public static final Creator<SupplementalLink> CREATOR = new Creator<SupplementalLink>() {
        @Override
        public SupplementalLink createFromParcel(Parcel in) {
            return new SupplementalLink(in);
        }

        @Override
        public SupplementalLink[] newArray(int size) {
            return new SupplementalLink[0];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(grammar_point_id);
        parcel.writeString(site);
        parcel.writeString(link);
        parcel.writeString(description);
        parcel.writeString(created_at);
        parcel.writeString(updated_at);
    }
}
