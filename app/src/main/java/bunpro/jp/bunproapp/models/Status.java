package bunpro.jp.bunproapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Status {
    private static List<Status> statusList = new ArrayList<>();

    private String name;
    private int status;
    private int total;

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

    public static List<Status> getStatusList() {
        return statusList;
    }
    public static void setStatusList(List<Status> statusList) {
        Status.statusList.clear();
        Status.statusList.addAll(statusList);
    }
}
