package bunpro.jp.bunproapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Status {

    public String name;
    public int status;
    public int total;

    public Status() {

    }

    public Status(String name, int status, int total) {
        this.name = name;
        this.total = total;
        this.status = status;
    }



    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getStatus() {
        return this.status;
    }

    public int getTotal() {
        return this.total;
    }

    public String getName() {
        return this.name;
    }

}
