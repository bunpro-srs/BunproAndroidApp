package bunpro.jp.bunprosrs.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.ncapdevi.fragnav.FragNavController;

import java.util.ArrayList;
import java.util.List;

import bunpro.jp.bunprosrs.R;
import bunpro.jp.bunprosrs.fragments.ExampleFragment;
import bunpro.jp.bunprosrs.fragments.LevelDetailFragment;
import bunpro.jp.bunprosrs.fragments.SearchFragment;
import bunpro.jp.bunprosrs.fragments.SettingFragment;
import bunpro.jp.bunprosrs.fragments.StatusDetailFragment;
import bunpro.jp.bunprosrs.fragments.StatusFragment;
import bunpro.jp.bunprosrs.fragments.WordDetailFragment;
import bunpro.jp.bunprosrs.models.GrammarPoint;
import bunpro.jp.bunprosrs.models.Lesson;
import bunpro.jp.bunprosrs.models.Review;
import bunpro.jp.bunprosrs.models.Status;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ActivityImpl, FragNavController.RootFragmentListener {

    @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;
    @BindView(R.id.container) FrameLayout container;

    List<Status> jlptLevels;

    FragNavController.Builder builder;
    FragNavController fragNavController;

    private static final int INDEX_STATUS = 31;
    private static final int INDEX_SEARCH = 32;
    private static final int INDEX_SETTING = 33;

    private List<Lesson> lessons;
    private List<Review> reviews;
    private List<GrammarPoint> grammarPoints;
    private List<List<GrammarPoint>> arrangedGrammarPoints;

    private GrammarPoint selectedGrammarPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        lessons = new ArrayList<>();
        grammarPoints = new ArrayList<>();
        reviews = new ArrayList<>();
        arrangedGrammarPoints = new ArrayList<>();

        builder = FragNavController.newBuilder(savedInstanceState, getSupportFragmentManager(), R.id.container);

        final List<Fragment> fragments = new ArrayList<>();
        fragments.add(StatusFragment.newInstance());
        fragments.add(SearchFragment.newInstance());
        fragments.add(SettingFragment.newInstance());
        builder.rootFragments(fragments);

        fragNavController = builder.build();
        builder.rootFragmentListener(this, 3);

        /*  Initialization  */

        jlptLevels = new ArrayList<>();

        /*  end  */

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_status:
                                fragNavController.switchTab(FragNavController.TAB1);
                                break;
                            case R.id.action_search:
                                fragNavController.switchTab(FragNavController.TAB2);
                                break;
                            case R.id.action_settings:
                                fragNavController.switchTab(FragNavController.TAB3);
                                break;
                        }
                        return true;
                    }
                });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (fragNavController != null) {
            fragNavController.onSaveInstanceState(outState);
        }
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

    @Override
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    @Override
    public List<Review> getReviews() {
        return this.reviews;
    }

    @Override
    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    @Override
    public List<Lesson> getLessons() {
        return this.lessons;
    }

    @Override
    public void setGrammarPoints(List<GrammarPoint> grammarPoints) {
        this.grammarPoints = grammarPoints;
    }

    @Override
    public List<GrammarPoint> getGrammarPoints() {
        return this.grammarPoints;
    }

    @Override
    public void setArrangedGrammarPoints(List<List<GrammarPoint>> grammarPoints) {
        this.arrangedGrammarPoints = grammarPoints;
    }

    @Override
    public List<List<GrammarPoint>> getArrangedGrammarPoints() {
        return this.arrangedGrammarPoints;
    }

    @Override
    public void setGrammarPoint(GrammarPoint point) {
        this.selectedGrammarPoint = point;
    }

    @Override
    public GrammarPoint getGrammarPoint() {
        return this.selectedGrammarPoint;
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
                return StatusFragment.newInstance();
            case INDEX_SEARCH:
                return SearchFragment.newInstance();
            case INDEX_SETTING:
                return SettingFragment.newInstance();
        }
        throw new IllegalStateException("Need to send an index that we know");
    }
}
