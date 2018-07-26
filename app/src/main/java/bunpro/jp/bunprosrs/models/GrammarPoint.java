package bunpro.jp.bunprosrs.models;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Comparator;
import java.util.List;

public class GrammarPoint implements Parcelable {

    public int id;
    public String title;
    public String created_at, updated_at;
    public String alternate;
    public String meaning;
    public String caution;
    public String structure;
    public boolean formal;
    public String level;
    public String lesson_id;
    public boolean new_grammar;
    public String yomikata;
    public String nuance;
    public List<ExampleSentence> example_sentences;
    public List<SupplementalLink> supplemental_links;

    public GrammarPoint() {

    }

    public static Comparator<GrammarPoint> levelComparator = new Comparator<GrammarPoint>() {
        @Override
        public int compare(GrammarPoint point1, GrammarPoint point2) {

            String level1Str = point1.level.replaceAll("[^0-9]","");
            String level2Str = point2.level.replaceAll("[^0-9]", "");
            int number1 = Integer.parseInt(level1Str);
            int number2 = Integer.parseInt(level2Str);

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

    protected GrammarPoint(Parcel in) {
        id = in.readInt();
        title = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
        alternate = in.readString();
        meaning = in.readString();
        caution = in.readString();
        structure = in.readString();
        formal = in.readByte() != 0;
        level = in.readString();
        lesson_id = in.readString();
        new_grammar = in.readByte() != 0;
        yomikata = in.readString();
        nuance = in.readString();
        example_sentences = in.createTypedArrayList(ExampleSentence.CREATOR);
        supplemental_links = in.createTypedArrayList(SupplementalLink.CREATOR);
    }

    public static final Creator<GrammarPoint> CREATOR = new Creator<GrammarPoint>() {
        @Override
        public GrammarPoint createFromParcel(Parcel in) {
            return new GrammarPoint(in);
        }

        @Override
        public GrammarPoint[] newArray(int size) {
            return new GrammarPoint[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(created_at);
        parcel.writeString(updated_at);
        parcel.writeString(alternate);
        parcel.writeString(meaning);
        parcel.writeString(caution);
        parcel.writeString(structure);
        parcel.writeByte((byte) (formal ? 1 : 0));
        parcel.writeString(level);
        parcel.writeString(lesson_id);
        parcel.writeByte((byte) (new_grammar ? 1 : 0));
        parcel.writeString(yomikata);
        parcel.writeString(nuance);
        parcel.writeTypedList(example_sentences);
        parcel.writeTypedList(supplemental_links);
    }
}
