package bunpro.jp.bunproapp.ui.home;

import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.ncapdevi.fragnav.FragNavController;

import java.util.ArrayList;
import java.util.List;

import bunpro.jp.bunproapp.R;
import bunpro.jp.bunproapp.ui.search.SearchFragment;
import bunpro.jp.bunproapp.ui.settings.SettingFragment;
import bunpro.jp.bunproapp.ui.status.StatusFragment;
import bunpro.jp.bunproapp.utils.EspressoTestingIdlingResource;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements HomeContract.View, FragNavController.RootFragmentListener {

    HomeContract.Presenter homePresenter;

    @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;
    @BindView(R.id.main_container) FrameLayout container;


    FragNavController.Builder builder;
    FragNavController fragNavController;
    Toast exitToast;

    private static final int INDEX_STATUS = 31;
    private static final int INDEX_SEARCH = 32;
    private static final int INDEX_SETTING = 33;

    KProgressHUD hud;

    public Context getContext() {
        return this;
    }

    /**
     * Replace main fragment with given fragment
     * @param fragment New fragment
     */
    @Override
    public void replaceFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_container, fragment);
        ft.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homePresenter = new HomePresenter(this);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        builder = FragNavController.newBuilder(savedInstanceState, getSupportFragmentManager(), R.id.main_container);

        hud = KProgressHUD.create(HomeActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        initializeUI();
        homePresenter.fetchData();
    }

    private void initializeUI() {

        final List<Fragment> fragments = new ArrayList<>();
        fragments.add(new StatusFragment());
        fragments.add(new SearchFragment());
        fragments.add(new SettingFragment());
        builder.rootFragments(fragments);

        // Workaround for random crash ; see https://github.com/hulab/debugkit/issues/3
        try {
            fragNavController = builder.build();
            builder.rootFragmentListener(this, 3);
        } catch (IllegalStateException e) {
            Log.e("IllegalStateException", "Initializing UI failed due to a button event called after a state saving.");
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_status:
                                fragNavController.replaceFragment(new StatusFragment());
                                break;
                            case R.id.action_search:
                                fragNavController.replaceFragment(new SearchFragment());
                                break;
                            case R.id.action_settings:
                                fragNavController.replaceFragment(new SettingFragment());
                                break;
                        }
                        return true;
                    }
                });
        EspressoTestingIdlingResource.decrement("login_and_loading");
    }

    /**
     * Allowing double back button to exit if it is status fragment. Else go to status
     */
    @Override
    public void onBackPressed() {
        Fragment currentFragment = fragNavController.getCurrentFrag();
        if (currentFragment instanceof StatusFragment || currentFragment instanceof SearchFragment || currentFragment instanceof SettingFragment) {
            if (exitToast == null || exitToast.getView() == null || exitToast.getView().getWindowToken() == null) {
                exitToast = Toast.makeText(this, R.string.press_again_to_exit, Toast.LENGTH_SHORT);
                exitToast.show();
            } else {
                exitToast.cancel();
                super.onBackPressed();
            }
        } else {
            fragNavController.replaceFragment(new StatusFragment());
        }
    }

    /**
     * Bug happening on API > 11 causing a random crash when saving instance : https://stackoverflow.com/questions/7469082/getting-exception-illegalstateexception-can-not-perform-this-action-after-onsa/10261438#10261438
     * @param outState Bundle to pass on
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("WORKAROUND_FOR_RANDOM_CRASH", "WORKAROUND_FOR_RANDOM_CRASH");
        super.onSaveInstanceState(outState);
        if (fragNavController != null) {
            fragNavController.onSaveInstanceState(outState);
        }
    }

    @Override
    public void addFragment(Fragment fragment) {
        if (fragNavController != null) {
            fragNavController.pushFragment(fragment);
        }
    }

    @Override
    public void popFragment() {
        if (fragNavController != null) {
            fragNavController.popFragment();
        }
    }

    @Override
    public Fragment getRootFragment(int index) {

        switch (index) {
            case INDEX_STATUS:
                return new StatusFragment();
            case INDEX_SEARCH:
                return new SearchFragment();
            case INDEX_SETTING:
                return new SettingFragment();
        }
        throw new IllegalStateException("Need to send an index that we know");
    }

}
