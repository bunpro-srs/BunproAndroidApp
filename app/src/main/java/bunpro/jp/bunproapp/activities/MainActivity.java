package bunpro.jp.bunproapp.activities;

import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.ncapdevi.fragnav.FragNavController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import bunpro.jp.bunproapp.R;
import bunpro.jp.bunproapp.models.ExampleSentence;
import bunpro.jp.bunproapp.fragments.SearchFragment;
import bunpro.jp.bunproapp.fragments.SettingFragment;
import bunpro.jp.bunproapp.fragments.StatusFragment;
import bunpro.jp.bunproapp.models.GrammarPoint;
import bunpro.jp.bunproapp.models.Lesson;
import bunpro.jp.bunproapp.models.Review;
import bunpro.jp.bunproapp.models.Status;
import bunpro.jp.bunproapp.models.SupplementalLink;
import bunpro.jp.bunproapp.service.ApiService;
import bunpro.jp.bunproapp.service.JsonParser;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ActivityImpl, FragNavController.RootFragmentListener {

    @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;
    @BindView(R.id.container) FrameLayout container;

    List<Status> jlptLevels;

    FragNavController.Builder builder;
    FragNavController fragNavController;
    Toast exitToast;

    private static final int INDEX_STATUS = 31;
    private static final int INDEX_SEARCH = 32;
    private static final int INDEX_SETTING = 33;

    private List<Lesson> lessons;
    private List<Review> reviews;
    private List<GrammarPoint> grammarPoints;
    private List<List<GrammarPoint>> arrangedGrammarPoints;
    private List<ExampleSentence> exampleSentences;
    private List<SupplementalLink> supplementalLinks;

    private GrammarPoint selectedGrammarPoint;
    private ExampleSentence selectedSentence;

    KProgressHUD hud;

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

        hud = KProgressHUD.create(MainActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        fetchReviews();

    }

    private void initializeUI() {

        final List<Fragment> fragments = new ArrayList<>();
        fragments.add(StatusFragment.newInstance());
        fragments.add(SearchFragment.newInstance());
        fragments.add(SettingFragment.newInstance());
        builder.rootFragments(fragments);

        // Workaround for random crash ; see https://github.com/hulab/debugkit/issues/3
        try {
            fragNavController = builder.build();
            builder.rootFragmentListener(this, 3);
        } catch (IllegalStateException e) {
            Log.e("IllegalStateException", "Initializing UI failed due to a button event called after a state saving.");
        }

        /*  Initialization  */

        jlptLevels = new ArrayList<>();

        /*  end  */

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_status:
                                fragNavController.replaceFragment(StatusFragment.newInstance());
                                break;
                            case R.id.action_search:
                                fragNavController.replaceFragment(SearchFragment.newInstance());
                                break;
                            case R.id.action_settings:
                                fragNavController.replaceFragment(SettingFragment.newInstance());
                                break;
                        }
                        return true;
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (exitToast == null || exitToast.getView() == null || exitToast.getView().getWindowToken() == null) {
            exitToast = Toast.makeText(this, R.string.press_again_to_exit, Toast.LENGTH_SHORT);
            exitToast.show();
        } else {
            exitToast.cancel();
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Bug happening on API > 11 causing a random crash when saving instance : https://stackoverflow.com/questions/7469082/getting-exception-illegalstateexception-can-not-perform-this-action-after-onsa/10261438#10261438
        outState.putString("WORKAROUND_FOR_RANDOM_CRASH", "WORKAROUND_FOR_RANDOM_CRASH");
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
    public void setExampleSentense(ExampleSentence sentense) {
        this.selectedSentence = sentense;
    }

    @Override
    public void setExampleSentences(List<ExampleSentence> sentences) {
        this.exampleSentences = sentences;
    }

    @Override
    public List<ExampleSentence> getExampleSentences() {
        return this.exampleSentences;
    }

    @Override
    public ExampleSentence getExampleSentence() {
        return this.selectedSentence;
    }

    @Override
    public void setSupplimentalLinks(List<SupplementalLink> links) {
        this.supplementalLinks = links;
    }

    @Override
    public List<SupplementalLink> getSupplimentalLinks() {
        return this.supplementalLinks;
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

    private void fetchGrammarPoints() {

        ApiService apiService = new ApiService(this);
        apiService.getGrammarPoints(new ApiService.CallbackListener() {
            @Override
            public void success(JSONObject jsonObject) {

            }

            @Override
            public void successAsJSONArray(JSONArray jsonArray) {

                List<GrammarPoint> grammarPoints = JsonParser.getInstance(MainActivity.this).parseGrammarPoints(jsonArray);
                setGrammarPoints(grammarPoints);
                fetchExampleSentences();
            }

            @Override
            public void error(ANError anError) {
                Log.d("Error", anError.getErrorDetail());
            }
        });
    }

    private void fetchExampleSentences() {
        ApiService apiService = new ApiService(this);
        apiService.getExampleSentences(new ApiService.CallbackListener() {
            @Override
            public void success(JSONObject jsonObject) {

            }

            @Override
            public void successAsJSONArray(JSONArray jsonArray) {

                List<ExampleSentence> exampleSentences = JsonParser.getInstance(MainActivity.this).parseExampleSentences(jsonArray);
                setExampleSentences(exampleSentences);
                fetchSupplimentalLinks();
            }

            @Override
            public void error(ANError anError) {
                Log.d("Error", anError.getErrorDetail());
            }
        });
    }

    private void fetchSupplimentalLinks() {
        ApiService apiService = new ApiService(MainActivity.this);
        apiService.getSupplimentalLinks(new ApiService.CallbackListener() {
            @Override
            public void success(JSONObject jsonObject) {

            }

            @Override
            public void successAsJSONArray(JSONArray jsonArray) {
                if (hud.isShowing()) {
                    hud.dismiss();
                }

                List<SupplementalLink> supplementalLinks = JsonParser.getInstance(MainActivity.this).parseSupplimentalLinks(jsonArray);
                setSupplimentalLinks(supplementalLinks);
                initializeUI();
            }

            @Override
            public void error(ANError anError) {
                Log.d("Error", anError.getErrorDetail());
            }
        });
    }

    private void fetchReviews() {

        if (!hud.isShowing()) {
            hud.show();
        }

        ApiService apiService = new ApiService(this);
        apiService.getReviews(new ApiService.CallbackListener() {
            @Override
            public void success(JSONObject jsonObject) {
                List<Review> reviews = JsonParser.getInstance(MainActivity.this).parseReviews(jsonObject);
                setReviews(reviews);
                fetchGrammarPoints();
            }

            @Override
            public void successAsJSONArray(JSONArray jsonArray) {
            }

            @Override
            public void error(ANError anError) {

                Log.d("Error", anError.getErrorDetail());
            }
        });

    }

}
