package bunpro.jp.bunprosrs.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import bunpro.jp.bunprosrs.R;
import bunpro.jp.bunprosrs.fragments.SearchFragment;
import bunpro.jp.bunprosrs.fragments.SettingFragment;
import bunpro.jp.bunprosrs.fragments.StatusFragment;
import bunpro.jp.bunprosrs.models.Status;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ActivityImpl {

    @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;
    @BindView(R.id.container) FrameLayout container;

    List<Status> jlptLevels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        /*  Initialization  */

        jlptLevels = new ArrayList<>();

        /*  end  */

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_status:
                                Fragment statusFragment = new StatusFragment();
                                replaceFragment(statusFragment);
                                break;
                            case R.id.action_search:
                                Fragment searchFragment = new SearchFragment();
                                replaceFragment(searchFragment);
                                break;
                            case R.id.action_settings:
                                Fragment settingFragment = new SettingFragment();
                                replaceFragment(settingFragment);
                                break;

                        }
                        return true;
                    }
                });

        replaceFragment(new StatusFragment());
    }


    @Override
    public void replaceFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, fragment);
        ft.commit();
    }

    @Override
    public void setjlptLevel(List<Status> levels) {

        jlptLevels = levels;

    }

    @Override
    public List<Status> getjlptLevel() {
        return jlptLevels;
    }
}
