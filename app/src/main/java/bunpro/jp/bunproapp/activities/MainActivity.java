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
import bunpro.jp.bunproapp.ui.status.StatusFragment;
import bunpro.jp.bunproapp.models.GrammarPoint;
import bunpro.jp.bunproapp.models.Lesson;
import bunpro.jp.bunproapp.models.Review;
import bunpro.jp.bunproapp.models.Status;
import bunpro.jp.bunproapp.models.SupplementalLink;
import bunpro.jp.bunproapp.service.ApiService;
import bunpro.jp.bunproapp.service.JsonParser;
import bunpro.jp.bunproapp.utils.EspressoTestingIdlingResource;
import bunpro.jp.bunproapp.utils.SimpleCallbackListener;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ActivityImpl, FragNavController.RootFragmentListener {

    @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;
    @BindView(R.id.main_container) FrameLayout container;

    List<Status> jlptLevels = new ArrayList<>();

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
    // Temporary lists for /user/progress workaround
    public List<Integer> n2GrammarPointsTotal = new ArrayList<>();
    public List<Integer> n1GrammarPointsTotal = new ArrayList<>();
    public List<Integer> n2GrammarPointsLearned = new ArrayList<>();
    public List<Integer> n1GrammarPointsLearned = new ArrayList<>();

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
        exampleSentences = new ArrayList<>();
        supplementalLinks = new ArrayList<>();

        builder = FragNavController.newBuilder(savedInstanceState, getSupportFragmentManager(), R.id.main_container);

        hud = KProgressHUD.create(MainActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        initializeUI();
        fetchData();
    }

    private void fetchData() {
        MainActivity currentActivity = this;
        // Attempt to fetch reviews
        fetchReviews(new SimpleCallbackListener() {
            @Override
            public void success() {
                // Attempt to fetch grammar points
                fetchGrammarPoints(new SimpleCallbackListener() {
                    @Override
                    public void success() {
                        // Attempt to fetch example sentences
                        fetchExampleSentences(new SimpleCallbackListener() {
                            @Override
                            public void success() {
                                // Attempt to fetch supplemental links
                                fetchSupplementalLinks(new SimpleCallbackListener() {
                                    @Override
                                    public void success() {
                                    }
                                    @Override
                                    public void error(String errorMessage) {
                                        Log.e("Data retrieval error", errorMessage);
                                    }
                                });
                            }
                            @Override
                            public void error(String errorMessage) {
                                Log.e("Data retrieval error", errorMessage);
                            }
                        });
                        // Workaround for /user/progress v3 endpoint not working
                        if (n2GrammarPointsTotal.size() == 0) {
                            countProgress(reviews);
                            Fragment currentFragment = fragNavController.getCurrentFrag();
                            if (currentFragment instanceof StatusFragment) {
//                                ((StatusFragment)currentFragment).refreshStatus();
                            }
                        }
                    }
                    @Override
                    public void error(String errorMessage) {
                        Log.e("Data retrieval error", errorMessage);
                    }
                });
                // Try to update status fragment with review count
                Fragment currentFragment = fragNavController.getCurrentFrag();
//                if (currentFragment instanceof StatusFragment) {
//                    ((StatusFragment)currentFragment).calculateReviewsNumber();
//                }
            }
            @Override
            public void error(String errorMessage) {
                Log.e("Data retrieval error", errorMessage);
            }
        });
    }

    private void initializeUI() {

        final List<Fragment> fragments = new ArrayList<>();
        fragments.add(new StatusFragment());
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

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_status:
                                fragNavController.replaceFragment(new StatusFragment());
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

    /**
     * Temporary workaround for the non working /user/progress v3 endpoint
     */
    public void countProgress(List<Review> reviews) {
        n2GrammarPointsLearned = new ArrayList<>();
        n1GrammarPointsLearned = new ArrayList<>();
        n2GrammarPointsTotal = new ArrayList<>();
        n1GrammarPointsTotal = new ArrayList<>();
        List<GrammarPoint> n2GrammarPoints = new ArrayList<>(), n1GrammarPoints = new ArrayList<>();

        for (GrammarPoint grammarPointExample : grammarPoints) {
            if (grammarPointExample.level.equals("JLPT2")) {
                if (!n2GrammarPointsTotal.contains(grammarPointExample.id)) {
                    n2GrammarPointsTotal.add(grammarPointExample.id);
                }
                n2GrammarPoints.add(grammarPointExample);
            } else if (grammarPointExample.level.equals("JLPT1")) {
                if (!n1GrammarPointsTotal.contains(grammarPointExample.id)) {
                    n1GrammarPointsTotal.add(grammarPointExample.id);
                }
                n1GrammarPoints.add(grammarPointExample);
            }
        }
        for (Review review : reviews) {
            for (GrammarPoint grammarConcernedByReview : n2GrammarPoints) {
                if (review.grammar_point_id == grammarConcernedByReview.id) {
                    if (review.times_correct > 0 && n2GrammarPointsLearned.contains(review.id)) {
                        n2GrammarPointsLearned.add(review.id);
                    }
                }
            }
            for (GrammarPoint grammarConcernedByReview : n1GrammarPoints) {
                if (review.grammar_point_id == grammarConcernedByReview.id) {
                    if (review.times_correct > 0 && n1GrammarPointsLearned.contains(review.id)) {
                        n1GrammarPointsLearned.add(review.id);
                    }
                }
            }
        }
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
                return new StatusFragment();
            case INDEX_SEARCH:
                return SearchFragment.newInstance();
            case INDEX_SETTING:
                return SettingFragment.newInstance();
        }
        throw new IllegalStateException("Need to send an index that we know");
    }

    private void fetchReviews(SimpleCallbackListener callback) {
        ApiService apiService = new ApiService(this);
        apiService.getReviews(new ApiService.ApiCallbackListener() {
            @Override
            public void success(JSONObject jsonObject) {
                List<Review> reviews = JsonParser.getInstance(MainActivity.this).parseReviews(jsonObject);
                setReviews(reviews);
                callback.success();
            }

            @Override
            public void successAsJSONArray(JSONArray jsonArray) {
                Log.w("API Format changed", "JSONArray obtained instead of an JSONObject ! (Reviews)");
                callback.error("Grammar points API response format changed !");
            }

            @Override
            public void error(ANError anError) {
                Log.d("Error", anError.getErrorDetail());
                callback.error(anError.getErrorDetail());
            }
        });
    }

    private void fetchGrammarPoints(SimpleCallbackListener callback) {

        ApiService apiService = new ApiService(this);
        apiService.getGrammarPoints(new ApiService.ApiCallbackListener() {
            @Override
            public void success(JSONObject jsonObject) {
                Log.w("API Format changed", "JSONObject obtained instead of an JSONArray ! (Grammar points)");
                callback.error("Grammar points API response format changed !");
            }

            @Override
            public void successAsJSONArray(JSONArray jsonArray) {

                List<GrammarPoint> grammarPoints = JsonParser.getInstance(MainActivity.this).parseGrammarPoints(jsonArray);
                setGrammarPoints(grammarPoints);
                callback.success();
            }

            @Override
            public void error(ANError anError) {
                Log.e("Error", anError.getErrorDetail());
                callback.error(anError.getErrorDetail());
            }
        });
    }

    private void fetchExampleSentences(SimpleCallbackListener callback) {
        ApiService apiService = new ApiService(this);
        apiService.getExampleSentences(new ApiService.ApiCallbackListener() {
            @Override
            public void success(JSONObject jsonObject) {
                Log.w("API Format changed", "JSONObject obtained instead of an JSONArray ! (Example sentences)");
                callback.error("Grammar points API response format changed !");
            }

            @Override
            public void successAsJSONArray(JSONArray jsonArray) {

                List<ExampleSentence> exampleSentences = JsonParser.getInstance(MainActivity.this).parseExampleSentences(jsonArray);
                setExampleSentences(exampleSentences);
                callback.success();
            }

            @Override
            public void error(ANError anError) {
                Log.d("Error", anError.getErrorDetail());
                callback.error(anError.getErrorDetail());
            }
        });
    }

    private void fetchSupplementalLinks(SimpleCallbackListener callback) {
        ApiService apiService = new ApiService(MainActivity.this);
        apiService.getSupplementalLinks(new ApiService.ApiCallbackListener() {
            @Override
            public void success(JSONObject jsonObject) {
                Log.w("API Format changed", "JSONObject obtained instead of an JSONArray ! (Supplemental Links)");
                callback.error("Grammar points API response format changed !");
            }

            @Override
            public void successAsJSONArray(JSONArray jsonArray) {
                List<SupplementalLink> supplementalLinks = JsonParser.getInstance(MainActivity.this).parseSupplimentalLinks(jsonArray);
                setSupplimentalLinks(supplementalLinks);
                callback.success();
            }

            @Override
            public void error(ANError anError) {
                Log.d("Error", anError.getErrorDetail());
                callback.error(anError.getErrorDetail());
            }
        });
    }

}
