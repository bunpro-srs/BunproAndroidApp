package bunpro.jp.bunproapp.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class History extends RealmObject {
    @PrimaryKey
    public int id;
    public String time;
    public boolean status;
    public int attempts;
    public int streak;

    public History() {

    }
}
