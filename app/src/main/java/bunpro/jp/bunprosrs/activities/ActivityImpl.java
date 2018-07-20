package bunpro.jp.bunprosrs.activities;

import android.support.v4.app.Fragment;

import java.util.List;

import bunpro.jp.bunprosrs.models.Status;

public interface ActivityImpl {

    void replaceFragment(Fragment fragment);

    void setjlptLevel(List<Status> levels);
    List<Status> getjlptLevel();
}
